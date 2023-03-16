package com.example.githubuser

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getUserList(
        @Query ("q") q: String
    ) : Call<UserListResponse>

    @GET("users/{name}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path ("name") name: String
    ) : Call<DetailUserResponse>

    @GET("users/{name}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path ("name") name: String
    ) : Call<List<User>>

    @GET("users/{name}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path ("name") name: String
    ) : Call<List<User>>

}