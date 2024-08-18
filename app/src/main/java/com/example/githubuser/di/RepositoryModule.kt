package com.example.githubuser.di

import com.example.githubuser.data.repository.BookmarkedRepository
import com.example.githubuser.data.repository.ThemeRepository
import com.example.githubuser.data.repository.UserRepository
import com.example.githubuser.domain.repository.IBookmarkedRepository
import com.example.githubuser.domain.repository.IThemeRepository
import com.example.githubuser.domain.repository.IUserRepository
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