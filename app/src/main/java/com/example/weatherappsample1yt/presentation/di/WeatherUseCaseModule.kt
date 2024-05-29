package com.example.weatherappsample1yt.presentation.di

import android.util.Log
import com.example.weatherappsample1yt.domain.useCase.preferencesUser.PreferencesUseCase
import com.example.weatherappsample1yt.domain.useCase.weather.WeatherUseCase
import com.example.weatherappsample1yt.presentation.view.options.ApiProviderOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherUseCaseModule {
    @Provides
    @Singleton
    @JvmSuppressWildcards
    fun provideWeatherUseCaseFlow(
        preferencesUseCase: PreferencesUseCase,
        @Named("OWWeather") owWeatherUseCase: WeatherUseCase,
        @Named("WAWeather") waWeatherUseCase: WeatherUseCase,
        @Named("AMSWeather") amsWeatherUseCase: WeatherUseCase
    ): Flow<WeatherUseCase> {
        return preferencesUseCase.observeApiPreferences().mapNotNull { apiOption ->
            Log.i("Api Options", apiOption.toString())
            when (apiOption) {
                ApiProviderOptions.OPEN_WEATHER -> owWeatherUseCase
                ApiProviderOptions.AI_METEOSOURCE -> amsWeatherUseCase
                ApiProviderOptions.WEATHER_API -> waWeatherUseCase
                else -> throw IllegalArgumentException("Invalid API provider")
            }
        }
    }
}