package com.example.smartwardrobeanalytics.interfaces.iservices

import com.example.smartwardrobeanalytics.dtos.NotificationDto
import com.example.smartwardrobeanalytics.dtos.updateDto.NotificationUpdateDto
import com.example.smartwardrobeanalytics.dtos.сreateDto.NotificationCreateDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface INotificationService {
    @GET("notifications/get/{id}")
    fun getNotifications(@Path("id") itemId: String): Call<List<NotificationDto>>

    @GET("notifications/get/by-id/{id}")
    fun getNotificationById(@Path("id") notificationId: String): Call<NotificationDto>

    @POST("notifications")
    fun createNotification(@Body notificationCreateDto: NotificationCreateDto): Call<Void>

    @PUT("notifications")
    fun updateNotification(@Body notificationUpdateDto: NotificationUpdateDto): Call<Void>

    @DELETE("notifications/{id}")
    fun deleteNotification(@Path("id") notificationId: String): Call<Void>
}