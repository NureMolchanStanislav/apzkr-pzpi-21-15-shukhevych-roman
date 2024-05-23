package com.example.smartwardrobeanalytics;

import com.example.smartwardrobeanalytics.dtos.CollectionDto
import com.example.smartwardrobeanalytics.dtos.CollectionResponse
import com.example.smartwardrobeanalytics.dtos.ItemDto
import com.example.smartwardrobeanalytics.dtos.LoginUserDto;
import com.example.smartwardrobeanalytics.dtos.NotificationDto
import com.example.smartwardrobeanalytics.dtos.StatisticDto
import com.example.smartwardrobeanalytics.dtos.TokensModel;
import com.example.smartwardrobeanalytics.dtos.UsageDto
import com.example.smartwardrobeanalytics.dtos.User
import com.example.smartwardrobeanalytics.dtos.сreateDto.CollectionCreateDto

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET
import retrofit2.http.POST;
import retrofit2.http.Path
import retrofit2.http.Query

interface IApiService {
    @POST("users/login")
    fun login(@Body loginRequest:LoginUserDto): Call<TokensModel>

    @GET("users/get")
    fun getUser(@Query("email") email: String): Call<User>

    @GET("collections")
    fun getCollections(@Query("pageNumber") pageNumber: Int, @Query("pageSize") pageSize: Int): Call<CollectionResponse>

    @GET("items/collection/{collectionId}")
    fun getCollectionItems(@Path("collectionId") collectionId: String): Call<List<ItemDto>>

    @GET("Statistics/item-statistic/{itemId}")
    fun getItemStatistics(@Path("itemId") itemId: String, @Query("months") months: Int): Call<List<StatisticDto>>

    @GET("Statistics/item-usages/{itemId}")
    fun getItemUsages(@Path("itemId") itemId: String): Call<List<UsageDto>>

    @POST("collections")
    fun createCollection(@Body collectionCreateDto: CollectionCreateDto): Call<Void>

    @GET("notifications/get/{id}")
    fun getNotifications(@Path("id") itemId: String): Call<List<NotificationDto>>
}
