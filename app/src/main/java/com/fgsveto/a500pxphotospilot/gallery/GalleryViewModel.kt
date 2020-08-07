package com.fgsveto.a500pxphotospilot.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fgsveto.a500pxphotospilot.network.Photo
import com.fgsveto.a500pxphotospilot.network.PhotosApi
import com.fgsveto.a500pxphotospilot.network.PhotosApiCategory
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

    private var filter = FilterHolder()
    private var unfilteredPhotos = _photos.value

    // For navigating to view the details of a selected photo
    private val _navigateToSelectedPhoto = MutableLiveData<Photo>()
    val navigateToSelectedPhoto: LiveData<Photo>
        get() = _navigateToSelectedPhoto

    private var currentPage = 1
    var currentFeature = PhotosApiFeature.POPULAR

    init {
        getPhotos()
    }

    private fun getPhotos(filterOnly: Boolean = false) {
        coroutineScope.launch {
            try {
                if (!filterOnly) {
                    // Get the Retrofit request Deferred object...
                    var getPropertiesDeferred = PhotosApi.retrofitService.getPhotos(
                        feature = currentFeature.value, page = currentPage
                    )
                    _status.value = PhotosApiStatus.LOADING
                    // ... for which we would Await the result.
                    val photosApiResponse = getPropertiesDeferred.await()
                    if (photosApiResponse.photos.isNotEmpty()) {
                        unfilteredPhotos = if (currentPage > 1) {
                            val existingPhotos = unfilteredPhotos?.toMutableList()
                            existingPhotos?.addAll(photosApiResponse.photos)
                            existingPhotos
                        } else {
                            photosApiResponse.photos
                        }
                    }
                }
                _photos.value = if (filter.currentValue == null) {
                    unfilteredPhotos
                } else {
                    unfilteredPhotos!!.filter { photo -> photo.category == filter.currentValue!!.id }
                }
                _status.value = PhotosApiStatus.COMPLETED
            } catch (e: Exception) {
                _status.value = PhotosApiStatus.ERROR
            }
        }
    }

    fun getNextPage() {
        currentPage++
        getPhotos()
    }

    fun isLoading(): Boolean {
        return _status.value == PhotosApiStatus.LOADING
    }

    fun showFeature(feature: PhotosApiFeature) {
        if (feature != currentFeature) {
            currentFeature = feature
            getPhotos()
        }
    }

    fun getFilters(): List<String> {
        return PhotosApiCategory.values().filter { it != PhotosApiCategory.ALL }.map { it.value }
    }

    fun onFilterChanged(filter: String, isChecked: Boolean) {
        if (this.filter.update(filter, isChecked)) {
            getPhotos(filterOnly = true)
        }
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

    private class FilterHolder {
        var currentValue: PhotosApiCategory? = null
            private set

        fun update(changedFilter: String, isChecked: Boolean): Boolean {
            var category = PhotosApiCategory.values().associateBy(PhotosApiCategory::value)[changedFilter]
            if (isChecked) {
                currentValue = category
                return true
            } else if (currentValue == category) {
                currentValue = null
                return true
            }
            return false
        }
    }
}

