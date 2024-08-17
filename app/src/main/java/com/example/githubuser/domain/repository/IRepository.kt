package com.example.githubuser.domain.repository

import com.example.githubuser.data.Resource
import com.example.githubuser.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IRepository {
    fun getUserList(searchQuery: String?): Flow<Resource<List<User>>>
    fun getDetailUser(name: String): Flow<Resource<User>>
    fun getFollower(name : String) : Flow<Resource<List<User>>>
    fun getFollowing(name : String) : Flow<Resource<List<User>>>
    fun getBookmarkedDetailUser(name : String) : Flow<Resource<User>>
    fun getBookmarkedUser() : Flow<Resource<List<User>>>
    fun setBookmarkedUser(user : User): Flow<Resource<Unit>>
    fun deleteBookmarkedUser(id : Int): Flow<Resource<Unit>>
    fun getThemeSetting(): Flow<Boolean>
    fun switchThemeSetting(): Flow<Resource<Unit>>
}