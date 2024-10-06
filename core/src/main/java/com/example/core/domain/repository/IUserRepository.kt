package com.example.core.domain.repository

import com.example.core.data.Resource
import com.example.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun getUserList(searchQuery: String?): Flow<Resource<List<User>>>
    fun getDetailUser(name: String): Flow<Resource<User>>
    fun getFollower(name : String) : Flow<Resource<List<User>>>
    fun getFollowing(name : String) : Flow<Resource<List<User>>>
}