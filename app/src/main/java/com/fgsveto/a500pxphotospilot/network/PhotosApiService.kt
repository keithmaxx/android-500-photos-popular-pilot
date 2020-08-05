package com.fgsveto.a500pxphotospilot.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query

private const val BASE_URL = "https://api.500px.com/"
private const val CONSUMER_KEY = "P7LLhKkPAnPUpbfAXk3Jq2iDjYmCx87zgfEDxQVS"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface PhotosApiService {
    @GET("v1/photos")
    fun getPhotos(
        @HeaderMap headers: Map<String, String> = getHeaderMap(),
        @Query("page") page: Int = 1
    ): Deferred<PhotosApiResponse>
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