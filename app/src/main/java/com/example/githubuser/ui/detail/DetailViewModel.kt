package com.example.githubuser.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubuser.domain.model.User
import com.example.githubuser.domain.usecase.BookmarkedUseCase
import com.example.githubuser.domain.usecase.ThemeUseCase
import com.example.githubuser.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val bookmarkedUseCase: BookmarkedUseCase,
    private val themeUseCase: ThemeUseCase
): ViewModel() {

    private val _userName = MutableLiveData<String>()
    private val userName : LiveData<String> = _userName

    private val _darkMode = MutableLiveData<Boolean>()
    val darkMode : LiveData<Boolean> get() = _darkMode

    init {
        getThemeSetting()
    }

    fun updateUserName(name: String) {
        _userName.value = name
    }

    fun getDetailUser() = userUseCase.getDetailUser(userName.value ?: "").asLiveData()

    fun getFavouriteDetailUser() = bookmarkedUseCase.getBookmarkedDetailUser(userName.value ?: "").asLiveData()

    fun setBookmarkedUser(user : User) = bookmarkedUseCase.setBookmarkedUser(user).asLiveData()

    fun deleteBookmarkedUser(id : Int) = bookmarkedUseCase.deleteBookmarkedUser(id).asLiveData()

    fun getThemeSetting(){
        themeUseCase.getThemeSetting()
            .onEach {
                _darkMode.value = it
            }.launchIn(viewModelScope)
    }

    fun switchThemeSetting() = themeUseCase.switchThemeSetting().asLiveData()
}