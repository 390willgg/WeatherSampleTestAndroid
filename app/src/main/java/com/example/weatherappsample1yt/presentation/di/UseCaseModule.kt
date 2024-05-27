package com.example.weatherappsample1yt.presentation.di

import androidx.lifecycle.ViewModelProvider
import com.example.weatherappsample1yt.domain.repository.CityRepository
import com.example.weatherappsample1yt.domain.repository.PreferencesRepository
import com.example.weatherappsample1yt.domain.repository.ServiceLocationRepository
import com.example.weatherappsample1yt.domain.repository.WeatherRepository
import com.example.weatherappsample1yt.domain.useCase.city.CitiesListUseCase
import com.example.weatherappsample1yt.domain.useCase.city.getCitiesListUseCaseImpl
import com.example.weatherappsample1yt.domain.useCase.preferencesUser.PreferencesUseCase
import com.example.weatherappsample1yt.domain.useCase.preferencesUser.getPreferencesUseCaseImpl
import com.example.weatherappsample1yt.domain.useCase.serviceLocation.ServiceLocationUseCase
import com.example.weatherappsample1yt.domain.useCase.serviceLocation.getServiceLocationUseCaseImpl
import com.example.weatherappsample1yt.domain.useCase.weather.WeatherUseCase
import com.example.weatherappsample1yt.domain.useCase.weather.getWeatherUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Singleton
    @Provides
    @Named("WAWeather")
    fun provideWAWeatherUseCase(@Named("WAWeather") repository: WeatherRepository): WeatherUseCase =
        getWeatherUseCaseImpl(repository)

    @Singleton
    @Provides
    @Named("OWWeather")
    fun provideOWWeatherUseCase(@Named("OWWeather") repository: WeatherRepository): WeatherUseCase =
        getWeatherUseCaseImpl(repository)

    @Singleton
    @Provides
    @Named("AMSWeather")
    fun provideAMSWeatherUseCase(@Named("AMSWeather") repository: WeatherRepository): WeatherUseCase =
        getWeatherUseCaseImpl(repository)

    @Singleton
    @Provides
    @Named("AMSCity")
    fun provideAMSCityListUseCase(@Named("AMSCity") repository: CityRepository): CitiesListUseCase =
        getCitiesListUseCaseImpl(repository)

    @Singleton
    @Provides
    @Named("OWCity")
    fun provideOWCityListUseCase(@Named("OWCity") repository: CityRepository): CitiesListUseCase =
        getCitiesListUseCaseImpl(repository)

    @Singleton
    @Provides
    @Named("WACity")
    fun provideAWCityListUseCase(@Named("WACity") repository: CityRepository): CitiesListUseCase =
        getCitiesListUseCaseImpl(repository)

    @Singleton
    @Provides
    fun providePreferenceUseCase(repository: PreferencesRepository): PreferencesUseCase =
        getPreferencesUseCaseImpl(repository)

    @Singleton
    @Provides
    fun provideServiceLocationUseCase(repository: ServiceLocationRepository): ServiceLocationUseCase =
        getServiceLocationUseCaseImpl(repository)
}