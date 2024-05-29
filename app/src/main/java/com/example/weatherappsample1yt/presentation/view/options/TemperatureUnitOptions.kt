package com.example.weatherappsample1yt.presentation.view.options

enum class TemperatureUnitOptions {
    Celsius, Fahrenheit;

    override fun toString(): String {
        return when (this) {
            Celsius -> "Celsius"
            Fahrenheit -> "Fahrenheit"
        }
    }
}