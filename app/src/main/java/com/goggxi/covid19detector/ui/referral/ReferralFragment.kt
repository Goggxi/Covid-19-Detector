package com.goggxi.covid19detector.ui.referral

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.RoundedCornersTransformation
import com.goggxi.covid19detector.R
import com.goggxi.covid19detector.data.api.ApiClient
import com.goggxi.covid19detector.data.api.ApiHelper
import com.goggxi.covid19detector.data.model.Referral
import com.goggxi.covid19detector.databinding.FragmentReferralBinding
import com.goggxi.covid19detector.ui.adapter.ReferralAdapter
import com.goggxi.covid19detector.ui.viewmodel.ReferralViewModel
import com.goggxi.covid19detector.ui.viewmodel.ViewModelFactory
import com.goggxi.covid19detector.utils.Status


class ReferralFragment : Fragment() {
    private lateinit var binding: FragmentReferralBinding
    private lateinit var referralViewModel: ReferralViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_referral, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        binding = FragmentReferralBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)

        binding.recyclerReferral.setHasFixedSize(true)

        binding.expandedImage.load(R.drawable.bg_rujukan){
            crossfade(true)
            crossfade(500)
            transformations(RoundedCornersTransformation(0F, 0F, 80F, 80F))
        }
    }

    override fun onStart() {
        super.onStart()
        bottomNavigation()
        getReferral()
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

    private fun setupViewModel() {
        referralViewModel = ViewModelProviders.of(
                this,
                ViewModelFactory(ApiHelper(ApiClient().getApiServiceReferral()))
        ).get(ReferralViewModel::class.java)
    }

    private fun getReferral() {
        referralViewModel.getReferral().observe(
                viewLifecycleOwner,
                {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                binding.progressBar.visibility = View.GONE
                                if (resource.data?.isSuccessful!!) {
                                    Log.d( "getNews: ", resource.data.body().toString())
                                    showReferral(resource.data.body()!!)
                                } else {
                                    Toast.makeText(context, "Gagal Load Data Rumah Sakit Rujukan", Toast.LENGTH_SHORT).show()
                                }
                            }
                            Status.ERROR -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(context, "Error Memuat Data", Toast.LENGTH_SHORT).show()
                                Log.e("error", it.message.toString())
                            }
                            Status.LOADING -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                        }
                    }
                })
    }

    private fun showReferral(referral: List<Referral>) {
        binding.recyclerReferral.layoutManager = LinearLayoutManager(context)
        val referralAdapter =  ReferralAdapter(referral)
        binding.recyclerReferral.adapter = referralAdapter
    }
}