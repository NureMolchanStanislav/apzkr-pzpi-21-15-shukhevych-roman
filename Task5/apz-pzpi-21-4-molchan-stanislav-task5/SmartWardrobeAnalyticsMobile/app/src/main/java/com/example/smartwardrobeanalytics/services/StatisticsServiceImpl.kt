package com.example.smartwardrobeanalytics.services

import com.example.smartwardrobeanalytics.dtos.StatisticDto
import com.example.smartwardrobeanalytics.dtos.statistics.StatisticComboDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.interfaces.iservices.IStatisticsService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class StatisticsServiceImpl {

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

    private val apiService = retrofit.create(IStatisticsService::class.java)

    fun getComboStatistics(callback: ApiCallback<List<StatisticComboDto>>) {
        apiService.getComboStatistics().enqueue(object : Callback<List<StatisticComboDto>> {
            override fun onResponse(call: Call<List<StatisticComboDto>>, response: Response<List<StatisticComboDto>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onSuccess(it)
                    } ?: callback.onError("Response body is null")
                } else {
                    callback.onError("Failed to get statistics: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<StatisticComboDto>>, t: Throwable) {
                callback.onError("Error: ${t.message}")
            }
        })
    }
}
