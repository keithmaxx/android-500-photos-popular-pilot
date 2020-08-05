package com.fgsveto.a500pxphotospilot.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fgsveto.a500pxphotospilot.network.Image
import com.fgsveto.a500pxphotospilot.network.Photo
import com.fgsveto.a500pxphotospilot.network.PhotosApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Main [ViewModel] attached to the [GalleryFragment].
 */
class GalleryViewModel: ViewModel() {

    // A pair of CoroutineScope and cancelable Job to handle potentially heavy API queries
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status
    private val _photos = MutableLiveData<List<Photo>>()
    val photos: LiveData<List<Photo>>
        get() = _photos

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
                _status.value = "Success: page ${photosApiResponse.currentPage}" +
                        "\n${photosApiResponse.totalPages} total pages" +
                        "\n${photosApiResponse.totalItems} total items" +
                        "\n${photosApiResponse.photos.size} photos retrieved"
                if (photosApiResponse.photos.isNotEmpty()) {
                    _photos.value = photosApiResponse.photos
                }
            } catch (e: Exception) {
                _status.value = "Failure: ${e.message}"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

