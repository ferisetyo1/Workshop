package com.feri.workshop.di

import android.content.Context
import com.feri.workshop.repository.WorkshopRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun provideWorkshopRepository(
        @ApplicationContext appContext: Context,
    ) = WorkshopRepository(appContext)
}