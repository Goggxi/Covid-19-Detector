package com.goggxi.covid19detector.ui.classification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import coil.load
import coil.transform.RoundedCornersTransformation
import com.goggxi.covid19detector.R
import com.goggxi.covid19detector.databinding.FragmentClassificationBinding


class ClassificationFragment : Fragment() {
    private lateinit var binding: FragmentClassificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_classification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentClassificationBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)

        binding.expandedImage.load(R.drawable.bg_klasifikasi){
            crossfade(true)
            crossfade(500)
            transformations(RoundedCornersTransformation(0F, 0F, 80F, 80F))
        }
    }

    override fun onStart() {
        super.onStart()
        bottomNavigation()
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