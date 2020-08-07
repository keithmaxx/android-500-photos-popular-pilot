package com.fgsveto.a500pxphotospilot.detail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fgsveto.a500pxphotospilot.R
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

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_details) {
            // TODO toggle showing/hiding details
        }
        return true
    }
}