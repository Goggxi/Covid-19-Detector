package com.goggxi.covid19detector.ui.information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import coil.load
import coil.transform.RoundedCornersTransformation
import com.goggxi.covid19detector.R
import com.goggxi.covid19detector.databinding.FragmentInformationBinding

class InformationFragment : Fragment() {
    private lateinit var binding: FragmentInformationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentInformationBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)

        binding.expandedImage.load(R.drawable.bg_informasi){
            crossfade(true)
            crossfade(500)
            transformations(RoundedCornersTransformation(0F, 0F, 80F, 80F))
        }
    }

    override fun onStart() {
        super.onStart()
        initAction()
    }

    private fun initAction() {
        bottomNavigation()

        binding.btnMengenal.setOnClickListener {
//            Toast.makeText(context, "Mengenal", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_informasiFragment_to_knowCovidFragment)
        }
        binding.btnMencegah.setOnClickListener {
//            Toast.makeText(context, "Mencegah", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_informasiFragment_to_preventCovidFragment)
        }
        binding.btnMengobati.setOnClickListener {
//            Toast.makeText(context, "Mengobati", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_informasiFragment_to_treatCovidFragment)
        }
        binding.btnMengantisipasi.setOnClickListener {
//            Toast.makeText(context, "Mengantisipasi", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_informasiFragment_to_anticipatingCovidFragment)
        }
    }

    private fun bottomNavigation() {
        // Nav host fragment
        val host: NavHostFragment = activity?.supportFragmentManager
                ?.findFragmentById(R.id.container) as NavHostFragment?
                ?: return
        // nav controller
        val navController = host.navController

        // Setup bottom navigation view
        binding.bottomNavigationId.setupWithNavController(navController)
    }
}