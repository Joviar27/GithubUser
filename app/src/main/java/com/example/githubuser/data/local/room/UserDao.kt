package com.example.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubuser.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM user ORDER BY login DESC")
    fun getUserList(): LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: List<UserEntity>)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE user SET bookmarked = :status WHERE id = :id")
    suspend fun updateBookmarked(id : Int, status : Boolean)

    @Query("DELETE FROM user")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM user WHERE id = :id AND bookmarked = 1)")
    suspend fun isUserBookmarked(id : Int): Boolean

    @Query("SELECT * FROM user WHERE login = :login")
    fun getSingleUser(login : String) : LiveData<List<UserEntity>>
}