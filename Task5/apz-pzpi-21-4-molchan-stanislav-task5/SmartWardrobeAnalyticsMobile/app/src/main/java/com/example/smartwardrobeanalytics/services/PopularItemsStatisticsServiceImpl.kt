package com.example.smartwardrobeanalytics.services

import com.example.smartwardrobeanalytics.dtos.statistics.PopularItemStatisticDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.interfaces.iservices.IStatisticsService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class PopularItemsStatisticsServiceImpl {

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

    private val apiService = retrofit.create(IStatisticsService::class.java)

    fun getPopularItemsStatistics(brandId: String, startDate: String, endDate: String, topCount: Int, callback: ApiCallback<List<PopularItemStatisticDto>>) {
        apiService.getPopularItemsStatistics(brandId, startDate, endDate, topCount).enqueue(object : Callback<List<PopularItemStatisticDto>> {
            override fun onResponse(call: Call<List<PopularItemStatisticDto>>, response: Response<List<PopularItemStatisticDto>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onSuccess(it)
                    } ?: callback.onError("Response body is null")
                } else {
                    callback.onError("Failed to get statistics: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<PopularItemStatisticDto>>, t: Throwable) {
                callback.onError("Error: ${t.message}")
            }
        })
    }
}
