package com.example.weatherappsample1yt.presentation.view.city.componentUi

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
import com.example.weatherappsample1yt.R
import com.example.weatherappsample1yt.presentation.view.city.CityAdapter
import com.example.weatherappsample1yt.presentation.view.city.CityViewModel
import kotlinx.coroutines.launch

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CitySearchScreenPreview() {
    Scaffold {
        CitySearchScreen(
            modifier = Modifier.padding(it), null, null
        )
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