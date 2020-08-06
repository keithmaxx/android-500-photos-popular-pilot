package com.fgsveto.a500pxphotospilot.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PhotosApiResponse(@Json(name = "current_page") val currentPage: Long,
                             @Json(name = "total_pages") val totalPages: Long,
                             @Json(name = "total_items") val totalItems: Long,
                             val photos: List<Photo>): Parcelable


/**
 * This data class defines a short-format Photo as described by the 500px Photos API.
 * The property names of this data class are used by Moshi to match the names of values in JSON.
 */
@Parcelize
data class Photo(
    val id: String,
    @Json(name = "created_at") val createdAt: String,
    val privacy: Boolean,
    val width: Int,
    val height: Int,
    val rating: Double,
    @Json(name = "image_format") val imageFormat: String? = "",
    val images: List<Image>,
    val name: String? = "",
    val description: String? = "",
    @Json(name = "taken_at") val takenAt: String? = "",
    @Json(name = "shutter_speed") val shutterSpeed: String? = "",
    @Json(name = "focal_length") val focalLength: String? = "",
    val aperture: String? = "",
    val camera: String? = "",
    val lens: String? = "",
    val iso: String? = "",
    val location: String? = "",
    @Json(name = "comments_count") val commentsCount: Long,
    @Json(name = "votes_count") val votesCount: Long,
    @Json(name = "times_viewed") val timesViewed: Long,
    val feature: String? = "",
    @Json(name = "feature_date") val featureDate: String): Parcelable


@Parcelize
data class Image(
    val format: String,
    val size: Int,
    val url: String,
    @Json(name = "https_url") val httpsUrl: String) : Parcelable