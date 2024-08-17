package com.example.githubuser.di

import android.content.Context
import com.example.githubuser.data.local.preference.ThemePreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {

    @Singleton
    @Provides
    fun provideThemePreference(@ApplicationContext context: Context) : ThemePreference{
        return ThemePreference(context)
    }
}