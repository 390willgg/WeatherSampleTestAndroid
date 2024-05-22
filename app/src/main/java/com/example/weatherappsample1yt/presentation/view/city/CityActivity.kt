package com.example.weatherappsample1yt.presentation.view.city

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherappsample1yt.R
import com.example.weatherappsample1yt.data.model.format.DataItemCity
import com.example.weatherappsample1yt.databinding.ActivityCityBinding
import com.example.weatherappsample1yt.domain.useCase.preferencesUser.PreferencesUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CitySearchScreenPreview() {
    Scaffold {
        CitySearchScreen(
            modifier = Modifier.padding(it), null, null
        )
    }
}

@AndroidEntryPoint
class CityActivity : AppCompatActivity() {
    @Inject
    lateinit var preferencesUseCase: PreferencesUseCase

    private val cityAdapter by lazy { CityAdapter() }
    private lateinit var binding: ActivityCityBinding
    private fun dpToPx(context: Context): Int {
        return (16 * context.resources.displayMetrics.density).toInt()
    }

    private val citiesListViewModel: CityViewModel by viewModels()

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
                val cities: ArrayList<DataItemCity> = it.data
                cities.forEach { city ->
                    println(city.cityName)
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.cityView.apply {
            layoutManager = LinearLayoutManager(
                this@CityActivity, LinearLayoutManager.HORIZONTAL, false
            )
            adapter = cityAdapter
        }
    }

    private fun observeViewModel() {
        citiesListViewModel.citiesList.observe(this) { cityWeatherData ->
            cityWeatherData?.let {
                cityAdapter.differ.submitList(it.data)
            }
        }
    }
}

@Composable
fun CitySearchScreen(
    modifier: Modifier = Modifier,
    cityViewModel: CityViewModel?,
    cityAdapter: CityAdapter?,
) {
    val coroutineScope = rememberCoroutineScope()
    var searchText by remember {
        mutableStateOf("")
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.search_text_header),
            modifier = modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.headlineSmall
        )
        OutlinedTextField(value = searchText, onValueChange = {
            searchText = it
        }, label = {
            Text(
                text = stringResource(
                    id = R.string.hintTxtCity
                )
            )
        }, leadingIcon = {
            Icon(
                Icons.Filled.Search,
                contentDescription = null,
            )
        }, singleLine = true, modifier = modifier.fillMaxWidth()
        )
    }

    LaunchedEffect(searchText) {
        coroutineScope.launch {
            if (searchText.isNotEmpty()) {
                isLoading = true
                try {
                    cityViewModel?.getCitiesList(searchText, 10)
                    cityViewModel?.citiesList?.observeForever {
                        isLoading = false
                        cityAdapter?.differ?.submitList(it?.data)
                    }
                } catch (e: Exception) {
                    isLoading = false
                    e.printStackTrace()
                }
            }
        }
    }
}