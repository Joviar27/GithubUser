package com.example.githubuser.ui.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.UserRepository
import kotlinx.coroutines.launch

class FavouriteViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _darkMode = MutableLiveData<Boolean>()
    val darkMode : LiveData<Boolean> = _darkMode

    init {
        updateThemeSetting()
    }

    fun getFavouriteList() = userRepository.getBookmarkedUser().asLiveData()

    fun deleteBookmarkedUser(id : Int) = userRepository.deleteBookmarkedUser(id).asLiveData()

    private fun updateThemeSetting(){
        viewModelScope.launch {
            userRepository.getThemeSetting().collect{
                _darkMode.value = it
            }
        }
    }

    fun switchThemeSetting() {
        viewModelScope.launch {
            userRepository.switchThemeSetting()
        }
    }
}