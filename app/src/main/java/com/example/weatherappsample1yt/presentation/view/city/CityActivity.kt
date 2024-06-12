package com.example.weatherappsample1yt.presentation.view.city

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.Modifier
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherappsample1yt.R
import com.example.weatherappsample1yt.data.model.format.DataItemCity
import com.example.weatherappsample1yt.databinding.ActivityCityBinding
import com.example.weatherappsample1yt.domain.useCase.preferencesUser.PreferencesUseCase
import com.example.weatherappsample1yt.presentation.view.city.componentUi.CitySearchScreen
import com.example.weatherappsample1yt.presentation.view.main.CityDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CityActivity : AppCompatActivity() {
    @Inject
    lateinit var preferencesUseCase: PreferencesUseCase

    private lateinit var binding: ActivityCityBinding

    private fun dpToPx(context: Context): Int {
        return (16 * context.resources.displayMetrics.density).toInt()
    }

    private val citiesListViewModel: CityViewModel by viewModels()
    private val cityDataHistoryViewModel: CityDataViewModel by viewModels()

    private val cityAdapter by lazy {
        CityAdapter {
            cityDataHistoryViewModel.addCityData(it)
        }
    }

    private val cityHistoryAdapter: CityDataHistoryAdapter by lazy {
        CityDataHistoryAdapter { position: Int ->
            cityDataHistoryViewModel.removeCityData(position)
            cityHistoryAdapter.notifyItemRemoved(position)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val additionalPadding = dpToPx(v.context)
            v.setPadding(
                systemBars.left + additionalPadding,
                systemBars.top + additionalPadding,
                systemBars.right + additionalPadding,
                systemBars.bottom + additionalPadding
            )
            insets
        }

        try {
            setUpRecyclerView()
            observeViewModel()
        } catch (e: Exception) {
            Log.i("CityActivity", "Error setting up recycler view")
            e.printStackTrace()
        }

        binding.composeView.setContent {
            CitySearchScreen(
                modifier = Modifier, cityViewModel = citiesListViewModel, cityAdapter = cityAdapter
            )
        }

        citiesListViewModel.getCitiesList("London", 5)

        citiesListViewModel.citiesList.observe(this) { cityWeatherData ->
            cityWeatherData?.let {
                val cities: ArrayList<DataItemCity>? = it.data
                cities?.forEach { city ->
                    println(city.cityName)
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.cityView.apply {
            layoutManager = LinearLayoutManager(
                this@CityActivity, LinearLayoutManager.VERTICAL, false
            )
            adapter = cityAdapter

            binding.cityHistoryView.apply {
                layoutManager = LinearLayoutManager(
                    this@CityActivity, LinearLayoutManager.VERTICAL, false
                )
                adapter = cityHistoryAdapter
            }
        }
    }

    private fun observeViewModel() {
        citiesListViewModel.citiesList.observe(this) { cityWeatherData ->
            cityWeatherData?.let {
                cityAdapter.differ.submitList(it.data)
            }
        }

        cityDataHistoryViewModel.observerCityData().observe(this) { cityData ->
            Log.i("CityActivity", "CityData: $cityData")
            cityData?.let {
                cityHistoryAdapter.differ.submitList(it)
            }
        }
    }
}