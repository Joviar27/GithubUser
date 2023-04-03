package com.example.githubuser.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarked_user")
class BookmarkedUserEntity (

    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    val id : Int = 123,

    @field:ColumnInfo(name = "name")
    val name : String? = "default",

    @field:ColumnInfo(name= "login")
    val login : String = "default",

    @field:ColumnInfo(name= "location")
    val location : String? = "default",

    @field:ColumnInfo(name = "avatar_url")
    val avatar_url : String? = "default",

    @field:ColumnInfo(name = "followers")
    val followers : Int = 1,

    @field:ColumnInfo(name = "following")
    val following : Int = 1
)