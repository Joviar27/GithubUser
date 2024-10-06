package com.example.githubuser.di

import com.example.core.domain.interactor.BookmarkedInteractor
import com.example.core.domain.interactor.ThemeInteractor
import com.example.core.domain.interactor.UserInteractor
import com.example.core.domain.usecase.BookmarkedUseCase
import com.example.core.domain.usecase.ThemeUseCase
import com.example.core.domain.usecase.UserUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {

    @Binds
    @ViewModelScoped
    abstract fun provideBookmarkedUseCase(bookmarkedInteractor: BookmarkedInteractor): BookmarkedUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideThemeUseCase(themeInteractor: ThemeInteractor): ThemeUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideUserUseCase(userInteractor: UserInteractor): UserUseCase
}