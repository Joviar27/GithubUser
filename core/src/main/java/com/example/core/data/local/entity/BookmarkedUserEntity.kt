package com.example.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.domain.model.User

@Entity(tableName = "bookmarked_user")
class BookmarkedUserEntity (

    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    val id : Int,

    @field:ColumnInfo(name = "name")
    val name : String? = null,

    @field:ColumnInfo(name= "login")
    val login : String,

    @field:ColumnInfo(name= "location")
    val location : String? = null,

    @field:ColumnInfo(name = "avatar_url")
    val avatar_url : String? = null,

    @field:ColumnInfo(name = "followers")
    val followers : Int? = null,

    @field:ColumnInfo(name = "following")
    val following : Int? = null
)

fun BookmarkedUserEntity?.mapToDomain() =
    User(
        id = this?.id ?: 0,
        login = this?.login ?: "",
        avatar_url = this?.avatar_url,
        followers = this?.followers,
        following = this?.following,
        location = this?.location,
        name = this?.name,
        isBookmarked = true
    )

fun List<BookmarkedUserEntity>.mapToDomain() =
    this.map { it.mapToDomain() }