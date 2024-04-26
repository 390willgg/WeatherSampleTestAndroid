package com.example.weatherappsample1yt.presentation.di.weather

import com.example.weatherappsample1yt.domain.useCase.weather.WeatherUseCase
import com.example.weatherappsample1yt.presentation.view.main.WeatherViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class WeatherModule {
    @ActivityScoped
    @Provides
    fun provideWeatherViewModelFactory(weatherUseCase: WeatherUseCase): WeatherViewModelFactory {
        return WeatherViewModelFactory(weatherUseCase)
    }
}