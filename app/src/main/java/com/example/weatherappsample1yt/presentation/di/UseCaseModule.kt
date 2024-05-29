package com.example.weatherappsample1yt.presentation.di

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
    @Provides
    @Singleton
    @Named("WAWeather")
    fun provideWAWeatherUseCase(@Named("WAWeather") repository: WeatherRepository): WeatherUseCase =
        getWeatherUseCaseImpl(repository)

    @Provides
    @Singleton
    @Named("OWWeather")
    fun provideOWWeatherUseCase(@Named("OWWeather") repository: WeatherRepository): WeatherUseCase =
        getWeatherUseCaseImpl(repository)

    @Provides
    @Singleton
    @Named("AMSWeather")
    fun provideAMSWeatherUseCase(@Named("AMSWeather") repository: WeatherRepository): WeatherUseCase =
        getWeatherUseCaseImpl(repository)

    @Provides
    @Singleton
    @Named("AMSCity")
    fun provideAMSCityListUseCase(@Named("AMSCity") repository: CityRepository): CitiesListUseCase =
        getCitiesListUseCaseImpl(repository)

    @Provides
    @Singleton
    @Named("OWCity")
    fun provideOWCityListUseCase(@Named("OWCity") repository: CityRepository): CitiesListUseCase =
        getCitiesListUseCaseImpl(repository)

    @Provides
    @Singleton
    @Named("WACity")
    fun provideAWCityListUseCase(@Named("WACity") repository: CityRepository): CitiesListUseCase =
        getCitiesListUseCaseImpl(repository)

    @Provides
    @Singleton
    fun providePreferenceUseCase(repository: PreferencesRepository): PreferencesUseCase =
        getPreferencesUseCaseImpl(repository)

    @Provides
    @Singleton
    fun provideServiceLocationUseCase(repository: ServiceLocationRepository): ServiceLocationUseCase =
        getServiceLocationUseCaseImpl(repository)
}