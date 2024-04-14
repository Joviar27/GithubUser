package com.example.githubuser.ui.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.Resource
import com.example.githubuser.data.UserRepository
import com.example.githubuser.domain.User
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository
):ViewModel(){

    private val _darkMode = MutableLiveData<Boolean>()
    val darkMode : LiveData<Boolean> = _darkMode

    private val _query = MutableLiveData<String?>()
    val query : LiveData<String?> get() = _query

    init {
        updateThemeSetting()
    }

    fun updateQuery(query: String?){
        _query.value = query
    }

    fun getUserList():LiveData<Resource<List<User>>>{
        Log.d(TAG, "Terpanggil with query ${_query.value}")
        return userRepository.getUserList(query.value).asLiveData()
    }

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