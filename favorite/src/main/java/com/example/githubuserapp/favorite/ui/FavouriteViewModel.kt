package com.example.githubuserapp.favorite.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.BookmarkedUseCase
import com.example.core.domain.usecase.ThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val bookmarkedUseCase: BookmarkedUseCase,
    private val themeUseCase: ThemeUseCase
) : ViewModel() {

    private val _darkMode = MutableLiveData<Boolean>()
    val darkMode : LiveData<Boolean> get() = _darkMode

    init {
        getThemeSetting()
    }

    fun getFavouriteList() = bookmarkedUseCase.getBookmarkedUser().asLiveData()

    fun deleteBookmarkedUser(id : Int) = bookmarkedUseCase.deleteBookmarkedUser(id).asLiveData()

    fun getThemeSetting() {
        themeUseCase.getThemeSetting()
            .onEach {
                _darkMode.value = it
            }.launchIn(viewModelScope)
    }

    fun switchThemeSetting() = themeUseCase.switchThemeSetting().asLiveData()
}