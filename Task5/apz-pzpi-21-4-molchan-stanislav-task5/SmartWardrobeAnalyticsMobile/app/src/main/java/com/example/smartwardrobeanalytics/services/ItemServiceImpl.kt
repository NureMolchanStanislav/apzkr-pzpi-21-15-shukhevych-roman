package com.example.smartwardrobeanalytics.services

import com.example.smartwardrobeanalytics.dtos.ItemDto
import com.example.smartwardrobeanalytics.dtos.updateDto.ItemUpdateDto
import com.example.smartwardrobeanalytics.dtos.сreateDto.ItemCreateDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.interfaces.iservices.IItemService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ItemServiceImpl {

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

    private val apiService = retrofit.create(IItemService::class.java)

    fun createItem(itemCreateDto: ItemCreateDto, callback: ApiCallback<Unit>) {
        val call = apiService.createItem(itemCreateDto)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback.onSuccess(Unit)
                } else {
                    callback.onError("Create item failed with error code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback.onError("Create item request failed: ${t.message}")
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

    fun updateItem(itemUpdateDto: ItemUpdateDto, callback: ApiCallback<Unit>) {
        val call = apiService.updateItem(itemUpdateDto)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback.onSuccess(Unit)
                } else {
                    callback.onError("Update item failed with error code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback.onError("Update item request failed: ${t.message}")
            }
        })
    }

    fun getItemById(itemId: String, callback: ApiCallback<ItemDto>) {
        val call = apiService.getItemById(itemId)

        call.enqueue(object : Callback<ItemDto> {
            override fun onResponse(call: Call<ItemDto>, response: Response<ItemDto>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onSuccess(it)
                    } ?: callback.onError("Response body is null")
                } else {
                    callback.onError("Get item by ID failed with error code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ItemDto>, t: Throwable) {
                callback.onError("Get item by ID request failed: ${t.message}")
            }
        })
    }

    fun deleteItem(itemId: String, callback: ApiCallback<Unit>) {
        val call = apiService.deleteItem(itemId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback.onSuccess(Unit)
                } else {
                    callback.onError("Delete item failed with error code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback.onError("Delete item request failed: ${t.message}")
            }
        })
    }
}
