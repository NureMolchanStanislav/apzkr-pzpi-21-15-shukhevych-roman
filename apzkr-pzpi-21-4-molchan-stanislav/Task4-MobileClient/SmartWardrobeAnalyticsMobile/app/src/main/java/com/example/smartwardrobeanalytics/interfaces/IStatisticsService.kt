package com.example.smartwardrobeanalytics.interfaces.iservices

import com.example.smartwardrobeanalytics.dtos.StatisticDto
import com.example.smartwardrobeanalytics.dtos.statistics.PopularItemStatisticDto
import com.example.smartwardrobeanalytics.dtos.statistics.SeasonalStatisticDto
import com.example.smartwardrobeanalytics.dtos.statistics.StatisticComboDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IStatisticsService {
    @GET("Statistics/combo")
    fun getComboStatistics(): Call<List<StatisticComboDto>>

    @GET("Statistics/seasonal-item-usage")
    fun getSeasonalStatistics(@Query("brandId") brandId: String): Call<List<SeasonalStatisticDto>>

    @GET("Statistics/popular-items")
    fun getPopularItemsStatistics(
        @Query("brandId") brandId: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("topCount") topCount: Int
    ): Call<List<PopularItemStatisticDto>>
}