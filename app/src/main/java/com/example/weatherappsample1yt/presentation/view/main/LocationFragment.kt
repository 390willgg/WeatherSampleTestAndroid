package com.example.weatherappsample1yt.presentation.view.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherappsample1yt.R
import com.example.weatherappsample1yt.data.model.format.CurrentWeatherData
import com.example.weatherappsample1yt.data.model.format.ForecastWeatherData
import com.example.weatherappsample1yt.databinding.FragmentLocationBinding
import com.example.weatherappsample1yt.domain.useCase.weather.WeatherUseCase
import com.example.weatherappsample1yt.presentation.view.main.adapter.ForeCastAdapter
import com.example.weatherappsample1yt.presentation.view.main.adapter.ForeCastAdapter.Companion.iconKey
import com.example.weatherappsample1yt.presentation.view.main.adapter.ForeCastAdapter.Companion.iconMap
import com.example.weatherappsample1yt.presentation.view.main.adapter.ForecastItemDayAdapter
import com.example.weatherappsample1yt.presentation.view.main.componentUi.CurrentTemperatureComponentInformation
import com.example.weatherappsample1yt.presentation.view.main.componentUi.WeatherCurrentStatus
import com.example.weatherappsample1yt.presentation.view.main.componentUi.WeatherDetailCard
import com.example.weatherappsample1yt.presentation.view.options.TemperatureUnitOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LocationFragment : Fragment() {
    @Inject
    lateinit var weatherViewModelFactory: WeatherViewModel.Factory

    @Inject
    lateinit var weatherUseCase: Flow<@JvmSuppressWildcards WeatherUseCase>

    private lateinit var weatherViewModel: WeatherViewModel

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    private val foreCastAdapter: ForeCastAdapter = ForeCastAdapter()
    private var forecastItemDayAdapter: ForecastItemDayAdapter = ForecastItemDayAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("LocationFragment", "onViewCreated")
        weatherViewModel = ViewModelProvider(
            this,
            WeatherViewModel.provideFactory(weatherViewModelFactory, weatherUseCase)
        )[WeatherViewModel::class.java]

        setUpRecyclerView()
        observeViewModel()
    }

    private fun setUpRecyclerView() {
        binding.forecastView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = foreCastAdapter
        }

        binding.forecastView2.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = forecastItemDayAdapter
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            arguments?.let {
                val latitude = it.getDouble("latData")
                val longitude = it.getDouble("lonData")
                val cityName = it.getString("nameData")
                Log.i("LocationFragment", "Latitude: $latitude, Longitude: $longitude")
                Log.i("LocationFragment", "CityName: $cityName")
                binding.cityText.text = cityName
                weatherViewModel.setLocation(latitude, longitude)
            }

            Log.i("LocationFragment", "observeViewModel")
            weatherViewModel.currentWeather.observe(viewLifecycleOwner) { response ->
                Log.i("LocationFragment", "currentWeather: $response")
                response?.let { currentWeatherData ->
                    updateCurrentWeatherUI(currentWeatherData)
                }
            }

            weatherViewModel.forecastWeather.observe(viewLifecycleOwner) { forecastResponse ->
                forecastResponse?.let { updateForecastUI(it) }
            }

            weatherViewModel.temperatureUnit.observe(viewLifecycleOwner) { tempUnit ->
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
                    } ?: responseState.value?.temperature?.formatTemperature(
                        TemperatureUnitOptions.Celsius
                    )
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

    companion object {
        fun newInstance(
            latitude: Double?, longitude: Double?, cityName: String?
        ): LocationFragment {
            val fragment = LocationFragment()
            val args = Bundle().apply {
                latitude?.let { putDouble("latData", it) }
                Log.i("LocationFragment", "Latitude: $latitude")
                longitude?.let { putDouble("lonData", it) }
                cityName?.let { putString("nameData", it) }
            }
            fragment.arguments = args
            return fragment
        }
    }
}