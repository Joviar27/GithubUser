package com.example.githubuser.domain.interactor

import com.example.githubuser.data.Resource
import com.example.githubuser.data.repository.ThemeRepository
import com.example.githubuser.domain.repository.IThemeRepository
import com.example.githubuser.domain.usecase.ThemeUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class ThemeInteractor @Inject constructor(
    private val themeRepository: IThemeRepository
): ThemeUseCase {

    override fun getThemeSetting(): Flow<Boolean> {
        return themeRepository.getThemeSetting()
    }

    override fun switchThemeSetting(): Flow<Resource<Unit>> {
        return themeRepository.switchThemeSetting()
    }
}