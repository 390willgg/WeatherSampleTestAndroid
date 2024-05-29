package com.example.weatherappsample1yt.presentation.di

import com.example.weatherappsample1yt.domain.useCase.serviceLocation.ServiceLocationUseCase
import com.example.weatherappsample1yt.presentation.view.serviceLocation.ServiceLocationViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object ServiceLocationModule {
    @Provides
    @ActivityScoped
    fun provideServiceLocationViewModelFactory(
        useCase : ServiceLocationUseCase,
    ): ServiceLocationViewModelFactory {
        return ServiceLocationViewModelFactory(useCase)
    }
}