package com.example.githubuser.ui.follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.githubuser.data.UserRepository

class FollowViewModel(
    private val userRepository: UserRepository
) : ViewModel(){

    private val _userName = MutableLiveData<String>()
    private val userName : LiveData<String> = _userName

    private val _type = MutableLiveData<TabType>()
    private val type : LiveData<TabType> = _type

    fun getUserList() =
        when(type.value ?: TabType.FOLLOWER){
            TabType.FOLLOWER-> {
                userRepository.getFollower(userName.value ?: "").asLiveData()
            }
            TabType.FOLLOWING ->{
                userRepository.getFollowing(userName.value ?: "").asLiveData()
            }
        }

    fun updateUserName(name: String) {
        _userName.value = name
    }

    fun updateTabType(type: TabType) {
        _type.value = type
    }
}