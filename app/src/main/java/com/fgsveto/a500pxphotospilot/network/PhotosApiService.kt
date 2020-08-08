package com.fgsveto.a500pxphotospilot.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Deferred
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
    UNCATEGORIZED(0, "Uncategorized"),
    CELEBRITIES(1, "Celebrities"),
    FILM(2, "Film"),
    JOURNALISM(3, "Journalism"),
    NUDE(4, "Nude"),
    BLACK_AND_WHITE(5, "Black and White"),
    STILL_LIFE(6, "Still Life"),
    PEOPLE(7, "People"),
    LANDSCAPES(8, "Landscapes"),
    CITY_ARCHITECTURE(9, "City and Architecture"),
    ABSTRACT(10, "Abstract"),
    ANIMALS(11, "Animals"),
    MACRO(12, "Macro"),
    TRAVEL(13, "Travel"),
    FASHION(14, "Fashion"),
    COMMERCIAL(15, "Commercial"),
    CONCERT(16, "Concert"),
    SPORT(17, "Sport"),
    NATURE(18, "Nature"),
    PERFORMING_ARTS(19, "Performing Arts"),
    FAMILY(20, "Family"),
    STREET(21, "Street"),
    UNDERWATER(22, "Underwater"),
    FOOD(23, "Food"),
    FINE_ART(24, "Fine Art"),
    WEDDING(25, "Wedding"),
    TRANSPORTATION(26, "Transportation"),
    URBAN_EXPLORATION(27, "Urban Exploration"),
    AERIAL(29, "Aerial"),
    NIGHT(30, "Night")
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