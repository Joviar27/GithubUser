package com.example.githubuser.domain.interactor

import com.example.githubuser.data.Repository
import com.example.githubuser.data.Resource
import com.example.githubuser.domain.model.User
import com.example.githubuser.domain.usecase.UserUseCase
import kotlinx.coroutines.flow.Flow

class UserInteractor constructor(
    private val repository: Repository
): UserUseCase{

    override fun getUserList(searchQuery: String?): Flow<Resource<List<User>>> {
        return repository.getUserList(searchQuery)
    }

    override fun getDetailUser(name: String): Flow<Resource<User>> {
        return repository.getDetailUser(name)
    }

    override fun getFollower(name: String): Flow<Resource<List<User>>> {
        return repository.getFollower(name)
    }

    override fun getFollowing(name: String): Flow<Resource<List<User>>> {
        return repository.getFollowing(name)
    }
}