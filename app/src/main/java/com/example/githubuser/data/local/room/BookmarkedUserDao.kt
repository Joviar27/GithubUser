package com.example.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubuser.data.local.entity.BookmarkedUserEntity

@Dao
interface BookmarkedUserDao {
    @Query("SELECT * FROM bookmarked_user ORDER BY login DESC")
    fun getBookmarkedUser(): LiveData<List<BookmarkedUserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBookmarkedUser(news: BookmarkedUserEntity)

    @Query("DELETE FROM bookmarked_user WHERE id = :id")
    suspend fun deleteBookmarkedUser(id : Int)

    @Query("SELECT * FROM bookmarked_user WHERE id = :id")
    fun getSingleBookmarked(id : Int) : LiveData<BookmarkedUserEntity>

    @Query("SELECT EXISTS(SELECT * FROM bookmarked_user WHERE id = :id)")
    suspend fun isUserBookmarked(id:Int): Boolean
}