package com.fgsveto.a500pxphotospilot.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fgsveto.a500pxphotospilot.databinding.FragmentDetailBinding

/**
 * This fragment shows the detailed information about a 500px [Photo].
 */
class DetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val application = requireNotNull(activity).application

        val binding = FragmentDetailBinding.inflate(inflater)

        binding.lifecycleOwner = this

        val photo = DetailFragmentArgs.fromBundle(arguments!!).selectedPhoto
        val viewModelFactory = DetailViewModelFactory(photo, application)

        binding.viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)

        return binding.root
    }
}