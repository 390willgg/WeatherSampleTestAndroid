package com.example.weatherappsample1yt.presentation.di

import android.content.Context
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.weatherappsample1yt.data.repository.city.dataSource.CityListRemoteDataSource
import com.example.weatherappsample1yt.data.repository.city.dataSourceImpl.getAMSCityListRemoteDataSourceImpl
import com.example.weatherappsample1yt.data.repository.city.dataSourceImpl.getOWCityListRemoteDataSourceImpl
import com.example.weatherappsample1yt.data.repository.city.dataSourceImpl.getWACityListRemoteDataSourceImpl
import com.example.weatherappsample1yt.data.repository.preferences.dataSource.PreferenceDataSource
import com.example.weatherappsample1yt.data.repository.preferences.dataStore.getDataStoreDataSourceImpl
import com.example.weatherappsample1yt.data.repository.preferences.sharePrefereces.getSharedPreferencesDataSourceImpl
import com.example.weatherappsample1yt.data.repository.serviceLocation.dataSource.ServiceLocationDataSource
import com.example.weatherappsample1yt.data.repository.serviceLocation.dataSourceImpl.getServiceLocationDataSourceImpl
import com.example.weatherappsample1yt.data.repository.weather.dataSource.WeatherRemoteDataSource
import com.example.weatherappsample1yt.data.repository.weather.dataSourceImpl.getAMSWeatherRemoteDataSourceImpl
import com.example.weatherappsample1yt.data.repository.weather.dataSourceImpl.getOWWeatherRemoteDataSourceImpl
import com.example.weatherappsample1yt.data.repository.weather.dataSourceImpl.getWAWeatherRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Qualifier
import javax.inject.Singleton

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DataStorePreference

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SharedPreferencesPreference

@Module
@InstallIn(SingletonComponent::class)
class RemoteDataModule {
    @Singleton
    @Provides
    fun provideOWWeatherRemoteDataSource(
        client: OkHttpClient
    ): WeatherRemoteDataSource {
        return getOWWeatherRemoteDataSourceImpl(client)
    }

    @Singleton
    @Provides
    fun provideWAWeatherRemoteDataSource(
        client: OkHttpClient
    ): WeatherRemoteDataSource {
        return getWAWeatherRemoteDataSourceImpl(client)
    }

    @Singleton
    @Provides
    fun provideAMSWeatherRemoteDataSource(
        client: OkHttpClient
    ): WeatherRemoteDataSource {
        return getAMSWeatherRemoteDataSourceImpl(client)
    }

    @Singleton
    @Provides
    fun provideOWCityListRemoteDataSource(
        client: OkHttpClient
    ): CityListRemoteDataSource {
        return getOWCityListRemoteDataSourceImpl(client)
    }

    @Singleton
    @Provides
    fun provideWACityListRemoteDataSource(
        client: OkHttpClient
    ): CityListRemoteDataSource {
        return getWACityListRemoteDataSourceImpl( client)
    }

    @Singleton
    @Provides
    fun provideAMSCityListRemoteDataSource(
        client: OkHttpClient
    ): CityListRemoteDataSource {
        return getAMSCityListRemoteDataSourceImpl(client)
    }
    
    @Singleton
    @Provides
    fun provideServiceLocationDataSource(@ApplicationContext context : Context) : ServiceLocationDataSource {
        return getServiceLocationDataSourceImpl(context)
    }
    
    @Singleton
    @Provides
    @SharedPreferencesPreference
    fun provideSharePreferencesDataSource(@ApplicationContext context : Context) : PreferenceDataSource =
        getSharedPreferencesDataSourceImpl(context)
    
    @Singleton
    @Provides
    @DataStorePreference
    fun provideDataStoreDataSource(dataStore : DataStore<Preferences>) : PreferenceDataSource =
        getDataStoreDataSourceImpl(dataStore)
    
    @Singleton
    @Provides
    fun providePreferenceDataSource(context : Context) : PreferenceDataSource =
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            getSharedPreferencesDataSourceImpl(context)
        } else {
            getDataStoreDataSourceImpl(dataStore = context.dataStore)
        }
}