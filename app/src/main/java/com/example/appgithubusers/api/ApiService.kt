package com.example.appgithubusers.api

import com.example.appgithubusers.model.ItemsItem
import com.example.appgithubusers.model.ResponseDetail
import com.example.appgithubusers.model.ResponseUser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    fun getUsers(
        @Query("q") username: String,
    ): Call<ResponseUser>

    @GET("users/{username}")
    fun getUsersDetail(
        @Path("username") username: String,
    ): Call<ResponseDetail>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String,
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String,
    ): Call<List<ItemsItem>>

}