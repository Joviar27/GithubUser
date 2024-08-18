package com.example.githubuser.domain.interactor

import com.example.githubuser.data.repository.UserRepository
import com.example.githubuser.data.Resource
import com.example.githubuser.domain.model.User
import com.example.githubuser.domain.repository.IUserRepository
import com.example.githubuser.domain.usecase.UserUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserInteractor @Inject constructor(
    private val userRepository: IUserRepository
): UserUseCase{

    override fun getUserList(searchQuery: String?): Flow<Resource<List<User>>> {
        return userRepository.getUserList(searchQuery)
    }

    override fun getDetailUser(name: String): Flow<Resource<User>> {
        return userRepository.getDetailUser(name)
    }

    override fun getFollower(name: String): Flow<Resource<List<User>>> {
        return userRepository.getFollower(name)
    }

    override fun getFollowing(name: String): Flow<Resource<List<User>>> {
        return userRepository.getFollowing(name)
    }
}