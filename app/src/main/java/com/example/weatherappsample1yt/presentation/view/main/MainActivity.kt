package com.example.weatherappsample1yt.presentation.view.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuInflater
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherappsample1yt.R
import com.example.weatherappsample1yt.data.model.format.CityData
import com.example.weatherappsample1yt.databinding.ActivityMainBinding
import com.example.weatherappsample1yt.presentation.view.city.CityActivity
import com.example.weatherappsample1yt.presentation.view.serviceLocation.ServiceLocationViewModel
import com.example.weatherappsample1yt.presentation.view.serviceLocation.ServiceLocationViewModelFactory
import com.example.weatherappsample1yt.presentation.view.settings.SettingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var serviceLocationFactory: ServiceLocationViewModelFactory

    private val cityDataViewModel: CityDataViewModel by viewModels()
    private lateinit var serviceLocationViewModel: ServiceLocationViewModel

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2
    private val pagerAdapter by lazy { ScreenSlidePagerAdapter(this) }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.entries.all { it.value }
        if (granted) {
            serviceLocationViewModel.getLocation(this)
        } else {
            Toast.makeText(this, "Permission denied for access to location", Toast.LENGTH_SHORT)
                .show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        serviceLocationViewModel =
            ViewModelProvider(this, serviceLocationFactory)[ServiceLocationViewModel::class.java]

        binding.addCity.setOnClickListener {
            showPopMenu(it)
        }

        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = pagerAdapter

        checkAndRequestPermissions()
        binding.viewPager.adapter = pagerAdapter

        serviceLocationViewModel.location.observe(this) {
            it?.let {
                cityDataViewModel.addCityData(CityData(it.latitude, it.longitude, "Unknown"))
            }
        }

        cityDataViewModel.removedItemPosition.observe(this) {
            it?.let {
                Log.i("MainActivity", "Removed item position: $it")
                CoroutineScope(Dispatchers.Main).launch {
                    pagerAdapter.removeFragments(it)
                }
            }
        }

        cityDataViewModel.observerCityData().observe(this) {
            Log.i("MainActivity", "CityData: $it")
            it?.let { cityDataList ->
                CoroutineScope(Dispatchers.Main).launch {
                    pagerAdapter.updateFragments(cityDataList)
                }
            }
        }

    }

    private fun showPopMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.nav_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    startActivity(Intent(this, SettingActivity::class.java))
                    true
                }

                R.id.location -> {
                    startActivity(Intent(this, CityActivity::class.java))
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    private fun checkAndRequestPermissions() {
        val requiredPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val allPermissionsGranted = requiredPermissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
        if (!allPermissionsGranted) {
            requestPermissionLauncher.launch(requiredPermissions)
        } else {
            serviceLocationViewModel.getLocation(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private inner class ScreenSlidePagerAdapter(fa: AppCompatActivity) : FragmentStateAdapter(fa) {
        var cityData = listOf<CityData>()
            set(value) {
                val diffCallback = object : DiffUtil.Callback() {
                    override fun getOldListSize() = field.size
                    override fun getNewListSize() = value.size
                    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                        field[oldItemPosition] == value[newItemPosition]

                    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                        field[oldItemPosition] == value[newItemPosition]
                }
                val diffResult = DiffUtil.calculateDiff(diffCallback)
                field = value
                diffResult.dispatchUpdatesTo(this)
            }

        fun addFragment(cityData: CityData) {
            this.cityData += cityData
            CoroutineScope(Dispatchers.Main).launch {
                notifyDataSetChanged()
            }
        }

        fun clearFragments() {
            this.cityData = emptyList()
            CoroutineScope(Dispatchers.Main).launch {
                notifyDataSetChanged()
            }
        }

        fun removeFragments(position: Int) {
            if (position < cityData.size) {
                this.cityData = this.cityData.filterIndexed() { index, _ -> index != position }
                CoroutineScope(Dispatchers.Main).launch {
                    notifyItemRemoved(position)
                }
            }
        }

        fun updateFragments(newCityData: List<CityData>) {
            clearFragments()
            newCityData.forEach { cityData ->
                addFragment(cityData)
            }
        }

        override fun getItemCount(): Int = cityData.size

        override fun createFragment(position: Int): Fragment {
            val cityDataItem = cityData[position]
            return LocationFragment.newInstance(
                cityDataItem.latitude, cityDataItem.longitude, cityDataItem.cityName
            )
        }

        override fun getItemId(position: Int): Long {
            return cityData[position].latitude.hashCode()
                .toLong() * 31 + cityData[position].longitude.hashCode()
        }

        override fun containsItem(itemId: Long): Boolean {
            return cityData.any {
                (it.latitude.hashCode().toLong() * 31 + it.longitude.hashCode()) == itemId
            }
        }
    }
}