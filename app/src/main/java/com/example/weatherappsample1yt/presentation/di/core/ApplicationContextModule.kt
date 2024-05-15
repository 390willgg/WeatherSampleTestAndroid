package com.example.weatherappsample1yt.presentation.di.core

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationContextModule {
	@Singleton
	@Provides
	fun provideApplicationContext(@ApplicationContext context : Context) : Context {
		return context
	}
}