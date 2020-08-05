package com.fgsveto.a500pxphotospilot.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fgsveto.a500pxphotospilot.network.PhotosApi
import com.fgsveto.a500pxphotospilot.network.PhotosApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Main [ViewModel] attached to the [GalleryFragment].
 */
class GalleryViewModel: ViewModel() {

    // The most recent response
    private val _response = MutableLiveData<String>()
    // The external immutable LiveData for the response String
    val response: LiveData<String>
        get() = _response

    init {
        getPhotos()
    }

    private fun getPhotos(page: Int = 1) {
        PhotosApi.retrofitService.getPhotos(page = page).enqueue( object: Callback<PhotosApiResponse> {
            override fun onFailure(call: Call<PhotosApiResponse>, t: Throwable) {
                _response.value = "Failure: " + t.message
            }

            override fun onResponse(call: Call<PhotosApiResponse>,
                                    photosApiResponse: Response<PhotosApiResponse>) {
                _response.value = "Success: page ${photosApiResponse.body()?.currentPage}" +
                        "\n${photosApiResponse.body()?.totalPages} total pages" +
                        "\n${photosApiResponse.body()?.totalItems} total items" +
                        "\n${photosApiResponse.body()?.photos?.size} photos retrieved"
            }
        })
    }
}

