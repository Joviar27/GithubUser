package com.example.githubuser.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.repository.UserRepository
import com.example.githubuser.domain.usecase.ThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private fun updateThemeSetting(){
        viewModelScope.launch {
            themeUseCase.getThemeSetting().collect{
                _darkMode.value = it
            }
        }
    }
}