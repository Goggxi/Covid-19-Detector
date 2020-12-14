package com.goggxi.covid19detector.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.RoundedCornersTransformation
import com.goggxi.covid19detector.R


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val expandedImage = view.findViewById<ImageView>(R.id.expandedImage)

        expandedImage.load(R.drawable.bg_home){
            crossfade(true)
            crossfade(500)
            transformations(RoundedCornersTransformation(0F, 0F, 80F, 80F))
        }

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)

        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
        }

        val locationDropdownMenu = view.findViewById<AutoCompleteTextView>(R.id.locationDropdownMenu)
        val location = arrayOf("Sulawesi Selatan", "Sulawesi Tengah", "DKI Jakarta", "Bali", "NTT")
        val locationAdapter = ArrayAdapter(requireContext(), R.layout.location_dropdown_item, location)
        locationDropdownMenu.setAdapter(locationAdapter)
    }
}