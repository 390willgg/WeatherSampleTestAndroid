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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherappsample1yt.R
import com.example.weatherappsample1yt.data.model.format.CurrentWeatherData
import com.example.weatherappsample1yt.data.model.format.ForecastWeatherData
import com.example.weatherappsample1yt.databinding.ActivityMainBinding
import com.example.weatherappsample1yt.domain.useCase.weather.WeatherUseCase
import com.example.weatherappsample1yt.presentation.view.city.CityActivity
import com.example.weatherappsample1yt.presentation.view.main.ForeCastAdapter.Companion.iconKey
import com.example.weatherappsample1yt.presentation.view.main.ForeCastAdapter.Companion.iconMap
import com.example.weatherappsample1yt.presentation.view.options.TemperatureUnitOptions
import com.example.weatherappsample1yt.presentation.view.serviceLocation.ServiceLocationViewModel
import com.example.weatherappsample1yt.presentation.view.serviceLocation.ServiceLocationViewModelFactory
import com.example.weatherappsample1yt.presentation.view.settings.SettingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var serviceLocationFactory: ServiceLocationViewModelFactory

    @Inject
    lateinit var weatherViewModelFactory: WeatherViewModel.Factory

    @Inject
    lateinit var weatherUseCase: Flow<@JvmSuppressWildcards WeatherUseCase>

    private val weatherViewModel: WeatherViewModel by viewModels {
        WeatherViewModel.provideFactory(weatherViewModelFactory, weatherUseCase)
    }

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val foreCastAdapter: ForeCastAdapter = ForeCastAdapter()
    private var forecastItemDayAdapter: ForecastItemDayAdapter = ForecastItemDayAdapter()

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

        checkAndRequestPermissions()
        observeViewModel()
        setUpRecyclerView()
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

    private fun setUpRecyclerView() {
        binding.forecastView.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = foreCastAdapter
        }

        binding.forecastView2.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = forecastItemDayAdapter
        }
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

    private fun observeViewModel() {
        lifecycleScope.launch {
            serviceLocationViewModel.location.observe(this@MainActivity) { location ->
                location?.let {
                    val latitude = intent.getDoubleExtra("lat", it.latitude)
                    val longitude = intent.getDoubleExtra("lon", it.longitude)
                    fetchWeatherData(latitude, longitude)
                    binding.cityText.text = intent.getStringExtra("name") ?: ""
                } ?: Log.i("Location", "Error fetching location")
            }

            weatherViewModel.currentWeather.observe(this@MainActivity) { response ->
                response?.let { updateCurrentWeatherUI(it) }
            }

            weatherViewModel.forecastWeather.observe(this@MainActivity) { forecastResponse ->
                forecastResponse?.let { updateForecastUI(it) }
            }

            weatherViewModel.temperatureUnit.observe(this@MainActivity) { tempUnit ->
                tempUnit?.let {
                    foreCastAdapter.updateTemperatureUnitOptions(it)
                    forecastItemDayAdapter.updateTemperatureUnitOptions(it)
                }
            }
        }
    }

    private fun updateCurrentWeatherUI(response: CurrentWeatherData) {
        Log.i("response", response.toString())
        binding.apply {
            progressBar.visibility = View.GONE
            cityText.text = response.city
            statusText.text = response.weatherStatus
            detailedStatusText.text = response.weatherDescription
            currentTempTv.text = weatherViewModel.temperatureUnit.value?.let {
                response.temperature.formatTemperature(
                    it,
                )
            } ?: response.temperature.formatTemperature(TemperatureUnitOptions.Celsius)
            Log.i("temperature", "temperature : " + response.temperature.toString())
            maxTempText.text = weatherViewModel.temperatureUnit.value?.let {
                response.maxTemperature?.formatTemperature(
                    it,
                )
            } ?: response.maxTemperature?.formatTemperature(TemperatureUnitOptions.Celsius)
            minTempText.text = weatherViewModel.temperatureUnit.value?.let {
                response.minTemperature?.formatTemperature(
                    it,
                )
            } ?: response.minTemperature?.formatTemperature(TemperatureUnitOptions.Celsius)
            humidityText.text = response.humidity.toString()
            windText.text = response.windSpeed.toString()
            rainPrecipitation.text = response.precipitation.toString()

            Glide.with(this@MainActivity).load(
                iconMap[iconKey(response.icon.toString())] ?: R.drawable.sunny_day_icon
            ).placeholder(R.drawable.sunny_day_icon)
                .error(R.drawable.baseline_running_with_errors_24).into(weatherIcon)
        }
    }

    private fun updateForecastUI(forecastResponse: ForecastWeatherData) {
        forecastResponse.forecasts?.dailyDetails?.let { dailyDetails ->
            Log.i("dailyDetails", dailyDetails.toString())
            forecastItemDayAdapter.differ.submitList(dailyDetails)
            forecastItemDayAdapter.updateTemperatureUnitOptions(weatherViewModel.temperatureUnit.value)
        }
        forecastResponse.forecasts?.hourlyDetails?.let { hourlyDetails ->
            Log.i("hourlyDetails", hourlyDetails.toString())
            foreCastAdapter.differ.submitList(hourlyDetails)
            foreCastAdapter.updateTemperatureUnitOptions(weatherViewModel.temperatureUnit.value)
        }
    }

    private fun fetchWeatherData(latitude: Double, longitude: Double) {
        weatherViewModel.getCurrentWeather(latitude, longitude)
        weatherViewModel.getForecastWeather()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}