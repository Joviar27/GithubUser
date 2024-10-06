package com.example.core.domain.repository

import com.example.core.data.Resource
import kotlinx.coroutines.flow.Flow

interface IThemeRepository {

    fun getThemeSetting(): Flow<Boolean>
    fun switchThemeSetting(): Flow<Resource<Unit>>
}