package com.example.smartwardrobeanalytics.interfaces.iservices

import com.example.smartwardrobeanalytics.dtos.BrandDto
import com.example.smartwardrobeanalytics.dtos.BrandResponse
import com.example.smartwardrobeanalytics.dtos.updateDto.BrandUpdateDto
import com.example.smartwardrobeanalytics.dtos.сreateDto.BrandCreateDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface IBrandService {
    @GET("Brands")
    fun getBrands(@Query("pageNumber") pageNumber: Int, @Query("pageSize") pageSize: Int): Call<BrandResponse>

    @GET("Brands/by-user")
    fun getBrandsByUser(): Call<List<BrandDto>>

    @POST("Brands")
    fun createBrand(@Body brandCreateDto: BrandCreateDto): Call<Unit>

    @PUT("Brands")
    fun updateBrand(@Body brandUpdateDto: BrandUpdateDto): Call<Unit>

    @DELETE("Brands/{id}")
    fun deleteBrand(@Path("id") id: String): Call<Unit>
}
