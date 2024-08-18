package com.example.githubuser.ui.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.githubuser.domain.usecase.BookmarkedUseCase
import com.example.githubuser.domain.usecase.ThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val bookmarkedUseCase: BookmarkedUseCase,
    private val themeUseCase: ThemeUseCase
) : ViewModel() {

    fun getFavouriteList() = bookmarkedUseCase.getBookmarkedUser().asLiveData()

    fun deleteBookmarkedUser(id : Int) = bookmarkedUseCase.deleteBookmarkedUser(id).asLiveData()

    fun getThemeSetting() = themeUseCase.getThemeSetting().asLiveData()

    fun switchThemeSetting() = themeUseCase.switchThemeSetting().asLiveData()
}