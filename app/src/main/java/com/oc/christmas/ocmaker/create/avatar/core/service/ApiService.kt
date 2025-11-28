package com.oc.christmas.ocmaker.create.avatar.core.service
import com.oc.christmas.ocmaker.create.avatar.data.model.PartAPI
import retrofit2.Response
import retrofit2.http.GET
interface ApiService {
    @GET("/api/ST190_ChristmasOCMaker")
    suspend fun getAllData(): Response<Map<String, List<PartAPI>>>
}