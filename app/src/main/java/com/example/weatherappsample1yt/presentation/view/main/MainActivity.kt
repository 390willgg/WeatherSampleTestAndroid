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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherappsample1yt.R
import com.example.weatherappsample1yt.data.model.format.CurrentWeatherData
import com.example.weatherappsample1yt.data.model.format.ForecastWeatherData
import com.example.weatherappsample1yt.databinding.ActivityMainBinding
import com.example.weatherappsample1yt.domain.useCase.weather.WeatherUseCase
import com.example.weatherappsample1yt.presentation.view.city.CityActivity
import com.example.weatherappsample1yt.presentation.view.main.ForeCastAdapter.Companion.iconKey
import com.example.weatherappsample1yt.presentation.view.main.ForeCastAdapter.Companion.iconMap
import com.example.weatherappsample1yt.presentation.view.main.componentUi.CurrentTemperatureComponentInformation
import com.example.weatherappsample1yt.presentation.view.main.componentUi.WeatherCurrentStatus
import com.example.weatherappsample1yt.presentation.view.main.componentUi.WeatherDetailCard
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
        binding.apply {
            progressBar.visibility = View.GONE
            cityText.text = response.city
            detailLayout.setContent {
                WeatherScreen(
                    modifier = Modifier, response = response, weatherViewModel = weatherViewModel
                )
            }
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

@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    response: CurrentWeatherData?,
    weatherViewModel: WeatherViewModel?
) {
    val responseState = remember { mutableStateOf(response) }
    val weatherViewModelState = remember { mutableStateOf(weatherViewModel) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        WeatherCurrentStatus(
            modifier = Modifier,
            topTitle = responseState.value?.weatherStatus,
            bottomTitle = responseState.value?.weatherDescription
        )

        CurrentTemperatureComponentInformation(modifier = Modifier,
            image = iconMap[iconKey(responseState.value?.icon.toString())]
                ?: R.drawable.sunny_day_icon,
            downTemp = weatherViewModelState.value?.temperatureUnit?.value?.let {
                responseState.value?.minTemperature?.formatTemperature(
                    it,
                )
            }
                ?: responseState.value?.minTemperature?.formatTemperature(TemperatureUnitOptions.Celsius),
            currentTemperature = weatherViewModelState.value?.temperatureUnit?.value.let {
                it?.let { it1 ->
                    responseState.value?.temperature?.formatTemperature(
                        it1,
                    )
                }
                    ?: responseState.value?.temperature?.formatTemperature(TemperatureUnitOptions.Celsius)
            },
            upTemp = weatherViewModelState.value?.temperatureUnit?.value?.let {
                responseState.value?.maxTemperature?.formatTemperature(
                    it,
                )
            }
                ?: responseState.value?.maxTemperature?.formatTemperature(TemperatureUnitOptions.Celsius))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            WeatherDetailCard(
                modifier = modifier.weight(0.333f),
                title = "${response?.precipitation} ~ 1 hour",
                icon = painterResource(id = R.drawable.precipitation_rain_icon),
                description = "Precipitation Rain"
            )
            WeatherDetailCard(
                modifier = modifier.weight(0.333f),
                title = "${response?.humidity} %",
                icon = painterResource(id = R.drawable.humidity_icon),
                description = "Humidity"
            )
            WeatherDetailCard(
                modifier = modifier.weight(0.333f),
                title = "${response?.windSpeed} m/s",
                icon = painterResource(id = R.drawable.wind_icon),
                description = "Wind Speed"
            )
        }
    }
    LaunchedEffect(response) {
        responseState.value = response
    }
    LaunchedEffect(weatherViewModel) {
        weatherViewModelState.value = weatherViewModel
    }
}