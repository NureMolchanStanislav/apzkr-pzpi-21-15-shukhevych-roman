package com.example.smartwardrobeanalytics.services

import com.example.smartwardrobeanalytics.dtos.BrandDto
import com.example.smartwardrobeanalytics.dtos.CollectionResponse
import com.example.smartwardrobeanalytics.dtos.CollectionDto
import com.example.smartwardrobeanalytics.dtos.updateDto.CollectionUpdateDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.interfaces.iservices.IBrandService
import com.example.smartwardrobeanalytics.interfaces.iservices.ICollectionService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class CollectionServiceImpl {

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

    private val apiService = retrofit.create(ICollectionService::class.java)

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

    fun getCollectionById(id: String, callback: ApiCallback<CollectionDto>) {
        // Виклик API для отримання колекції по ID
        apiService.getCollectionById(id).enqueue(object : Callback<CollectionDto> {
            override fun onResponse(call: Call<CollectionDto>, response: Response<CollectionDto>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onError(response.message())
                }
            }

            override fun onFailure(call: Call<CollectionDto>, t: Throwable) {
                callback.onError(t.message ?: "Unknown error")
            }
        })
    }

    fun updateCollection(collectionUpdateDto: CollectionUpdateDto, callback: ApiCallback<Unit>) {
        // Виклик API для оновлення колекції
        apiService.updateCollection(collectionUpdateDto).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    callback.onSuccess(Unit)
                } else {
                    callback.onError(response.message())
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError(t.message ?: "Unknown error")
            }
        })
    }

    fun deleteCollection(id: String, callback: ApiCallback<Unit>) {
        // Виклик API для видалення колекції
        apiService.deleteCollection(id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    callback.onSuccess(Unit)
                } else {
                    callback.onError(response.message())
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError(t.message ?: "Unknown error")
            }
        })
    }

}