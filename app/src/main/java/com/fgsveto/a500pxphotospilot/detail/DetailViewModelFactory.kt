package com.fgsveto.a500pxphotospilot.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fgsveto.a500pxphotospilot.network.Photo

/**
 * Simple ViewModel factory that provides the Photo and context to the ViewModel.
 */
class DetailViewModelFactory(
    private val photo: Photo,
    private val application: Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(photo, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
