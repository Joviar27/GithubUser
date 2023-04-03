package com.example.githubuser.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "follow")
class FollowEntity (
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    val id : Int = 123,

    @field:ColumnInfo(name= "login")
    val login : String = "default",

    @field:ColumnInfo(name = "avatar_url")
    val avatar_url : String? = "default",

    @field:ColumnInfo(name = "type")
    val type : Int = 0
)