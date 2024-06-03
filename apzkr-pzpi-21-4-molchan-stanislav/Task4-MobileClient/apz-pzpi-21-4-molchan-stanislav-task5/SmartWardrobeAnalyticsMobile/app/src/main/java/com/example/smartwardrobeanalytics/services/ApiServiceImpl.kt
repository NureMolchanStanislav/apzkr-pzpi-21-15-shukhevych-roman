package com.example.smartwardrobeanalytics.services

import com.example.smartwardrobeanalytics.dtos.*
import com.example.smartwardrobeanalytics.dtos.сreateDto.CollectionCreateDto
import com.example.smartwardrobeanalytics.dtos.сreateDto.UserCreateDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.interfaces.iservices.IApiService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiServiceImpl {

    private val notificationService = NotificationServiceImpl()

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

    private val apiService = retrofit.create(IApiService::class.java)

    fun getNotifications(itemId: String, callback: ApiCallback<List<NotificationDto>>) {
        notificationService.getNotifications(itemId, callback)
    }

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

    fun createCollection(collectionCreateDto: CollectionCreateDto, callback: ApiCallback<Unit>) {
        val call = apiService.createCollection(collectionCreateDto)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback.onSuccess(Unit)
                } else {
                    callback.onError("Create collection failed with error code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback.onError("Create collection request failed: ${t.message}")
            }
        })
    }

    fun registerUser(userCreateDto: UserCreateDto, callback: ApiCallback<Unit>) {
        val call = apiService.registerUser(userCreateDto)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback.onSuccess(Unit)
                } else {
                    callback.onError("Registration failed with error code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback.onError("Registration request failed: ${t.message}")
            }
        })
    }
}
