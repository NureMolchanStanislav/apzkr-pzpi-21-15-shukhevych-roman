package com.example.smartwardrobeanalytics.services

import com.example.smartwardrobeanalytics.dtos.TagDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.interfaces.iservices.ITagService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class TagServiceImpl {

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

    private val apiService = retrofit.create(ITagService::class.java)

    fun getTags(callback: ApiCallback<List<TagDto>>) {
        apiService.getTags().enqueue(object : Callback<List<TagDto>> {
            override fun onResponse(call: Call<List<TagDto>>, response: Response<List<TagDto>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onSuccess(it)
                    } ?: callback.onError("Response body is null")
                } else {
                    callback.onError("Failed to get tags: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<TagDto>>, t: Throwable) {
                callback.onError("Error: ${t.message}")
            }
        })
    }

    fun updateTag(tagId: String, itemId: String, callback: ApiCallback<Unit>) {
        apiService.updateTag(tagId, itemId).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    callback.onSuccess(Unit)
                } else {
                    callback.onError("Failed to update tag: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError("Error: ${t.message}")
            }
        })
    }
}
