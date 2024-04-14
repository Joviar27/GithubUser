package com.example.githubuser.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.githubuser.data.local.entity.FollowingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FollowingDao {
    @Query("SELECT * FROM following ORDER BY login DESC")
    fun getFollowing(): Flow<List<FollowingEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFollow(follow: List<FollowingEntity>)

    @Update
    suspend fun updateFollow(follow: FollowingEntity)

    @Query("DELETE FROM following")
    suspend fun deleteFollowing()

    @Query("SELECT * FROM following WHERE login = :login")
    fun getSingleFollow(login : String) : Flow<List<FollowingEntity>>
}