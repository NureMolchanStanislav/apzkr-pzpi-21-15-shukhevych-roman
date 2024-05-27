package com.example.smartwardrobeanalytics.interfaces.iservices

import com.example.smartwardrobeanalytics.dtos.CollectionDto
import com.example.smartwardrobeanalytics.dtos.CollectionResponse
import com.example.smartwardrobeanalytics.dtos.updateDto.CollectionUpdateDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ICollectionService {
    @GET("collections")
    fun getCollections(@Query("pageNumber") pageNumber: Int, @Query("pageSize") pageSize: Int): Call<CollectionResponse>

    @GET("Collections/{id}")
    fun getCollectionById(@Path("id") id: String): Call<CollectionDto>

    @PUT("Collections")
    fun updateCollection(@Body collectionUpdateDto: CollectionUpdateDto): Call<Unit>

    @DELETE("Collections/{id}")
    fun deleteCollection(@Path("id") id: String): Call<Unit>
}
