package com.example.core.data.local.room

import androidx.room.*
import com.example.core.data.local.entity.FollowerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FollowerDao {
    @Query("SELECT * FROM follower ORDER BY login DESC")
    fun getFollower(): Flow<List<FollowerEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFollow(follow: List<FollowerEntity>)

    @Update
    suspend fun updateFollow(follow: FollowerEntity)

    @Query("DELETE FROM follower")
    suspend fun deleteFollower()

    @Query("SELECT * FROM follower WHERE login = :login")
    fun getSingleFollow(login : String) : Flow<List<FollowerEntity>>
}