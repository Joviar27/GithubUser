package com.example.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.domain.model.User

@Entity(tableName = "follower")
class FollowerEntity (
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    val id : Int,

    @field:ColumnInfo(name= "login")
    val login : String,

    @field:ColumnInfo(name = "avatar_url")
    val avatar_url : String? = null,
)

fun FollowerEntity.mapToDomain() =
    User(
        id = this.id,
        login = this.login,
        avatar_url = this.avatar_url,
    )

fun List<FollowerEntity>.mapToDomain() =
    this.map { it.mapToDomain() }