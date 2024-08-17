package com.example.githubuser.domain.interactor

import com.example.githubuser.data.Resource
import com.example.githubuser.data.repository.ThemeRepository
import com.example.githubuser.domain.usecase.ThemeUseCase
import kotlinx.coroutines.flow.Flow

class ThemeInteractor constructor(
    private val themeRepository: ThemeRepository
): ThemeUseCase {

    override fun getThemeSetting(): Flow<Boolean> {
        return themeRepository.getThemeSetting()
    }

    override fun switchThemeSetting(): Flow<Resource<Unit>> {
        return themeRepository.switchThemeSetting()
    }
}