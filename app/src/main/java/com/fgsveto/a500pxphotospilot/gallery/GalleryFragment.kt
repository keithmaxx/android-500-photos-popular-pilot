package com.fgsveto.a500pxphotospilot.gallery

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fgsveto.a500pxphotospilot.R
import com.fgsveto.a500pxphotospilot.databinding.FragmentGalleryBinding
import com.fgsveto.a500pxphotospilot.network.PhotoGridAdapter
import com.fgsveto.a500pxphotospilot.network.PhotosApiFeature

/**
 * This fragment is the landing screen for our 500px Photos API pilot app.
 */
class GalleryFragment : Fragment() {

    // These are for tracking when we've reached the bottom of the page
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val lastVisibleItemPosition: Int
        get() = linearLayoutManager.findLastVisibleItemPosition()

    /**
     * Lazily initialize our [GalleryViewModel].
     */
    private val viewModel: GalleryViewModel by lazy {
        ViewModelProvider(this).get(GalleryViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentGalleryBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.photosGrid.adapter = PhotoGridAdapter(PhotoGridAdapter.OnClickListener { photo ->
            viewModel.displayPhotoDetails(photo)
        })

        linearLayoutManager = binding.photosGrid.layoutManager as LinearLayoutManager
        binding.photosGrid.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                // Request the next page of photos when we've scrolled to the bottom of the page
                val totalItemCount = recyclerView.layoutManager!!.itemCount
                if (!viewModel.isLoading() && totalItemCount == lastVisibleItemPosition + 1) {
                    viewModel.getNextPage()
                }
            }
        })

        viewModel.navigateToSelectedPhoto.observe(viewLifecycleOwner, Observer { photo ->
            if ( photo != null ) {
                this.findNavController().navigate(GalleryFragmentDirections.actionShowDetail(photo))
                viewModel.displayPhotoDetailsCompleted()
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.showFeature(
            when (item.itemId) {
                R.id.show_upcoming -> PhotosApiFeature.UPCOMING
                R.id.show_editors_choice -> PhotosApiFeature.EDITORS_CHOICE
                else -> PhotosApiFeature.POPULAR
            }
        )
        return true
    }
}
