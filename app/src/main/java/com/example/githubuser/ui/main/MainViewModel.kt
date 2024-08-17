package com.example.githubuser.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.Repository
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: Repository
): ViewModel() {

    private val _darkMode = MutableLiveData<Boolean>()
    val darkMode : LiveData<Boolean> = _darkMode

    init {
        updateThemeSetting()
    }

    private fun updateThemeSetting(){
        viewModelScope.launch {
            userRepository.getThemeSetting().collect{
                _darkMode.value = it
            }
        }
    }
}