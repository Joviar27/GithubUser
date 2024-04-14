package com.example.githubuser.data.remote.retrofit

import com.example.githubuser.data.remote.response.DetailUserResponse
import com.example.githubuser.data.remote.response.UserItemResponse
import com.example.githubuser.data.remote.response.UserListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    suspend fun getGithubUser(): List<UserItemResponse>

    @GET("search/users")
    suspend fun searchUser(
        @Query ("q") q: String
    ) : UserListResponse

    @GET("users/{name}")
    suspend fun getUserDetail(
        @Path ("name") name: String
    ) : DetailUserResponse?

    @GET("users/{name}/followers")
    suspend fun getFollowers(
        @Path ("name") name: String
    ) : List<UserItemResponse>

    @GET("users/{name}/following")
    suspend fun getFollowing(
        @Path ("name") name: String
    ) : List<UserItemResponse>

}