package com.example.githubuser.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.githubuser.data.UserRepository
import com.example.githubuser.data.local.room.UserDatabase
import com.example.githubuser.data.remote.retrofit.ApiConfig

object Injection {

    fun provideRepository(context: Context, dataStore: DataStore <Preferences>) : UserRepository{
        val apiService = ApiConfig.getApiService()

        val database = UserDatabase.getInstance(context)
        val userDao =  database.userDao()
        val bookmarkedUserDao = database.bookmarkedUserDao()
        val followDao  = database.followDao()

        return UserRepository.getInstance(apiService, userDao, bookmarkedUserDao, followDao, dataStore)
    }
}