package com.example.weatherappsample1yt.presentation.view.options

enum class ApiProviderOptions {
    OPEN_WEATHER, AI_METEOSOURCE, WEATHER_API;

    override fun toString(): String {
        return when (this) {
            OPEN_WEATHER -> "OPEN_WEATHER"
            AI_METEOSOURCE -> "AI_METEOSOURCE"
            WEATHER_API -> "WEATHER_API"
        }
    }
}