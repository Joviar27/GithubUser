package com.example.githubuser.data.remote.retrofit

import com.example.githubuser.BuildConfig
import com.example.githubuser.data.remote.response.DetailUserResponse
import com.example.githubuser.data.remote.response.User
import com.example.githubuser.data.remote.response.UserListResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    suspend fun getUserList(
        @Query ("q") q: String
    ) : UserListResponse

    @GET("users/{name}")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    suspend fun getUserDetail(
        @Path ("name") name: String
    ) : DetailUserResponse

    @GET("users/{name}/followers")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    suspend fun getFollowers(
        @Path ("name") name: String
    ) : List<User>

    @GET("users/{name}/following")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    suspend fun getFollowing(
        @Path ("name") name: String
    ) : List<User>

}