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

    private var currentPage = 1
    var currentFeature = PhotosApiFeature.POPULAR

    init {
        getPhotos(feature = currentFeature, page = currentPage)
    }

    private fun getPhotos(feature: PhotosApiFeature = PhotosApiFeature.POPULAR, page: Int = 1) {
        coroutineScope.launch {
            // Get the Retrofit request Deferred object...
            var getPropertiesDeferred = PhotosApi.retrofitService.getPhotos(feature = feature.value, page = page)
            try {
                _status.value = PhotosApiStatus.LOADING
                // ... for which we would Await the result.
                var photosApiResponse = getPropertiesDeferred.await()

                if (photosApiResponse.photos.isNotEmpty()) {
                    if (feature != currentFeature || _photos.value.isNullOrEmpty()) {
                        _photos.value = photosApiResponse.photos
                    } else {
                        val existingPhotos = _photos.value!!.toMutableList()
                        existingPhotos.addAll(photosApiResponse.photos)
                        _photos.value = existingPhotos.toList()
                    }
                }
                _status.value = PhotosApiStatus.COMPLETED
                currentPage = page
                currentFeature = feature
            } catch (e: Exception) {
                _status.value = PhotosApiStatus.ERROR
            }
        }
    }

    fun getNextPage() {
        getPhotos(page = ++currentPage)
    }

    fun isLoading(): Boolean {
        return _status == PhotosApiStatus.LOADING
    }

    fun showFeature(feature: PhotosApiFeature) {
        if (feature != currentFeature)
            getPhotos(feature = feature)
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

