package com.example.githubuser.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.room.withTransaction
import com.example.githubuser.data.Resource
import com.example.githubuser.data.local.entity.mapToDomain
import com.example.githubuser.data.local.preference.ThemePreference
import com.example.githubuser.data.local.room.UserDatabase
import com.example.githubuser.data.remote.response.mapToBookmarkEntity
import com.example.githubuser.data.remote.response.mapToEntity
import com.example.githubuser.data.remote.response.mapToFollowerEntity
import com.example.githubuser.data.remote.response.mapToFollowingEntity
import com.example.githubuser.data.remote.retrofit.ApiService
import com.example.githubuser.domain.model.User
import com.example.githubuser.domain.model.mapToBookmarkEntity
import com.example.githubuser.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class UserRepository constructor(
    private val apiService: ApiService,
    private val userDatabase: UserDatabase,
    private val themePreference: ThemePreference
): IUserRepository{
    override fun getUserList(searchQuery: String?) : Flow<Resource<List<User>>> = flow{
        emit(Resource.Loading)
        try{
            val bookmarkedDao = userDatabase.bookmarkedUserDao()
            val userDao = userDatabase.userDao()

            val responseItems = if(searchQuery.isNullOrEmpty()){
                apiService.getGithubUser()
            } else {
                apiService.searchUser(searchQuery).items
            }

            if(!responseItems.isNullOrEmpty()){
                val userList = responseItems.map { item ->
                    val isBookmarked = bookmarkedDao.isUserBookmarked(item.id)
                    item.mapToEntity(isBookmarked)
                }
                userDatabase.withTransaction {
                    userDao.deleteAll()
                    userDao.insertUser(userList)
                }
            }
        } catch (e : Exception){
            Log.d("UserRepo", "getUserList : ${e.message.toString()}")
            emit(Resource.Error(e.message.toString()))
        } finally {
            val userDao = userDatabase.userDao()
            val localData = userDao.getUserList().map{
                if(it.isNotEmpty()) Resource.Success(it.mapToDomain())
                else Resource.Error("User data can't be found")
            }
            emitAll(localData)
        }
    }

    override fun getDetailUser(name : String) : Flow<Resource<User>> = flow{
        emit(Resource.Loading)
        try{
            val bookmarkedDao = userDatabase.bookmarkedUserDao()
            val userDao = userDatabase.userDao()

            val responseDetail = apiService.getUserDetail(name)
            responseDetail?.let{
                val isBookmarked = bookmarkedDao.isUserBookmarked(responseDetail.id)
                val userDetail = it.mapToEntity(isBookmarked)
                userDao.updateUser(userDetail)
            }
        }
        catch (e : Exception){
            Log.d("UserRepo", "getDetailUser : ${e.message.toString()}")
            emit(Resource.Error(e.message.toString()))
        } finally {
            val userDao = userDatabase.userDao()
            val localData = userDao.getSingleUser(name).map{ user->
                Resource.Success(user.mapToDomain())
            }
            emitAll(localData)
        }
    }

    override fun getFollower(name : String) : Flow<Resource<List<User>>> = flow{
        emit(Resource.Loading)
        try {
            val followDao = userDatabase.followerDao()

            val followerResponse = apiService.getFollowers(name)
            val followerList = followerResponse.map {it.mapToFollowerEntity()}

            userDatabase.withTransaction {
                followDao.deleteFollower()
                followDao.insertFollow(followerList)
            }
        }
        catch (e : Exception){
            Log.d("UserRepo", "getFollowers : ${e.message.toString()}")
            emit(Resource.Error(e.message.toString()))
        } finally {
            val followerDao = userDatabase.followerDao()
            val localData = followerDao.getFollower().map{
                Resource.Success(it.mapToDomain())
            }
            emitAll(localData)
        }
    }

    override fun getFollowing(name : String) : Flow<Resource<List<User>>> = flow{
        emit(Resource.Loading)
        try {
            val followDao = userDatabase.followingDao()

            val followerResponse = apiService.getFollowing(name)
            val followerList = followerResponse.map{it.mapToFollowingEntity()}

            userDatabase.withTransaction {
                followDao.deleteFollowing()
                followDao.insertFollow(followerList)
            }
        }
        catch (e : Exception){
            Log.d("UserRepo", "getFollowing : ${e.message.toString()}")
            emit(Resource.Error(e.message.toString()))
        } finally {
            val followingDao = userDatabase.followingDao()
            val localData = followingDao.getFollowing().map{
                Resource.Success(it.mapToDomain())
            }
            emitAll(localData)
        }
    }

    companion object{
        @Volatile
        private var instance : UserRepository? = null

        fun getInstance(
            apiService:ApiService,
            userDatabase: UserDatabase,
            themePreference: ThemePreference
        ) : UserRepository =
            instance ?: synchronized(this){
                instance ?: UserRepository(apiService, userDatabase, themePreference)
            }.also { instance =it }
    }
}