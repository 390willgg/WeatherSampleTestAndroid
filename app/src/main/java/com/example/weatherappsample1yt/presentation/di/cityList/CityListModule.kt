package com.example.weatherappsample1yt.presentation.di.cityList

import com.example.weatherappsample1yt.domain.useCase.city.CitiesListUseCase
import com.example.weatherappsample1yt.presentation.view.city.CityViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class CityModule {
    @Provides
    @ActivityScoped
    fun provideCityListViewModelFactory(
        useCase: CitiesListUseCase
    ): CityViewModelFactory {
        return CityViewModelFactory(useCase)
    }
}