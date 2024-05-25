package com.example.smartwardrobeanalytics.services

import com.example.smartwardrobeanalytics.dtos.BonusDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.interfaces.iservices.IBonusService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class BonusServiceImpl {

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

    private val apiService = retrofit.create(IBonusService::class.java)

    fun getUserBonuses(callback: ApiCallback<List<BonusDto>>) {
        apiService.getUserBonuses().enqueue(object : Callback<List<BonusDto>> {
            override fun onResponse(call: Call<List<BonusDto>>, response: Response<List<BonusDto>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onSuccess(it)
                    } ?: callback.onError("Response body is null")
                } else {
                    callback.onError("Failed to get bonuses: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<BonusDto>>, t: Throwable) {
                callback.onError("Error: ${t.message}")
            }
        })
    }
}
