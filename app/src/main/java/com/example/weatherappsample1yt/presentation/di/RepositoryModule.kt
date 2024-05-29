package com.example.weatherappsample1yt.presentation.di

import android.content.Context
import com.example.weatherappsample1yt.data.repository.city.getAMSCityListRepositoryImpl
import com.example.weatherappsample1yt.data.repository.city.getOWCityListRepositoryImpl
import com.example.weatherappsample1yt.data.repository.city.getWACityListRepositoryImpl
import com.example.weatherappsample1yt.data.repository.preferences.dataSource.PreferenceDataSource
import com.example.weatherappsample1yt.data.repository.preferences.getPreferenceRepositoryImpl
import com.example.weatherappsample1yt.data.repository.serviceLocation.getServiceLocationRepositoryImpl
import com.example.weatherappsample1yt.data.repository.weather.getAMSWeatherRepositoryImpl
import com.example.weatherappsample1yt.data.repository.weather.getOWWeatherRepositoryImpl
import com.example.weatherappsample1yt.data.repository.weather.getWAWeatherRepositoryImpl
import com.example.weatherappsample1yt.domain.repository.CityRepository
import com.example.weatherappsample1yt.domain.repository.PreferencesRepository
import com.example.weatherappsample1yt.domain.repository.ServiceLocationRepository
import com.example.weatherappsample1yt.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Singleton
    @Provides
    @Named("WAWeather")
    fun provideWAWeatherRepository(): WeatherRepository {
        return getWAWeatherRepositoryImpl()
    }

    @Singleton
    @Provides
    @Named("OWWeather")
    fun provideOWWeatherRepository(): WeatherRepository {
        return getOWWeatherRepositoryImpl()
    }

    @Singleton
    @Provides
    @Named("AMSWeather")
    fun provideAMSWeatherRepository(): WeatherRepository {
        return getAMSWeatherRepositoryImpl()
    }

    @Singleton
    @Provides
    @Named("WACity")
    fun provideWACityListRepository(
    ): CityRepository {
        return getWACityListRepositoryImpl()
    }

    @Singleton
    @Provides
    @Named("OWCity")
    fun provideOWCityListRepository(
    ): CityRepository {
        return getOWCityListRepositoryImpl()
    }

    @Singleton
    @Provides
    @Named("AMSCity")
    fun provideAMSCityListRepository(
    ): CityRepository {
        return getAMSCityListRepositoryImpl()
    }

    @Singleton
    @Provides
    fun providePreferencesRepository(dataSource: PreferenceDataSource): PreferencesRepository {
        return getPreferenceRepositoryImpl(dataSource)
    }

    @Singleton
    @Provides
    fun provideServiceLocationRepository(@ApplicationContext context: Context): ServiceLocationRepository {
        return getServiceLocationRepositoryImpl(context)
    }
}