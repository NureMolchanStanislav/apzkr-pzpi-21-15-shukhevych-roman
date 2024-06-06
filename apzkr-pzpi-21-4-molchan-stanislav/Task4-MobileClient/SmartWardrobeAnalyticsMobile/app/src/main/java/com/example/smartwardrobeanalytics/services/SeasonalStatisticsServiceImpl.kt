package com.example.smartwardrobeanalytics.services

import com.example.smartwardrobeanalytics.dtos.statistics.SeasonalStatisticDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.interfaces.iservices.IStatisticsService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class SeasonalStatisticsServiceImpl {

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

    fun getSeasonalStatistics(brandId: String, callback: ApiCallback<List<SeasonalStatisticDto>>) {
        apiService.getSeasonalStatistics(brandId).enqueue(object : Callback<List<SeasonalStatisticDto>> {
            override fun onResponse(call: Call<List<SeasonalStatisticDto>>, response: Response<List<SeasonalStatisticDto>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onSuccess(it)
                    } ?: callback.onError("Response body is null")
                } else {
                    callback.onError("Failed to get statistics: ${response.message()} string: ${brandId}")
                }
            }

            override fun onFailure(call: Call<List<SeasonalStatisticDto>>, t: Throwable) {
                callback.onError("Error: ${t.message}")
            }
        })
    }
}
