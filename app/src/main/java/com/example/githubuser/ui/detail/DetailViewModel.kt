package com.example.githubuser.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.UserRepository
import com.example.githubuser.domain.User
import kotlinx.coroutines.launch

class DetailViewModel(
    private val userRepository: UserRepository
): ViewModel() {

    private val _darkMode = MutableLiveData<Boolean>()
    val darkMode : LiveData<Boolean> = _darkMode

    private val _userName = MutableLiveData<String>()
    private val userName : LiveData<String> = _userName

    init {
        updateThemeSetting()
    }

    fun updateUserName(name: String) {
        _userName.value = name
    }

    fun getDetailUser() = userRepository.getDetailUser(userName.value ?: "").asLiveData()

    fun getFavouriteDetailUser() = userRepository.getFavouriteDetailUser(userName.value ?: "").asLiveData()

    fun setBookmarkedUser(user : User) = userRepository.setBookmarkedUser(user).asLiveData()

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