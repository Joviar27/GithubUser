package com.example.githubuser.di

import android.content.Context
import com.example.githubuser.data.UserRepository
import com.example.githubuser.data.local.preference.ThemePreference
import com.example.githubuser.data.local.room.UserDatabase
import com.example.githubuser.data.remote.retrofit.ApiConfig

object Injection {

    private fun provideUserPreference(context: Context) =
        ThemePreference.getInstance(context)

    private fun provideUserDatabase(context: Context)=
        UserDatabase.getInstance(context)

    private fun provideApiService()=
        ApiConfig.getApiService()

    fun provideUserRepository(
        context: Context,
    ): UserRepository =
        UserRepository.getInstance(
            provideApiService(),
            provideUserDatabase(context),
            provideUserPreference(context)
        )
}