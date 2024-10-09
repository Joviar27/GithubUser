package com.example.githubuserapp.favorite.di

import android.content.Context
import com.example.core.domain.usecase.BookmarkedUseCase
import com.example.core.domain.usecase.ThemeUseCase
import com.example.githubuser.di.FavoriteModuleDependencies
import com.example.githubuserapp.favorite.ui.FavouriteFragment
import dagger.BindsInstance
import dagger.Component

@Component(dependencies = [FavoriteModuleDependencies::class])
interface FavoriteComponent {

    fun inject(fragment: FavouriteFragment)

    fun provideThemeUseCase(themeUseCase: ThemeUseCase)
    fun provideBookmarkedUseCase(bookmarkedUseCase: BookmarkedUseCase)

    @Component.Builder
    interface Builder{
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(favoriteModuleDependencies: FavoriteModuleDependencies): Builder
        fun build(): FavoriteComponent
    }
}