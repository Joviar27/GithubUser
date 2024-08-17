package com.example.githubuser.ui.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.githubuser.data.Repository

class FavouriteViewModel(
    private val userRepository: Repository
) : ViewModel() {

    fun getFavouriteList() = userRepository.getBookmarkedUser().asLiveData()

    fun deleteBookmarkedUser(id : Int) = userRepository.deleteBookmarkedUser(id).asLiveData()

    fun getThemeSetting() = userRepository.getThemeSetting().asLiveData()

    fun switchThemeSetting() = userRepository.switchThemeSetting().asLiveData()
}