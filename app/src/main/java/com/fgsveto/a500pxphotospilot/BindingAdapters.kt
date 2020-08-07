package com.fgsveto.a500pxphotospilot

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fgsveto.a500pxphotospilot.gallery.PhotosApiStatus
import com.fgsveto.a500pxphotospilot.network.Photo
import com.fgsveto.a500pxphotospilot.gallery.PhotoGridAdapter

/**
 * Change image visibility depending on API status
 */
@BindingAdapter("photosApiStatus")
fun bindStatus(statusImageView: ImageView, status: PhotosApiStatus?) {
    when (status) {
        PhotosApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        PhotosApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        PhotosApiStatus.COMPLETED -> {
            statusImageView.visibility = View.GONE
        }
    }
}

/**
 * Show photo data if available, otherwise hide the [RecyclerView]
 */
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Photo>?) {
    val adapter = recyclerView.adapter as PhotoGridAdapter
    adapter.submitList(data)
}

/**
 * Load an image by URL into an [ImageView] using Glide
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
                .load(imgUri)
                .apply(RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image))
                .into(imgView)
    }
}
