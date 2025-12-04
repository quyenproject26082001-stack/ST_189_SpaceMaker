package com.oc.space.ocmaker.create.core.service
import com.oc.space.ocmaker.create.data.model.PartAPI
import retrofit2.Response
import retrofit2.http.GET
interface ApiService {
    @GET("/api/ST189_SpaceMaker")
    suspend fun getAllData(): Response<Map<String, List<PartAPI>>>
}