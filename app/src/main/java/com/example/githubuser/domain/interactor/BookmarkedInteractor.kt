package com.example.githubuser.domain.interactor

import com.example.githubuser.data.Repository
import com.example.githubuser.data.Resource
import com.example.githubuser.domain.model.User
import com.example.githubuser.domain.usecase.BookmarkedUseCase
import kotlinx.coroutines.flow.Flow

class BookmarkedInteractor constructor(
    private val repository: Repository
): BookmarkedUseCase {
    override fun getBookmarkedDetailUser(name: String): Flow<Resource<User>> {
        return repository.getBookmarkedDetailUser(name)
    }

    override fun getBookmarkedUser(): Flow<Resource<List<User>>> {
        return repository.getBookmarkedUser()
    }

    override fun setBookmarkedUser(user: User): Flow<Resource<Unit>> {
        return repository.setBookmarkedUser(user)
    }

    override fun deleteBookmarkedUser(id: Int): Flow<Resource<Unit>> {
        return repository.deleteBookmarkedUser(id)
    }
}