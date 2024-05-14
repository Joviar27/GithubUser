package com.example.githubuser.domain.interactor

import com.example.githubuser.data.Repository
import com.example.githubuser.data.Resource
import com.example.githubuser.domain.usecase.ThemeUseCase
import kotlinx.coroutines.flow.Flow

class ThemeInteractor constructor(
    private val repository: Repository
): ThemeUseCase {

    override fun getThemeSetting(): Flow<Boolean> {
        return repository.getThemeSetting()
    }

    override fun switchThemeSetting(): Flow<Resource<Unit>> {
        return repository.switchThemeSetting()
    }
}