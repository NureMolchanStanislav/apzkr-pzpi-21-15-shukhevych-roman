package com.example.smartwardrobeanalytics.interfaces.iservices

import com.example.smartwardrobeanalytics.dtos.BonusDto
import retrofit2.Call
import retrofit2.http.GET

interface IBonusService {
    @GET("Offers/user-offer")
    fun getUserBonuses(): Call<List<BonusDto>>
}
