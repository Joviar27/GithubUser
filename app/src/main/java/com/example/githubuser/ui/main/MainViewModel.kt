package com.example.githubuser.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.repository.UserRepository
import com.example.githubuser.domain.usecase.ThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val themeUseCase: ThemeUseCase
): ViewModel() {

    private val _darkMode = MutableLiveData<Boolean>()
    val darkMode : LiveData<Boolean> = _darkMode

    init {
        updateThemeSetting()
    }

    fun updateThemeSetting(){
        themeUseCase.getThemeSetting()
            .onEach{
                _darkMode.value = it
            }.launchIn(viewModelScope)
    }
}