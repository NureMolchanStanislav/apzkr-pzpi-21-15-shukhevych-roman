package com.example.smartwardrobeanalytics.services

import com.example.smartwardrobeanalytics.dtos.BrandDto
import com.example.smartwardrobeanalytics.dtos.BrandResponse
import com.example.smartwardrobeanalytics.dtos.updateDto.BrandUpdateDto
import com.example.smartwardrobeanalytics.dtos.сreateDto.BrandCreateDto
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
        .baseUrl(Config.BASE_URL)
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

    fun getBrandsByUser(callback: ApiCallback<List<BrandDto>>) {
        apiService.getBrandsByUser().enqueue(object : Callback<List<BrandDto>> {
            override fun onResponse(call: Call<List<BrandDto>>, response: Response<List<BrandDto>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onSuccess(it)
                    } ?: callback.onError("Response body is null")
                } else {
                    callback.onError("Failed to get brands: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<BrandDto>>, t: Throwable) {
                callback.onError("Error: ${t.message}")
            }
        })
    }

    fun createBrand(brandCreateDto: BrandCreateDto, callback: ApiCallback<Unit>) {
        apiService.createBrand(brandCreateDto).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    callback.onSuccess(Unit)
                } else {
                    callback.onError("Failed to create brand: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError("Error: ${t.message}")
            }
        })
    }

    fun updateBrand(brandUpdateDto: BrandUpdateDto, callback: ApiCallback<Unit>) {
        apiService.updateBrand(brandUpdateDto).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    callback.onSuccess(Unit)
                } else {
                    callback.onError("Failed to update brand: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError("Error: ${t.message}")
            }
        })
    }

    fun deleteBrand(id: String, callback: ApiCallback<Unit>) {
        apiService.deleteBrand(id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    callback.onSuccess(Unit)
                } else {
                    callback.onError("Failed to delete brand: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError("Error: ${t.message}")
            }
        })
    }
}