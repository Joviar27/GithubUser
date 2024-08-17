package com.example.githubuser.domain.usecase

import com.example.githubuser.data.Resource
import kotlinx.coroutines.flow.Flow

interface ThemeUseCase {
    fun getThemeSetting(): Flow<Boolean>
    fun switchThemeSetting(): Flow<Resource<Unit>>
}