package com.fgsveto.a500pxphotospilot.detail

import android.app.Application
import androidx.lifecycle.*
import com.fgsveto.a500pxphotospilot.network.Photo

/**
 *  The [ViewModel] associated with the [DetailFragment], containing information about the selected
 *  [Photo].
 */
class DetailViewModel(photo: Photo, app: Application) : AndroidViewModel(app) {

    private val _selectedPhoto = MutableLiveData<Photo>()
    val selectedPhoto: LiveData<Photo>
        get() = _selectedPhoto

    init {
        _selectedPhoto.value = photo
    }

    val photoUrl = Transformations.map(selectedPhoto) { photo -> photo.images[0].httpsUrl }

    val photoName = Transformations.map(selectedPhoto) { photo -> photo.name }

    val photoDescription = Transformations.map(selectedPhoto) { photo -> photo.description }
}
