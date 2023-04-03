package com.example.githubuser.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.UserRepository
import com.example.githubuser.data.local.entity.UserEntity
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository):ViewModel(){

    fun getUserList(searchQuery : String) = userRepository.getUserList(searchQuery)

    fun getDetailUser(name : String) = userRepository.getDetailUser(name)


    fun getBookmarkedUser() = userRepository.getBookmarkedUser()


    fun setBookmarkedUser(user : UserEntity){
        viewModelScope.launch {
            userRepository.setBookmarkedUser(user)
        }
    }

    fun deleteBookmarkedUser(id : Int){
        viewModelScope.launch {
            userRepository.deleteBookmarkedUser(id)
        }
    }

    fun getFollower(name : String) = userRepository.getFollower(name)

    fun getFollowing(name : String) = userRepository.getFollowing(name)

    fun getThemeSetting() : LiveData<Boolean> {
        return userRepository.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(darkModeActive : Boolean){
        viewModelScope.launch {
            userRepository.saveSetting(darkModeActive)
        }
    }
}