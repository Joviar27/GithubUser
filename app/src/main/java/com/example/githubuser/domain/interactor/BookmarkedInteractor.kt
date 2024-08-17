package com.example.githubuser.domain.interactor

import com.example.githubuser.data.Resource
import com.example.githubuser.data.repository.BookmarkedRepository
import com.example.githubuser.domain.model.User
import com.example.githubuser.domain.usecase.BookmarkedUseCase
import kotlinx.coroutines.flow.Flow

class BookmarkedInteractor constructor(
    private val bookmarkedRepository: BookmarkedRepository
): BookmarkedUseCase {
    override fun getBookmarkedDetailUser(name: String): Flow<Resource<User>> {
        return bookmarkedRepository.getBookmarkedDetailUser(name)
    }

    override fun getBookmarkedUser(): Flow<Resource<List<User>>> {
        return bookmarkedRepository.getBookmarkedUser()
    }

    override fun setBookmarkedUser(user: User): Flow<Resource<Unit>> {
        return bookmarkedRepository.setBookmarkedUser(user)
    }

    override fun deleteBookmarkedUser(id: Int): Flow<Resource<Unit>> {
        return bookmarkedRepository.deleteBookmarkedUser(id)
    }
}