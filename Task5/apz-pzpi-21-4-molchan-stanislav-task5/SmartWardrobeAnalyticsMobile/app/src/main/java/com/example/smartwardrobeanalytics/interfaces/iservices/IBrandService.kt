package com.example.smartwardrobeanalytics.interfaces.iservices

import com.example.smartwardrobeanalytics.dtos.BrandDto
import com.example.smartwardrobeanalytics.dtos.BrandResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IBrandService {
    @GET("Brands")
    fun getBrands(@Query("pageNumber") pageNumber: Int, @Query("pageSize") pageSize: Int): Call<BrandResponse>
}
