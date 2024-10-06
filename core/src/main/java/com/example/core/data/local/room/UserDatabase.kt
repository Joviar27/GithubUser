package com.example.core.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.core.data.local.entity.BookmarkedUserEntity
import com.example.core.data.local.entity.FollowerEntity
import com.example.core.data.local.entity.FollowingEntity
import com.example.core.data.local.entity.UserEntity

@Database(entities = [
    UserEntity::class,
    BookmarkedUserEntity::class,
    FollowerEntity::class,
    FollowingEntity::class],
    version = 1,
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun bookmarkedUserDao(): BookmarkedUserDao
    abstract fun followerDao() : FollowerDao
    abstract fun followingDao() : FollowingDao

    companion object {
        @Volatile
        private var instance: UserDatabase? = null
        fun getInstance(context: Context): UserDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context,
                    UserDatabase::class.java, "User.db"
                ).build()
            }
    }
}