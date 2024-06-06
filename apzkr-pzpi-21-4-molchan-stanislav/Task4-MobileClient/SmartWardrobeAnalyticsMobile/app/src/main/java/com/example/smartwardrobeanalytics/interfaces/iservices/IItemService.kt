package com.example.smartwardrobeanalytics.interfaces.iservices

import com.example.smartwardrobeanalytics.dtos.ItemDto
import com.example.smartwardrobeanalytics.dtos.updateDto.ItemUpdateDto
import com.example.smartwardrobeanalytics.dtos.сreateDto.ItemCreateDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface IItemService {
    @POST("items")
    fun createItem(@Body itemCreateDto: ItemCreateDto): Call<Void>

    @PUT("items")
    fun updateItem(@Body itemUpdateDto: ItemUpdateDto): Call<Void>

    @GET("items/{id}")
    fun getItemById(@Path("id") itemId: String): Call<ItemDto>

    @GET("items/collection/{collectionId}")
    fun getCollectionItems(@Path("collectionId") collectionId: String): Call<List<ItemDto>>

    @DELETE("items/{id}")
    fun deleteItem(@Path("id") itemId: String): Call<Void>
}
