package com.example.weatherappsample1yt.presentation.di

import com.example.weatherappsample1yt.domain.useCase.city.CitiesListUseCase
import com.example.weatherappsample1yt.domain.useCase.preferencesUser.PreferencesUseCase
import com.example.weatherappsample1yt.presentation.view.options.ApiProviderOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class ViewModelModule {
    @Provides
    fun provideCitiesListUseCase(
        preferencesUseCase: PreferencesUseCase,
        @Named("OWCity") owCitiesListUseCase: CitiesListUseCase,
        @Named("WACity") waCitiesListUseCase: CitiesListUseCase,
        @Named("AMSCity") amsCitiesListUseCase: CitiesListUseCase
    ): CitiesListUseCase {
        val apiOptions = runBlocking {
            preferencesUseCase.getApiPreferences()
        }
        return when (apiOptions) {
            ApiProviderOptions.OPEN_WEATHER -> owCitiesListUseCase
            ApiProviderOptions.AI_METEOSOURCE -> amsCitiesListUseCase
            ApiProviderOptions.WEATHER_API -> waCitiesListUseCase
            else -> throw IllegalArgumentException("Invalid API provider")
        }
    }
}