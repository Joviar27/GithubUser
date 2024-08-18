package com.example.githubuser.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.githubuser.data.repository.UserRepository
import com.example.githubuser.domain.model.User
import com.example.githubuser.domain.usecase.BookmarkedUseCase
import com.example.githubuser.domain.usecase.ThemeUseCase
import com.example.githubuser.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val bookmarkedUseCase: BookmarkedUseCase,
    private val themeUseCase: ThemeUseCase
): ViewModel() {

    private val _userName = MutableLiveData<String>()
    private val userName : LiveData<String> = _userName

    fun updateUserName(name: String) {
        _userName.value = name
    }

    fun getDetailUser() = userUseCase.getDetailUser(userName.value ?: "").asLiveData()

    fun getFavouriteDetailUser() = bookmarkedUseCase.getBookmarkedDetailUser(userName.value ?: "").asLiveData()

    fun setBookmarkedUser(user : User) = bookmarkedUseCase.setBookmarkedUser(user).asLiveData()

    fun deleteBookmarkedUser(id : Int) = bookmarkedUseCase.deleteBookmarkedUser(id).asLiveData()

    fun getThemeSetting() = themeUseCase.getThemeSetting().asLiveData()

    fun switchThemeSetting() = themeUseCase.switchThemeSetting().asLiveData()
}