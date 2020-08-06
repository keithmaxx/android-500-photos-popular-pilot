package com.fgsveto.a500pxphotospilot.gallery

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fgsveto.a500pxphotospilot.databinding.FragmentGalleryBinding
import com.fgsveto.a500pxphotospilot.network.PhotoGridAdapter

/**
 * This fragment is the landing screen for our 500px Photos API pilot app.
 */
class GalleryFragment : Fragment() {

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

        viewModel.navigateToSelectedPhoto.observe(viewLifecycleOwner, Observer { photo ->
            if ( photo != null ) {
                this.findNavController().navigate(GalleryFragmentDirections.actionShowDetail(photo))
                viewModel.displayPhotoDetailsCompleted()
            }
        })

        return binding.root
    }
}
