package com.example.smartwardrobeanalytics.interfaces.iservices

import com.example.smartwardrobeanalytics.dtos.CollectionDto
import com.example.smartwardrobeanalytics.dtos.CollectionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ICollectionService {
    @GET("collections")
    fun getCollections(@Query("pageNumber") pageNumber: Int, @Query("pageSize") pageSize: Int): Call<CollectionResponse>
}
