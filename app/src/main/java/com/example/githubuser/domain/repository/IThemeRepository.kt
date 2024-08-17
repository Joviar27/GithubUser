package com.example.githubuser.domain.repository

import com.example.githubuser.data.Resource
import kotlinx.coroutines.flow.Flow

interface IThemeRepository {

    fun getThemeSetting(): Flow<Boolean>
    fun switchThemeSetting(): Flow<Resource<Unit>>
}