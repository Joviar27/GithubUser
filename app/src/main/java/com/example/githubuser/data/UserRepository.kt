package com.example.githubuser.data

import android.util.Log
import androidx.room.withTransaction
import com.example.githubuser.data.local.entity.mapToDomain
import com.example.githubuser.data.local.preference.ThemePreference
import com.example.githubuser.data.local.room.UserDatabase
import com.example.githubuser.data.remote.response.mapToBookmarkEntity
import com.example.githubuser.data.remote.response.mapToEntity
import com.example.githubuser.data.remote.response.mapToFollowerEntity
import com.example.githubuser.data.remote.response.mapToFollowingEntity
import com.example.githubuser.data.remote.retrofit.ApiService
import com.example.githubuser.domain.User
import com.example.githubuser.domain.mapToBookmarkEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class UserRepository constructor(
    private val apiService: ApiService,
    private val userDatabase: UserDatabase,
    private val themePreference: ThemePreference
){
    fun getUserList(searchQuery: String?) : Flow<Resource<List<User>>> = flow{
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

    fun getDetailUser(name : String) : Flow<Resource<User>> = flow{
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

    fun getFavouriteDetailUser(name : String) : Flow<Resource<User>> = flow{
        emit(Resource.Loading)
        try{
            val bookmarkedDao = userDatabase.bookmarkedUserDao()

            val responseDetail = apiService.getUserDetail(name)
            responseDetail?.let{
                val userDetail = it.mapToBookmarkEntity()
                bookmarkedDao.updateUser(userDetail)
            }
        }
        catch (e : Exception){
            Log.d("UserRepo", "getDetailUser : ${e.message.toString()}")
            emit(Resource.Error(e.message.toString()))
        } finally {
            val bookmarkedDao = userDatabase.bookmarkedUserDao()
            val localData = bookmarkedDao.getSingleBookmarked(name).map{ user->
                Resource.Success(user.mapToDomain())
            }
            emitAll(localData)
        }
    }

    fun getBookmarkedUser() : Flow<Resource<List<User>>> = flow{
        emit(Resource.Loading)
        try{
            val bookmarkedDao = userDatabase.bookmarkedUserDao()
            val bookmarkedUser = bookmarkedDao.getBookmarkedUser().map {
                Resource.Success(it.mapToDomain())
            }
            emitAll(bookmarkedUser)
        } catch (e : Exception) {
            Log.d("UserRepo", "getBookmarkedUser : ${e.message.toString()}")
            emit(Resource.Error(e.message.toString()))
        }
    }

    fun setBookmarkedUser(user : User): Flow<Resource<Unit>> = flow{
        emit(Resource.Loading)
        try{
            val bookmarkedDao = userDatabase.bookmarkedUserDao()
            val userDao = userDatabase.userDao()

            userDatabase.withTransaction {
                userDao.updateBookmarked(user.id, true)
                bookmarkedDao.insertBookmarkedUser(user.mapToBookmarkEntity())
            }
            emit(Resource.Success(Unit))
        } catch (e : Exception){
            Log.d("UserRepo", "setBookmarkedUser : ${e.message.toString()}")
            emit(Resource.Error(e.message.toString()))
        }
    }

    fun deleteBookmarkedUser(id : Int): Flow<Resource<Unit>> = flow{
        emit(Resource.Loading)
        try {
            val bookmarkedDao = userDatabase.bookmarkedUserDao()
            val userDao = userDatabase.userDao()

            userDatabase.withTransaction {
                userDao.updateBookmarked(id, false)
                bookmarkedDao.deleteBookmarkedUser(id)
            }
            emit(Resource.Success(Unit))
        }catch (e : Exception){
            Log.d("UserRepo", "deleteBookmarkedUser : ${e.message.toString()}")
            emit(Resource.Error(e.message.toString()))
        }
    }

    fun getFollower(name : String) : Flow<Resource<List<User>>> = flow{
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

    fun getFollowing(name : String) : Flow<Resource<List<User>>> = flow{
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

    fun getThemeSetting() = themePreference.getThemeSetting()

    suspend fun switchThemeSetting(){
        themePreference.switchThemeSetting()
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
            }.also { instance=it }
    }
}