package com.example.githubuser.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user")
class UserEntity (
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    val id : Int = 123,

    @field:ColumnInfo(name = "name")
    val name : String? = "N/A",

    @field:ColumnInfo(name= "login")
    val login : String = "default",

    @field:ColumnInfo(name= "location")
    val location : String? = "N/A",

    @field:ColumnInfo(name = "avatar_url")
    val avatar_url : String? = "default",

    @field:ColumnInfo(name = "followers")
    val followers : Int = 0,

    @field:ColumnInfo(name = "following")
    val following : Int = 0,

    @field:ColumnInfo(name = "bookmarked")
    var isBookmarked: Boolean = false
) : Parcelable
