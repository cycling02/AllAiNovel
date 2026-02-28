package com.cycling.core.datastore.di

import com.cycling.core.datastore.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(
        repository: UserPreferencesRepository
    ): UserPreferencesRepository = repository
}
