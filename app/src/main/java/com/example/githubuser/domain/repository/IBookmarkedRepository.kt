package com.example.githubuser.domain.repository

import com.example.githubuser.data.Resource
import com.example.githubuser.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IBookmarkedRepository {

    fun getBookmarkedDetailUser(name : String) : Flow<Resource<User>>
    fun getBookmarkedUser() : Flow<Resource<List<User>>>
    fun setBookmarkedUser(user : User): Flow<Resource<Unit>>
    fun deleteBookmarkedUser(id : Int): Flow<Resource<Unit>>
}