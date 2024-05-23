package com.example.smartwardrobeanalytics.services

import com.example.smartwardrobeanalytics.dtos.BrandDto
import com.example.smartwardrobeanalytics.dtos.BrandResponse
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.interfaces.iservices.IBrandService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class BrandServiceImpl {

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

    private val apiService = retrofit.create(IBrandService::class.java)

    fun getBrands(pageNumber: Int, pageSize: Int, callback: ApiCallback<List<BrandDto>>) {
        val call = apiService.getBrands(pageNumber, pageSize)

        call.enqueue(object : Callback<BrandResponse> {
            override fun onResponse(call: Call<BrandResponse>, response: Response<BrandResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onSuccess(it.items)
                    } ?: callback.onError("Response body is null")
                } else {
                    callback.onError("Get brands failed with error code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<BrandResponse>, t: Throwable) {
                callback.onError("Get brands request failed: ${t.message}")
            }
        })
    }
}