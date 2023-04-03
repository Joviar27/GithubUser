package com.example.githubuser.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.githubuser.data.local.entity.BookmarkedUserEntity
import com.example.githubuser.data.local.entity.FollowEntity
import com.example.githubuser.data.local.entity.UserEntity
import com.example.githubuser.data.local.room.BookmarkedUserDao
import com.example.githubuser.data.local.room.FollowDao
import com.example.githubuser.data.local.room.UserDao
import com.example.githubuser.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val bookmarkedUserDao: BookmarkedUserDao,
    private val followDao: FollowDao,
    private val dataStore: DataStore<Preferences>
){
    private val THEME_KEY = booleanPreferencesKey("themes_setting")

    fun getUserList(searchQuery: String) : LiveData<Result<List<UserEntity>>> = liveData{
        emit(Result.Loading)

        try{
            val responseSearch = apiService.getUserList(searchQuery)
            val items = responseSearch.items

            val userList = items.map { item ->
                val isBookmarked = bookmarkedUserDao.isUserBookmarked(item.id)

                UserEntity(
                    id = item .id,
                    login = item .login,
                    avatar_url = item .avatarUrl,
                    isBookmarked = isBookmarked
                )
            }

            userDao.deleteAll()
            userDao.insertUser(userList)
        }
        catch (e : Exception){
            Log.d("UserRepo", "getUserList : ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
        val localData : LiveData<Result<List<UserEntity>>> =
            userDao.getUserList().map {
                Result.Success(it)
            }
        emitSource(localData)
    }

    fun getDetailUser(name : String) : LiveData<Result<List<UserEntity>>> = liveData{
        emit(Result.Loading)

        try{
            val responseDetail = apiService.getUserDetail(name)

            val isBookmarked = bookmarkedUserDao.isUserBookmarked(responseDetail.id)

            val user = UserEntity(
                id = responseDetail.id,
                login = responseDetail.login,
                avatar_url = responseDetail.avatarUrl,
                followers = responseDetail.followers,
                following = responseDetail.following,
                location = responseDetail.location,
                name = responseDetail.name,
                isBookmarked = isBookmarked
            )
            userDao.updateUser(user)
        }
        catch (e : Exception){
            Log.d("UserRepo", "getDetailUser : ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }

        val localData : LiveData<Result<List<UserEntity>>> =
              userDao.getSingleUser(name).map { user->
                Result.Success(user)
            }
        emitSource(localData)
    }

    fun getBookmarkedUser() : LiveData<List<BookmarkedUserEntity>>{
        return bookmarkedUserDao.getBookmarkedUser()
    }

    suspend fun setBookmarkedUser(user : UserEntity) {
        val bookmarkedUser = BookmarkedUserEntity(
            id = user.id,
            name = user.name,
            login = user.login,
            location = user.location,
            avatar_url = user.avatar_url,
            followers = user.followers,
            following = user.following
        )
        userDao.updateBookmarked(user.id, true)
        bookmarkedUserDao.insertBookmarkedUser(bookmarkedUser)
    }

    suspend fun deleteBookmarkedUser(id : Int){
        userDao.updateBookmarked(id, false)
        bookmarkedUserDao.deleteBookmarkedUser(id)
    }

    fun getFollower(name : String) : LiveData<Result<List<FollowEntity>>> = liveData{
        emit(Result.Loading)

        try {
            val followers = apiService.getFollowers(name)
            followDao.deleteFollower()

            val followerList = followers.map {

                FollowEntity(
                    id = it.id,
                    login = it.login,
                    avatar_url = it.avatarUrl,
                    type = 0
                )
            }
            followDao.insertFollow(followerList)
        }
        catch (e : Exception){
            Log.d("UserRepo", "getFollowers : ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }

        val localData : LiveData<Result<List<FollowEntity>>> = followDao.getFollower().map {
            Result.Success(it)
        }
        emitSource(localData)
    }

    fun getFollowing(name : String) : LiveData<Result<List<FollowEntity>>> = liveData{
        emit(Result.Loading)

        try {
            val following = apiService.getFollowing(name)
            followDao.deleteFollowing()
            val followingList = following.map {

                FollowEntity(
                    id = it.id,
                    login = it.login,
                    avatar_url = it.avatarUrl,
                    type = 1
                )
            }
            followDao.insertFollow(followingList)
        }
        catch (e : Exception){
            Log.d("UserRepo", "getFollowing : ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }

        val localData : LiveData<Result<List<FollowEntity>>> = followDao.getFollowing().map {
            Result.Success(it)
        }
        emitSource(localData)
    }

    fun getThemeSetting() : Flow<Boolean> {
        return dataStore.data.map {
            it[THEME_KEY] ?: true
        }
    }

    suspend fun saveSetting(darkModeActive : Boolean){
        dataStore.edit {
            it[THEME_KEY] = darkModeActive
        }
    }

    companion object{
        @Volatile
        private var instance : UserRepository? = null

        fun getInstance(
            apiService:ApiService,
            userDao: UserDao,
            bookmarkedUserDao: BookmarkedUserDao,
            followDao: FollowDao,
            dataStore: DataStore<Preferences>
        ) : UserRepository =
            instance ?: synchronized(this){
                instance ?: UserRepository(apiService, userDao, bookmarkedUserDao,followDao,dataStore)
            }.also { instance=it }
    }
}