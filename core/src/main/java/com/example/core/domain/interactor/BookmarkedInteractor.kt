package com.example.core.domain.interactor

import com.example.core.data.Resource
import com.example.core.domain.model.User
import com.example.core.domain.repository.IBookmarkedRepository
import com.example.core.domain.usecase.BookmarkedUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookmarkedInteractor @Inject constructor(
    private val bookmarkedRepository: IBookmarkedRepository
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