package com.example.githubuser.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.githubuser.data.Resource
import com.example.githubuser.data.Repository
import com.example.githubuser.domain.model.User

class HomeViewModel(
    private val userRepository: Repository
):ViewModel(){

    private val _query = MutableLiveData<String?>()
    val query : LiveData<String?> get() = _query

    fun updateQuery(query: String?){
        _query.value = query
    }

    fun getUserList():LiveData<Resource<List<User>>>{
        return userRepository.getUserList(query.value).asLiveData()
    }

    fun setBookmarkedUser(user : User) = userRepository.setBookmarkedUser(user).asLiveData()

    fun deleteBookmarkedUser(id : Int) = userRepository.deleteBookmarkedUser(id).asLiveData()

    fun getThemeSetting() = userRepository.getThemeSetting().asLiveData()

    fun switchThemeSetting() = userRepository.switchThemeSetting().asLiveData()
}