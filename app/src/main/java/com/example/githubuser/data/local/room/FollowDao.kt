package com.example.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubuser.data.local.entity.FollowEntity

@Dao
interface FollowDao {
    @Query("SELECT * FROM follow WHERE type = 0 ORDER BY login DESC")
    fun getFollower(): LiveData<List<FollowEntity>>

    @Query("SELECT * FROM follow WHERE type = 1 ORDER BY login DESC")
    fun getFollowing(): LiveData<List<FollowEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFollow(follow: List<FollowEntity>)

    @Update
    suspend fun updateFollow(follow: FollowEntity)

    @Query("DELETE FROM follow WHERE type = 0")
    suspend fun deleteFollower()

    @Query("DELETE FROM follow WHERE type = 1")
    suspend fun deleteFollowing()

    @Query("SELECT * FROM follow WHERE login = :login")
    fun getSingleFollow(login : String) : LiveData<List<FollowEntity>>
}