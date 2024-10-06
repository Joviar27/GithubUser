package com.example.core.domain.usecase

import com.example.core.data.Resource
import kotlinx.coroutines.flow.Flow

interface ThemeUseCase {
    fun getThemeSetting(): Flow<Boolean>
    fun switchThemeSetting(): Flow<Resource<Unit>>
}