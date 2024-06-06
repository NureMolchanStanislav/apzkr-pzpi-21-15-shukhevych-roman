package com.example.smartwardrobeanalytics.interfaces.iservices

import com.example.smartwardrobeanalytics.dtos.UserListResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface IUserService {
    @GET("users")
    fun getUsers(@Query("pageNumber") pageNumber: Int, @Query("pageSize") pageSize: Int): Call<UserListResponse>

    @DELETE("users/ban/{id}")
    fun banUser(@Path("id") userId: String): Call<Unit>

    @POST("users/unban/{id}")
    fun unbanUser(@Path("id") userId: String): Call<Unit>
}
