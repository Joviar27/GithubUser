package com.example.core.domain.usecase

import com.example.core.data.Resource
import com.example.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface BookmarkedUseCase {
    fun getBookmarkedDetailUser(name : String) : Flow<Resource<User>>
    fun getBookmarkedUser() : Flow<Resource<List<User>>>
    fun setBookmarkedUser(user : User): Flow<Resource<Unit>>
    fun deleteBookmarkedUser(id : Int): Flow<Resource<Unit>>
}