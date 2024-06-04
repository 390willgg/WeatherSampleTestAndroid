package com.example.weatherappsample1yt.presentation.view.main.componentUi

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherappsample1yt.R

@Preview(showBackground = true)
@Composable
fun WeatherDetailCardPreview() {
    WeatherDetailCard(
        modifier = Modifier,
        icon = painterResource(id = R.drawable.precipitation_rain_icon),
        title = "0.0 ~ 1 hour",
        description = "Precipitation Rain"
    )
}

@Composable
fun WeatherDetailCard(
    modifier: Modifier,
    icon: Painter,
    title: String?,
    description: String
) {
    val titleState = remember { mutableStateOf(title) }
    Card(
        modifier = modifier
            .padding(8.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .height(140.dp)
                .background(Color.White.copy(alpha = 0.8f))
                .padding(8.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
                    .align(Alignment.CenterHorizontally)
                    .weight(0.6f),
                painter = icon, // Use the icon parameter here
                contentDescription = title, // Use the title parameter here as content description
                alignment = Alignment.Center,
                contentScale = ContentScale.Fit
            )
            Text(
                text = titleState.value ?: "NAN",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f)
                    .padding(top = 8.dp)
            )
            Text(
                text = description,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f)
            )
        }
    }
    LaunchedEffect(title) {
        titleState.value = title
    }
}