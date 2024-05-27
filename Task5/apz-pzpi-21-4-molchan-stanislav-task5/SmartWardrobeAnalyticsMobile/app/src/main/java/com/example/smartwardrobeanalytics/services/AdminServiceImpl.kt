package com.example.smartwardrobeanalytics.services


import com.example.smartwardrobeanalytics.dtos.*
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.interfaces.iservices.IUserService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class AdminServiceImpl {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Config.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private val userService = retrofit.create(IUserService::class.java)

    fun getUsers(pageNumber: Int, pageSize: Int, callback: ApiCallback<List<User>>) {
        userService.getUsers(pageNumber, pageSize).enqueue(object : Callback<UserListResponse> {
            override fun onResponse(call: Call<UserListResponse>, response: Response<UserListResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onSuccess(it.items)
                    } ?: callback.onError("Response body is null")
                } else {
                    callback.onError("Failed to get users: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                callback.onError("Error: ${t.message}")
            }
        })
    }

    fun banUser(userId: String, callback: ApiCallback<Unit>) {
        userService.banUser(userId).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    callback.onSuccess(Unit)
                } else {
                    callback.onError("Failed to ban user: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError("Error: ${t.message}")
            }
        })
    }

    fun unbanUser(userId: String, callback: ApiCallback<Unit>) {
        userService.unbanUser(userId).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    callback.onSuccess(Unit)
                } else {
                    callback.onError("Failed to unban user: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError("Error: ${t.message}")
            }
        })
    }
}