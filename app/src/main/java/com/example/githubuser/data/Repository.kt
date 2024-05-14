package com.example.githubuser.data

import android.content.ContentValues.TAG
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
import com.example.githubuser.domain.model.User
import com.example.githubuser.domain.model.mapToBookmarkEntity
import com.example.githubuser.domain.repository.IRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class Repository constructor(
    private val apiService: ApiService,
    private val userDatabase: UserDatabase,
    private val themePreference: ThemePreference
): IRepository{
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

    override fun getBookmarkedDetailUser(name : String) : Flow<Resource<User>> = flow{
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

    override fun getBookmarkedUser() : Flow<Resource<List<User>>> = flow{
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

    override fun setBookmarkedUser(user : User): Flow<Resource<Unit>> = flow{
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

    override fun deleteBookmarkedUser(id : Int): Flow<Resource<Unit>> = flow{
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

    override fun getThemeSetting(): Flow<Boolean>  = flow{
        themePreference.getThemeSetting().onEach {
            emit(it)
        }.catch {
            Log.d(TAG, "getThemeSetting: ${it.message}")
        }.first()
    }

    override fun switchThemeSetting(): Flow<Resource<Unit>> = flow{
        try {
            themePreference.switchThemeSetting()
            emit(Resource.Success(Unit))
        }catch (e: Exception){
            Log.d(TAG, "getThemeSetting: ${e.message}")
            emit(Resource.Error(e.message.toString()))
        }
    }

    companion object{
        @Volatile
        private var instance : Repository? = null

        fun getInstance(
            apiService:ApiService,
            userDatabase: UserDatabase,
            themePreference: ThemePreference
        ) : Repository =
            instance ?: synchronized(this){
                instance ?: Repository(apiService, userDatabase, themePreference)
            }.also { instance=it }
    }
}