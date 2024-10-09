package com.example.githubuser.di

import com.example.core.domain.usecase.BookmarkedUseCase
import com.example.core.domain.usecase.ThemeUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FavoriteModuleDependencies {

    fun themeUseCase(): ThemeUseCase
    fun bookmarkedUseCase(): BookmarkedUseCase
}