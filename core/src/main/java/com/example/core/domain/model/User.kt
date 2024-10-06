package com.example.core.domain.model

import android.os.Parcelable
import com.example.core.data.local.entity.BookmarkedUserEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    val id : Int,
    val name : String? = null,
    val login : String,
    val location : String? = null,
    val avatar_url : String?,
    val followers : Int? = null,
    val following : Int? = null,
    val isBookmarked: Boolean? = null
) : Parcelable

fun User.mapToBookmarkEntity() =
    BookmarkedUserEntity(
        id = this.id,
        name = this.name,
        login = this.login,
        location = this.location,
        avatar_url = this.avatar_url,
        followers = this.followers,
        following = this.following
    )