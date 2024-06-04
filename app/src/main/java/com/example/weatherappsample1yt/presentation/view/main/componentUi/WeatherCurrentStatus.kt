package com.example.weatherappsample1yt.presentation.view.main.componentUi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun WeatherCurrentStatusPreview() {
    WeatherCurrentStatus(
        modifier = Modifier,
        topTitle = "Raining",
        bottomTitle = "Raining More"
    )
}

@Composable
fun WeatherCurrentStatus(
    modifier: Modifier, topTitle: String?, bottomTitle: String?
) {
    val topTitleState = remember { mutableStateOf(topTitle) }
    val bottomTitleState = remember { mutableStateOf(bottomTitle) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = topTitleState.value ?: "Raining", style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = bottomTitleState.value ?: "Raining More",
            style = MaterialTheme.typography.bodySmall
        )
    }
    LaunchedEffect(topTitle) {
        topTitleState.value = topTitle
    }
    LaunchedEffect(bottomTitle) {
        bottomTitleState.value = bottomTitle
    }
}
