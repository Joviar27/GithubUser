package com.example.githubuser.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.githubuser.data.local.entity.BookmarkedUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkedUserDao {
    @Query("SELECT * FROM bookmarked_user ORDER BY login DESC")
    fun getBookmarkedUser(): Flow<List<BookmarkedUserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBookmarkedUser(news: BookmarkedUserEntity)

    @Update
    suspend fun updateUser(user: BookmarkedUserEntity)

    @Query("DELETE FROM bookmarked_user WHERE id = :id")
    suspend fun deleteBookmarkedUser(id : Int)

    @Query("SELECT * FROM bookmarked_user WHERE login = :name")
    fun getSingleBookmarked(name : String) : Flow<BookmarkedUserEntity>

    @Query("SELECT EXISTS(SELECT * FROM bookmarked_user WHERE id = :id)")
    suspend fun isUserBookmarked(id:Int): Boolean
}