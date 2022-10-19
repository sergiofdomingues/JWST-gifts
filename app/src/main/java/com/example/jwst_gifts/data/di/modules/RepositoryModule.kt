package com.example.jwst_gifts.data.di.modules

import com.example.jwst_gifts.data.repository.SpaceProgramRepositoryImpl
import com.example.jwst_gifts.domain.repository.SpaceProgramRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLaunchRepository(spaceProgramRepositoryImpl: SpaceProgramRepositoryImpl): SpaceProgramRepository
}