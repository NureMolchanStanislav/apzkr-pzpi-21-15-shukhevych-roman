package com.example.smartwardrobeanalytics.interfaces.iservices

import com.example.smartwardrobeanalytics.dtos.TagDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ITagService {
    @GET("RFIDTags")
    fun getTags(): Call<List<TagDto>>

    @POST("RFIDTags/update/{tagId}/{itemId}")
    fun updateTag(@Path("tagId") tagId: String, @Path("itemId") itemId: String): Call<Unit>
}
