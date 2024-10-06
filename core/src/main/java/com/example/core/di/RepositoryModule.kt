package com.example.core.di

import com.example.core.data.repository.BookmarkedRepository
import com.example.core.data.repository.ThemeRepository
import com.example.core.data.repository.UserRepository
import com.example.core.domain.repository.IBookmarkedRepository
import com.example.core.domain.repository.IThemeRepository
import com.example.core.domain.repository.IUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class, DatabaseModule::class, PreferenceModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideUserRepository(userRepository: UserRepository): IUserRepository

    @Binds
    abstract fun provideBookmarkedRepository(bookmarkedRepository: BookmarkedRepository): IBookmarkedRepository

    @Binds
    abstract fun provideThemeRepository(themeRepository: ThemeRepository): IThemeRepository
}