package com.example.core.domain.interactor

import com.example.core.data.Resource
import com.example.core.data.repository.ThemeRepository
import com.example.core.domain.repository.IThemeRepository
import com.example.core.domain.usecase.ThemeUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class ThemeInteractor @Inject constructor(
    private val themeRepository: IThemeRepository
): ThemeUseCase {

    override fun getThemeSetting(): Flow<Boolean> {
        return themeRepository.getThemeSetting()
    }

    override fun switchThemeSetting(): Flow<com.example.core.data.Resource<Unit>> {
        return themeRepository.switchThemeSetting()
    }
}