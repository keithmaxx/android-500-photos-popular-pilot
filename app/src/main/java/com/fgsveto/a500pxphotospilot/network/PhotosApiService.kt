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
private const val CONSUMER_KEY = "<insert_your_consumer_key>"

enum class PhotosApiFeature(val value: String) {
    POPULAR("popular"),
    UPCOMING("upcoming"),
    EDITORS_CHOICE("editors")
}
enum class PhotosApiCategory(val id: Int, val value: String) {
    ALL(-1, "All"),
    UNCATEGORIZED(0, "Uncategorized"),
    PEOPLE(7, "People"),
    LANDSCAPES(8, "Landscapes"),
    CITY_ARCHITECTURE(9, "City and Architecture"),
    ANIMALS(11, "Animals"),
    FASHION(14, "Fashion"),
    NATURE(18, "Nature"),
    FOOD(23, "Food")
}

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
        @Query("feature") feature: String = "popular",
        @Query("page") page: Int = 1
    ): Deferred<PhotosApiResponse>
}

private fun getHeaderMap(): Map<String, String> {
    val headerMap = mutableMapOf<String, String>()
    headerMap["consumer_key"] = CONSUMER_KEY
    return headerMap
}

object PhotosApi {
    val retrofitService : PhotosApiService by lazy {
        retrofit.create(PhotosApiService::class.java)
    }
}