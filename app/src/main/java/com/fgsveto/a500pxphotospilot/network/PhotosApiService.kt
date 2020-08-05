package com.fgsveto.a500pxphotospilot.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.HeaderMap

private const val BASE_URL = "https://api.500px.com/"
private const val CONSUMER_KEY = "P7LLhKkPAnPUpbfAXk3Jq2iDjYmCx87zgfEDxQVS"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface PhotosApiService {
    @GET("v1/photos")
    fun getPhotos(
        @HeaderMap headers: Map<String, String> = getHeaderMap()
    ): Call<String>
}

private fun getHeaderMap(): Map<String, String> {
    val headerMap = mutableMapOf<String, String>()
    headerMap["feature"] = "popular"
    headerMap["consumer_key"] = CONSUMER_KEY
    return headerMap
}

object PhotosApi {
    val retrofitService : PhotosApiService by lazy {
        retrofit.create(PhotosApiService::class.java)
    }
}