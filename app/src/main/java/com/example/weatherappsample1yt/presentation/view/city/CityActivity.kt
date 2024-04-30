package com.example.weatherappsample1yt.presentation.view.city

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherappsample1yt.data.model.format.DataItemCity
import com.example.weatherappsample1yt.databinding.ActivityCityBinding
import com.example.weatherappsample1yt.presentation.view.main.ApiProviderOptions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CityActivity : AppCompatActivity() {
    @Inject
    lateinit var citiesListViewModelFactory: CityViewModel.Factory
    private val citiesListViewModel: CityViewModel by viewModels {
        CityViewModel.provideFactory(
            citiesListViewModelFactory,
            ApiProviderOptions.OPEN_WEATHER
        )
    }
    private lateinit var binding: ActivityCityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        citiesListViewModel.getCitiesList("London", 3)
        citiesListViewModel.citiesList.observe(this) { cityWeatherData ->
            cityWeatherData?.let {
                val cities: ArrayList<DataItemCity> = it.data
                cities.forEach { city ->
                    println(city.cityName)
                }
            }
        }
    }
//    private lateinit var binding: ActivityCityBinding
//    private lateinit var cityViewModel: CityViewModel
//    private val cityAdapter by lazy { CityAdapter() }
//
//    companion object {
//        private const val TAG = "CityActivity"
//    }
//
//    private fun dpToPx(dp: Int, context: Context): Int {
//        return (dp * context.resources.displayMetrics.density).toInt()
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        binding = ActivityCityBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val cityRepository = getOWCityRepository()
//        val citiesListUseCase = CitiesListRemoteDataSourceImpl(repository = cityRepository)
//        cityViewModel = CityViewModel(citiesListUseCase)
//
//        window.apply {
//            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            statusBarColor = Color.TRANSPARENT
//        }
//
//        binding.apply {
//            cityEditText.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(
//                    s: CharSequence?, start: Int, count: Int, after: Int
//                ) {
//                }
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//                override fun afterTextChanged(s: android.text.Editable?) {
//                    progressBar2.visibility = View.VISIBLE
//                    CoroutineScope(Dispatchers.Main).launch {
////                        cityViewModel.loadCity(s.toString(), 10, { cityResponseApi ->
////                            // Handle successful response
////                            progressBar2.visibility = View.GONE
////                            cityAdapter.differ.submitList(cityResponseApi)
////                            cityView.apply {
////                                layoutManager = LinearLayoutManager(
////                                    this@CityActivity, LinearLayoutManager.HORIZONTAL, false
////                                )
////                                adapter = cityAdapter
////                            }
////                        }, { throwable ->
////                            // Handle error
////                            progressBar2.visibility = View.GONE
////                            // Log the error
////                            Log.e(TAG, "Error loading city data", throwable)
////                            // Show a toast with the error message
////                            Toast.makeText(this@CityActivity, throwable.message, Toast.LENGTH_LONG)
////                                .show()
////                            // Or show a dialog with the error message
////                            AlertDialog.Builder(this@CityActivity).setTitle("Error")
////                                .setMessage(throwable.message).setPositiveButton("OK", null).show()
////                        })
//                        cityViewModel.citiesList.observe(this@CityActivity) {
//                            progressBar2.visibility = View.GONE
//                            cityAdapter.differ.submitList(it.data)
//                            cityView.apply {
//                                layoutManager = LinearLayoutManager(
//                                    this@CityActivity, LinearLayoutManager.HORIZONTAL, false
//                                )
//                                adapter = cityAdapter
//                            }
//                        }
//                    }
//                }
//            })
//        }
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            val additionalPadding = dpToPx(16, v.context)
//            v.setPadding(
//                systemBars.left + additionalPadding,
//                systemBars.top + additionalPadding,
//                systemBars.right + additionalPadding,
//                systemBars.bottom + additionalPadding
//            )
//            insets
//        }
//    }
}
