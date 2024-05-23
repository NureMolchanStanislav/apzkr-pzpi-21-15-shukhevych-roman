package com.example.smartwardrobeanalytics.services

import com.example.smartwardrobeanalytics.dtos.NotificationDto
import com.example.smartwardrobeanalytics.dtos.updateDto.NotificationUpdateDto
import com.example.smartwardrobeanalytics.dtos.сreateDto.NotificationCreateDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.interfaces.iservices.INotificationService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NotificationServiceImpl {

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

    private val apiService = retrofit.create(INotificationService::class.java)

    fun getNotifications(itemId: String, callback: ApiCallback<List<NotificationDto>>) {
        val call = apiService.getNotifications(itemId)

        call.enqueue(object : Callback<List<NotificationDto>> {
            override fun onResponse(call: Call<List<NotificationDto>>, response: Response<List<NotificationDto>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onSuccess(it)
                    } ?: callback.onError("Response body is null")
                } else {
                    callback.onError("Get notifications failed with error code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<NotificationDto>>, t: Throwable) {
                callback.onError("Get notifications request failed: ${t.message}")
            }
        })
    }

    fun getNotificationById(notificationId: String, callback: ApiCallback<NotificationDto>) {
        val call = apiService.getNotificationById(notificationId)

        call.enqueue(object : Callback<NotificationDto> {
            override fun onResponse(call: Call<NotificationDto>, response: Response<NotificationDto>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onSuccess(it)
                    } ?: callback.onError("Response body is null")
                } else {
                    callback.onError("Get notification by ID failed with error code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<NotificationDto>, t: Throwable) {
                callback.onError("Get notification by ID request failed: ${t.message}")
            }
        })
    }

    fun createNotification(notificationCreateDto: NotificationCreateDto, callback: ApiCallback<Unit>) {
        val call = apiService.createNotification(notificationCreateDto)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback.onSuccess(Unit)
                } else {
                    callback.onError("Create notification failed with error code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback.onError("Create notification request failed: ${t.message}")
            }
        })
    }

    fun updateNotification(notificationUpdateDto: NotificationUpdateDto, callback: ApiCallback<Unit>) {
        val call = apiService.updateNotification(notificationUpdateDto)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback.onSuccess(Unit)
                } else {
                    callback.onError("Update notification failed with error code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback.onError("Update notification request failed: ${t.message}")
            }
        })
    }

    fun deleteNotification(notificationId: String, callback: ApiCallback<Unit>) {
        val call = apiService.deleteNotification(notificationId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback.onSuccess(Unit)
                } else {
                    callback.onError("Delete notification failed with error code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback.onError("Delete notification request failed: ${t.message}")
            }
        })
    }
}
