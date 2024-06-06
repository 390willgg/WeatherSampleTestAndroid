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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherappsample1yt.R
import com.example.weatherappsample1yt.databinding.ActivityMainBinding
import com.example.weatherappsample1yt.presentation.view.city.CityActivity
import com.example.weatherappsample1yt.presentation.view.serviceLocation.ServiceLocationViewModel
import com.example.weatherappsample1yt.presentation.view.serviceLocation.ServiceLocationViewModelFactory
import com.example.weatherappsample1yt.presentation.view.settings.SettingActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var serviceLocationFactory: ServiceLocationViewModelFactory

    private val pagerAdapter by lazy { ScreenSlidePagerAdapter(this) }
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var serviceLocationViewModel: ServiceLocationViewModel

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

    private lateinit var viewPager: ViewPager2

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

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = pagerAdapter

        checkAndRequestPermissions()
        binding.viewPager.adapter = pagerAdapter
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (intent.hasExtra("lat") && intent.hasExtra("lon")) {
            val latitude = intent.getDoubleExtra("lat", 0.0)
            val longitude = intent.getDoubleExtra("lon", 0.0)
            val cityName = intent.getStringExtra("name") ?: "Unknown"
            addCityFragment(latitude, longitude, cityName)
        } else {
            serviceLocationViewModel.getLocation(this)
            serviceLocationViewModel.location.observe(this) { location ->
                val latitude = location?.latitude ?: 0.0
                val longitude = location?.longitude ?: 0.0
                val cityName = intent.getStringExtra("name") ?: "Unknown"
                addCityFragment(latitude, longitude, cityName)
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

    private fun addCityFragment(latitude: Double?, longitude: Double, cityName: String) {
        Log.i("MainActivity", "MainActivity hashCode: ${this.hashCode()}")
        Log.i("MainActivity", "PagerAdapter hashCode: ${pagerAdapter.hashCode()}")
        val cityData = CityData(latitude ?: 0.0, longitude, cityName)
        pagerAdapter.addFragment(cityData)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private inner class ScreenSlidePagerAdapter(fa: AppCompatActivity) : FragmentStateAdapter(fa) {
        private val fragments = mutableListOf<Fragment>()
        private val cityData = mutableListOf<CityData>()

        fun addFragment(cityData: CityData) {
            this.cityData.add(cityData)
            val fragment = LocationFragment.newInstance(
                cityData.latitude, cityData.longitude, cityData.cityName
            )
            this.fragments.add(fragment)

            Log.i("MainActivity", "Fragment size: ${fragments.size}")
            Log.i("MainActivity", "Fragment contains: $fragments")
            notifyItemInserted(fragments.size - 1)
        }

        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment = fragments[position]
    }

    data class CityData(
        val latitude: Double, val longitude: Double, val cityName: String
    )
}