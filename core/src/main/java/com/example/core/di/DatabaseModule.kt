package com.example.core.di

import android.content.Context
import androidx.room.Room
import com.example.core.data.local.room.BookmarkedUserDao
import com.example.core.data.local.room.FollowerDao
import com.example.core.data.local.room.FollowingDao
import com.example.core.data.local.room.UserDao
import com.example.core.data.local.room.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): UserDatabase {
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "User.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun userDao(userDatabase: UserDatabase): UserDao = userDatabase.userDao()
    @Provides
    fun bookmarkedUserDao(userDatabase: UserDatabase): BookmarkedUserDao = userDatabase.bookmarkedUserDao()
    @Provides
    fun followerDao(userDatabase: UserDatabase): FollowerDao = userDatabase.followerDao()
    @Provides
    fun followingDao(userDatabase: UserDatabase): FollowingDao = userDatabase.followingDao()
}