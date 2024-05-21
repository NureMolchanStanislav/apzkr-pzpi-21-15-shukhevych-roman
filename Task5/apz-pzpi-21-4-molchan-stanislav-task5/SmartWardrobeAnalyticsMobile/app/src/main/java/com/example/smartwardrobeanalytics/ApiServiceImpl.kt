package com.example.smartwardrobeanalytics

import com.example.smartwardrobeanalytics.dtos.CollectionDto
import com.example.smartwardrobeanalytics.dtos.CollectionResponse
import com.example.smartwardrobeanalytics.dtos.LoginUserDto
import com.example.smartwardrobeanalytics.dtos.TokensModel
import com.example.smartwardrobeanalytics.dtos.User
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.example.smartwardrobeanalytics.dtos.*


class ApiServiceImpl {

    interface ApiCallback<T> {
        fun onSuccess(result: T)
        fun onError(error: String)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.50.234:5002/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private val apiService = retrofit.create(IApiService::class.java)

    fun loginUser(email: String, password: String, callback: ApiCallback<TokensModel>) {
        val loginRequest = LoginUserDto(email, password)
        val call = apiService.login(loginRequest)

        call.enqueue(object : Callback<TokensModel> {
            override fun onResponse(call: Call<TokensModel>, response: Response<TokensModel>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onSuccess(it)
                    } ?: callback.onError("Response body is null")
                } else {
                    callback.onError("Login failed with error code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<TokensModel>, t: Throwable) {
                callback.onError("Login request failed: ${t.message}")
            }
        })
    }

    fun getUser(email: String, callback: ApiCallback<User>) {
        val call = apiService.getUser(email)

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onSuccess(it)
                    } ?: callback.onError("Response body is null")
                } else {
                    callback.onError("Get user failed with error code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                callback.onError("Get user request failed: ${t.message}")
            }
        })
    }

    fun getCollections(pageNumber: Int, pageSize: Int, callback: ApiCallback<List<CollectionDto>>) {
        val call = apiService.getCollections(pageNumber, pageSize)

        call.enqueue(object : Callback<CollectionResponse> {
            override fun onResponse(call: Call<CollectionResponse>, response: Response<CollectionResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onSuccess(it.items)
                    } ?: callback.onError("Response body is null")
                } else {
                    callback.onError("Get collections failed with error code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<CollectionResponse>, t: Throwable) {
                callback.onError("Get collections request failed: ${t.message}")
            }
        })
    }

    fun getCollectionItems(collectionId: String, callback: ApiCallback<List<ItemDto>>) {
        val call = apiService.getCollectionItems(collectionId)

        call.enqueue(object : Callback<List<ItemDto>> {
            override fun onResponse(call: Call<List<ItemDto>>, response: Response<List<ItemDto>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onSuccess(it)
                    } ?: callback.onError("Response body is null")
                } else {
                    callback.onError("Get collection items failed with error code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<ItemDto>>, t: Throwable) {
                callback.onError("Get collection items request failed: ${t.message}")
            }
        })
    }
    fun getItemStatistics(itemId: String, months: Int, callback: ApiCallback<List<StatisticDto>>) {
        val call = apiService.getItemStatistics(itemId, months)

        call.enqueue(object : Callback<List<StatisticDto>> {
            override fun onResponse(call: Call<List<StatisticDto>>, response: Response<List<StatisticDto>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onSuccess(it)
                    } ?: callback.onError("Response body is null")
                } else {
                    callback.onError("Get item statistics failed with error code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<StatisticDto>>, t: Throwable) {
                callback.onError("Get item statistics request failed: ${t.message}")
            }
        })
    }

    fun getItemUsages(itemId: String, callback: ApiCallback<List<UsageDto>>) {
        val call = apiService.getItemUsages(itemId)

        call.enqueue(object : Callback<List<UsageDto>> {
            override fun onResponse(call: Call<List<UsageDto>>, response: Response<List<UsageDto>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onSuccess(it)
                    } ?: callback.onError("Response body is null")
                } else {
                    callback.onError("Get item usages failed with error code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<UsageDto>>, t: Throwable) {
                callback.onError("Get item usages request failed: ${t.message}")
            }
        })
    }
}