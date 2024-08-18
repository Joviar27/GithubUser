package com.example.githubuser.data.repository

import android.util.Log
import androidx.room.withTransaction
import com.example.githubuser.data.Resource
import com.example.githubuser.data.local.entity.mapToDomain
import com.example.githubuser.data.local.room.UserDatabase
import com.example.githubuser.data.remote.response.mapToBookmarkEntity
import com.example.githubuser.data.remote.retrofit.ApiService
import com.example.githubuser.domain.model.User
import com.example.githubuser.domain.model.mapToBookmarkEntity
import com.example.githubuser.domain.repository.IBookmarkedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkedRepository @Inject constructor(
    private val apiService: ApiService,
    private val userDatabase: UserDatabase,
): IBookmarkedRepository {
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
}