package com.github.coba.api

import com.github.coba.data.model.DetailUserResponse
import com.github.coba.data.model.User
import com.github.coba.data.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

private const val token: String = "Authorization: token fcffd46d359e6048524a57970de5262d173d46f3"
interface Api {

    @GET("search/users")
    @Headers(token)
    fun getSearchUsers (
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers(token)
    fun getUserDetail (
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers(token)
    fun getUserFollowers (
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers(token)
    fun getUserFollowing (
            @Path("username") username: String
    ): Call<ArrayList<User>>
}
