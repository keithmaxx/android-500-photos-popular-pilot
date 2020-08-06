package com.fgsveto.a500pxphotospilot.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fgsveto.a500pxphotospilot.network.Photo
import com.fgsveto.a500pxphotospilot.network.PhotosApi
import com.fgsveto.a500pxphotospilot.network.PhotosApiFeature
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class PhotosApiStatus { LOADING, ERROR, COMPLETED }

/**
 * Main [ViewModel] attached to the [GalleryFragment].
 */
class GalleryViewModel: ViewModel() {

    // A pair of CoroutineScope and cancelable Job to handle potentially heavy API queries
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    private val _status = MutableLiveData<PhotosApiStatus>()
    val status: LiveData<PhotosApiStatus>
        get() = _status
    private val _photos = MutableLiveData<List<Photo>>()
    val photos: LiveData<List<Photo>>
        get() = _photos

    // For navigating to view the details of a selected photo
    private val _navigateToSelectedPhoto = MutableLiveData<Photo>()
    val navigateToSelectedPhoto: LiveData<Photo>
        get() = _navigateToSelectedPhoto

    init {
        getPhotos(feature = PhotosApiFeature.POPULAR, page = 1)
    }

    private fun getPhotos(feature: PhotosApiFeature = PhotosApiFeature.POPULAR, page: Int = 1) {
        coroutineScope.launch {
            // Get the Retrofit request Deferred object...
            var getPropertiesDeferred = PhotosApi.retrofitService.getPhotos(feature = feature.value, page = page)
            try {
                _status.value = PhotosApiStatus.LOADING
                // ... for which we would Await the result.
                var photosApiResponse = getPropertiesDeferred.await()
                _status.value = PhotosApiStatus.COMPLETED
                if (photosApiResponse.photos.isNotEmpty()) {
                    _photos.value = photosApiResponse.photos
                }
            } catch (e: Exception) {
                _status.value = PhotosApiStatus.ERROR
            }
        }
    }

    fun updateFeature(feature: PhotosApiFeature) {
        getPhotos(feature)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun displayPhotoDetails(photo: Photo) {
        _navigateToSelectedPhoto.value = photo
    }

    fun displayPhotoDetailsCompleted() {
        _navigateToSelectedPhoto.value = null
    }
}

