package com.example.githubuser.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.githubuser.domain.User

@Entity(tableName = "following")
class FollowingEntity (
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    val id : Int,

    @field:ColumnInfo(name= "login")
    val login : String,

    @field:ColumnInfo(name = "avatar_url")
    val avatar_url : String? = null,
)

fun FollowingEntity.mapToDomain() =
    User(
        id = this.id,
        login = this.login,
        avatar_url = this.avatar_url,
    )

fun List<FollowingEntity>.mapToDomain() =
    this.map { it.mapToDomain() }