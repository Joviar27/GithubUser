package com.example.githubuser.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.githubuser.data.Resource
import com.example.githubuser.domain.model.User
import com.example.githubuser.domain.usecase.BookmarkedUseCase
import com.example.githubuser.domain.usecase.ThemeUseCase
import com.example.githubuser.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val bookmarkedUseCase: BookmarkedUseCase,
    private val themeUseCase: ThemeUseCase
):ViewModel(){

    private val _query = MutableLiveData<String?>()
    val query : LiveData<String?> get() = _query

    fun updateQuery(query: String?){
        _query.value = query
    }

    fun getUserList():LiveData<Resource<List<User>>>{
        return userUseCase.getUserList(query.value).asLiveData()
    }

    fun setBookmarkedUser(user : User) = bookmarkedUseCase.setBookmarkedUser(user).asLiveData()

    fun deleteBookmarkedUser(id : Int) = bookmarkedUseCase.deleteBookmarkedUser(id).asLiveData()

    fun getThemeSetting() = themeUseCase.getThemeSetting().asLiveData()

    fun switchThemeSetting() = themeUseCase.switchThemeSetting().asLiveData()
}