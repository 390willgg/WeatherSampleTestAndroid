package com.example.weatherappsample1yt.presentation.view.main.componentUi

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherappsample1yt.R

@Preview(showBackground = true)
@Composable
fun CurrentTemperatureComponentInformationPreview() {
    CurrentTemperatureComponentInformation(
        modifier = Modifier,
        image = R.drawable.sunny_icon,
        upTemp = "30°C",
        currentTemperature = "28°C",
        downTemp = "25°C"
    )
}

@Composable
fun CurrentTemperatureComponentInformation(
    modifier: Modifier = Modifier,
    image: Int?,
    currentTemperature: String?,
    upTemp: String?,
    downTemp: String?
) {
    val imageState = remember { image?.let { mutableIntStateOf(it) } }
    val currentTempState = remember { mutableStateOf(currentTemperature) }
    val upTempState = remember { mutableStateOf(upTemp) }
    val downTempState = remember { mutableStateOf(downTemp) }

    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = imageState?.intValue ?: R.drawable.sunny_icon),
            contentDescription = "Weather Icon",
            modifier = modifier
                .weight(1f)
                .sizeIn(minWidth = 75.dp, maxWidth = 150.dp, minHeight = 75.dp, maxHeight = 150.dp)
                .align(Alignment.CenterVertically),
            contentScale = ContentScale.Fit
        )
        Column(
            modifier = modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = currentTempState.value ?: "28°C",
                fontSize = 64.sp,
                textAlign = TextAlign.Center,
                modifier = modifier.fillMaxWidth()
            )

            Row(
                modifier = modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.KeyboardArrowUp,
                    contentDescription = "Max Temp"
                )
                upTempState.value?.let {
                    Text(
                        text = it,
                        modifier = modifier.align(
                            Alignment.CenterVertically
                        ),
                    )
                }
                Spacer(
                    modifier = Modifier.width(16.dp)
                )
                downTempState.value?.let {
                    Text(
                        text = it,
                        modifier = modifier
                            .align(
                                Alignment.CenterVertically
                            ),
                    )
                }
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = "Min Temp"
                )
            }
        }
    }

    LaunchedEffect(currentTemperature) {
        currentTempState.value = currentTemperature
    }

    LaunchedEffect(image) {
        imageState?.intValue = image ?: R.drawable.sunny_icon
    }

    LaunchedEffect(upTemp) {
        upTempState.value = upTemp
    }

    LaunchedEffect(downTemp) {
        downTempState.value = downTemp
    }
}