package com.fgsveto.a500pxphotospilot.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fgsveto.a500pxphotospilot.network.PhotosApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Main [ViewModel] attached to the [GalleryFragment].
 */
class GalleryViewModel: ViewModel() {

    // The most recent response
    private val _response = MutableLiveData<String>()
    // The external immutable LiveData for the response String
    val response: LiveData<String>
        get() = _response

    // A pair of CoroutineScope and cancelable Job to handle potentially heavy API queries
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    init {
        getPhotos()
    }

    private fun getPhotos(page: Int = 1) {
        coroutineScope.launch {
            // Get the Retrofit request Deferred object...
            var getPropertiesDeferred = PhotosApi.retrofitService.getPhotos(page = page)
            try {
                // ... for which we would Await the result.
                var photosApiResponse = getPropertiesDeferred.await()
                _response.value = "Success: page ${photosApiResponse.currentPage}" +
                        "\n${photosApiResponse.totalPages} total pages" +
                        "\n${photosApiResponse.totalItems} total items" +
                        "\n${photosApiResponse.photos.size} photos retrieved"
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

