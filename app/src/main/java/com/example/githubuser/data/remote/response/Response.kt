package com.example.githubuser.data.remote.response

import com.example.githubuser.data.local.entity.BookmarkedUserEntity
import com.example.githubuser.data.local.entity.FollowerEntity
import com.example.githubuser.data.local.entity.FollowingEntity
import com.example.githubuser.data.local.entity.UserEntity
import com.google.gson.annotations.SerializedName

data class UserListResponse(

	@field:SerializedName("items")
	val items: List<UserItemResponse>?
)

data class UserItemResponse(

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String?,

	@field:SerializedName("id")
	val id: Int

)

data class DetailUserResponse(

	@field:SerializedName("following_url")
	val followingUrl: String?,

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("followers_url")
	val followersUrl: String?,

	@field:SerializedName("followers")
	val followers: Int?,

	@field:SerializedName("avatar_url")
	val avatarUrl: String?,

	@field:SerializedName("following")
	val following: Int?,

	@field:SerializedName("name")
	val name: String?,

	@field:SerializedName("location")
	val location: String?,

	@field:SerializedName("id")
	val id: Int
)

fun UserItemResponse.mapToEntity(isBookmarked: Boolean) =
	UserEntity(
		id = this.id,
		login = this.login,
		avatar_url = this.avatarUrl,
		isBookmarked = isBookmarked
	)

fun UserItemResponse.mapToFollowerEntity() =
	FollowerEntity(
		id = this.id,
		login = this.login,
		avatar_url = this.avatarUrl,
	)

fun UserItemResponse.mapToFollowingEntity() =
	FollowingEntity(
		id = this.id,
		login = this.login,
		avatar_url = this.avatarUrl,
	)

fun DetailUserResponse.mapToEntity(isBookmarked: Boolean) =
	UserEntity(
		id = this.id,
		login = this.login,
		avatar_url = this.avatarUrl,
		followers = this.followers,
		following = this.following,
		location = this.location,
		name = this.name,
		isBookmarked = isBookmarked
	)

fun DetailUserResponse.mapToBookmarkEntity() =
	BookmarkedUserEntity(
		id = this.id,
		login = this.login,
		avatar_url = this.avatarUrl,
		followers = this.followers,
		following = this.following,
		location = this.location,
		name = this.name
	)
