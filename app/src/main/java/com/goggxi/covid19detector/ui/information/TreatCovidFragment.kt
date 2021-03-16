package com.goggxi.covid19detector.ui.information

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.RoundedCornersTransformation
import com.goggxi.covid19detector.R
import com.goggxi.covid19detector.databinding.FragmentTreatCovidBinding

class TreatCovidFragment : Fragment() {
    private lateinit var binding: FragmentTreatCovidBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_treat_covid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTreatCovidBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_treatCovidFragment_to_informasiFragment)
            }
        })

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_treatCovidFragment_to_informasiFragment)
        }

        binding.imageView2.load(R.drawable.img_pengnalan_covid){
            crossfade(true)
            crossfade(500)
            transformations(RoundedCornersTransformation(5F, 5F, 5F, 5F))
        }
    }
}