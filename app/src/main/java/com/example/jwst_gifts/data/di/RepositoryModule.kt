package com.example.jwst_gifts.data.di

import com.example.jwst_gifts.data.network.service.JWSTService
import com.example.jwst_gifts.data.repository.SpaceProgramRepository
import com.example.jwst_gifts.domain.repository.SpaceProgramRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLaunchRepository(service: JWSTService): SpaceProgramRepository =
        SpaceProgramRepositoryImpl(spaceProgramService = service)
}