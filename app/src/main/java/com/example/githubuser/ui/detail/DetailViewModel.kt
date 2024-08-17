package com.example.githubuser.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.githubuser.data.repository.UserRepository
import com.example.githubuser.domain.model.User

class DetailViewModel(
    private val userRepository: UserRepository
): ViewModel() {

    private val _userName = MutableLiveData<String>()
    private val userName : LiveData<String> = _userName

    fun updateUserName(name: String) {
        _userName.value = name
    }

    fun getDetailUser() = userRepository.getDetailUser(userName.value ?: "").asLiveData()

    fun getFavouriteDetailUser() = userRepository.getBookmarkedDetailUser(userName.value ?: "").asLiveData()

    fun setBookmarkedUser(user : User) = userRepository.setBookmarkedUser(user).asLiveData()

    fun deleteBookmarkedUser(id : Int) = userRepository.deleteBookmarkedUser(id).asLiveData()

    fun getThemeSetting() = userRepository.getThemeSetting().asLiveData()

    fun switchThemeSetting() = userRepository.switchThemeSetting().asLiveData()
}