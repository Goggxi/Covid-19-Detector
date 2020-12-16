package com.goggxi.covid19detector.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.RoundedCornersTransformation
import com.goggxi.covid19detector.R
import com.goggxi.covid19detector.data.model.News
import com.goggxi.covid19detector.databinding.FragmentHomeBinding
import com.goggxi.covid19detector.ui.adapter.ListNewsAdapter


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val list = ArrayList<News>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)

        binding.recyclerNews.setHasFixedSize(true)

        binding.expandedImage.load(R.drawable.bg_home){
            crossfade(true)
            crossfade(500)
            transformations(RoundedCornersTransformation(0F, 0F, 80F, 80F))
        }

        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        }

        val location = arrayOf(
            "Total Di Indonesia",
            "ACEH",
            "BALI",
            "BANTEN",
            "BENGKULU",
            "DKI JAKARTA",
            "DAERAH ISTIMEWA YOGYAKARTA",
            "GORONTALO",
            "JAWA TIMUR",
            "JAWA TENGAH",
            "JAWA BARAT",
            "JAMBI",
            "KALIMANTAN TIMUR",
            "KALIMANTAN TENGAH",
            "KALIMANTAN BARAT",
            "KALIMANTAN UTARA",
            "KALIMANTAN SELATAN",
            "KEPULAUAN RIAU",
            "KEPULAUAN BANGKA BELITUNG",
            "LAMPUNG",
            "MALUKU",
            "MALUKU UTARA",
            "NUSA TENGGARA TIMUR",
            "NUSA TENGGARA BARAT",
            "PAPUA",
            "PAPUA BARAT",
            "RIAU",
            "SULAWESI SELATAN",
            "SUMATERA BARAT",
            "SUMATERA UTARA",
            "SUMATERA SELATAN",
            "SULAWESI TENGAH",
            "SULAWESI BARAT",
            "SULAWESI UTARA",
            "SULAWESI TENGGARA"
        )
        val locationAdapter = ArrayAdapter(
            requireContext(),
            R.layout.location_dropdown_item,
            location
        )
        binding.locationDropdownMenu.setAdapter(locationAdapter)


        // Nav host fragment
        val host: NavHostFragment = activity?.supportFragmentManager
            ?.findFragmentById(R.id.container) as NavHostFragment?
            ?: return
        // nav controller
        val navController = host.navController

        // Setup bottom navigation view
        binding.bottomNavigationId.setupWithNavController(navController)

        //Setup News
        list.addAll(getListNews())
        showRecyclerList()
    }


    private fun getListNews(): ArrayList<News> {
        val title = resources.getStringArray(R.array.dataTitle)
        val type = resources.getStringArray(R.array.dataType)
        val time = resources.getStringArray(R.array.dataTime)
        val dataPhoto = resources.getStringArray(R.array.dataPhoto)
        val listNews = ArrayList<News>()
        for (position in title.indices) {
            val news = News(
                title[position],
                type[position],
                time[position],
                dataPhoto[position]
            )
            listNews.add(news)
        }
        return listNews
    }

    private fun showRecyclerList() {
        binding.recyclerNews.layoutManager = LinearLayoutManager(context)
        val listNewsAdapter = ListNewsAdapter(list)
        binding.recyclerNews.adapter = listNewsAdapter
    }
}