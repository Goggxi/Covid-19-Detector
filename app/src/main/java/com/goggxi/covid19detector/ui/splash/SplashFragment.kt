package com.goggxi.covid19detector.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.navigation.fragment.findNavController
import com.goggxi.covid19detector.R

@Suppress("DEPRECATION")
class SplashFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val handler = Handler()
        handler.postDelayed(Runnable {
            findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
        }, 5000L)
    }

}
//
//val intent = Intent(this, MainActivity::class.java)
//startActivity(intent)
//finish()