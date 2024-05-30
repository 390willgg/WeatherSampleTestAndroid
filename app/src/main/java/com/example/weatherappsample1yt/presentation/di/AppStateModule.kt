package com.example.weatherappsample1yt.presentation.di

import com.example.weatherappsample1yt.domain.useCase.preferencesUser.PreferencesUseCase
import com.example.weatherappsample1yt.presentation.AppState
import com.example.weatherappsample1yt.presentation.view.options.TemperatureUnitOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppStateModule {

    @Provides
    @Singleton
    fun provideAppState(
        preferencesUseCase: PreferencesUseCase,
    ): StateFlow<AppState> {
        val appState =
            MutableStateFlow(AppState(temperatureUnitOptions = TemperatureUnitOptions.Celsius))
        preferencesUseCase.bindAppStateFlow(appState)
        return appState
    }
}