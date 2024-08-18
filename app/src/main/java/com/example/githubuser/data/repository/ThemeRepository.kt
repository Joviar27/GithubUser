package com.example.githubuser.data.repository

import android.content.ContentValues
import android.util.Log
import com.example.githubuser.data.Resource
import com.example.githubuser.data.local.preference.ThemePreference
import com.example.githubuser.domain.repository.IThemeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeRepository @Inject constructor(
    private val themePreference: ThemePreference
): IThemeRepository {

    override fun getThemeSetting(): Flow<Boolean> = flow{
        themePreference.getThemeSetting().onEach {
            emit(it)
        }.catch {
            Log.d(ContentValues.TAG, "getThemeSetting: ${it.message}")
        }.first()
    }

    override fun switchThemeSetting(): Flow<Resource<Unit>> = flow{
        try {
            themePreference.switchThemeSetting()
            emit(Resource.Success(Unit))
        }catch (e: Exception){
            Log.d(ContentValues.TAG, "getThemeSetting: ${e.message}")
            emit(Resource.Error(e.message.toString()))
        }
    }
}