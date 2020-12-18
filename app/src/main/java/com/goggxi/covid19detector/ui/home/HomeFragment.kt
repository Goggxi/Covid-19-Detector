package com.goggxi.covid19detector.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import com.goggxi.covid19detector.data.model.News
import com.goggxi.covid19detector.data.model.Provinsi
import com.goggxi.covid19detector.databinding.FragmentHomeBinding
import com.goggxi.covid19detector.databinding.ShapeTerkonfirmasiBinding
import com.goggxi.covid19detector.ui.adapter.ListNewsAdapter
import com.goggxi.covid19detector.ui.viewmodel.ProvinsiViewModel
import com.goggxi.covid19detector.ui.viewmodel.ViewModelFactory
import com.goggxi.covid19detector.utils.Status


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var provinsiViewModel: ProvinsiViewModel

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
        setupViewModel()

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

        val location = resources.getStringArray(R.array.dataProvinsi)
        val locationAdapter = ArrayAdapter(
            requireContext(),
            R.layout.location_dropdown_item,
            location
        )
        binding.locationDropdownMenu.setAdapter(locationAdapter)
//        binding.spinnerProvinsi

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


        // Set a dismiss listener for auto complete text view
//        binding.locationDropdownMenu.setOnDismissListener {
//            Toast.makeText(context,"Suggestion closed.",Toast.LENGTH_SHORT).show()
//        }


        // Set a click listener for root layout
//        root_layout.setOnClickListener{
//            val text = auto_complete_text_view.text
//            Toast.makeText(applicationContext,"Inputted : $text",Toast.LENGTH_SHORT).show()
//        }


        // Set a focus change listener for auto complete text view
//        binding.locationDropdownMenu.onFocusChangeListener = View.OnFocusChangeListener{
//            view, b ->
//            if(b){
//                // Display the suggestion dropdown on focus
//                binding.locationDropdownMenu.showDropDown()
//            }
//        }

    }

    override fun onStart() {
        super.onStart()
        binding.locationDropdownMenu.setText("SULAWESI SELATAN")
        val locationDefault = "SULAWESI SELATAN"
        getProvinsi(locationDefault)

        binding.locationDropdownMenu.onItemClickListener = AdapterView.OnItemClickListener {
            parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            getProvinsi(selectedItem)
//            Toast.makeText(context,"Selected : $selectedItem",Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupViewModel() {
        provinsiViewModel = ViewModelProviders.of(
                this,
                ViewModelFactory(ApiHelper(ApiClient().getApiService()))
        ).get(ProvinsiViewModel::class.java)
    }

    private fun getProvinsi(key : String) {
        provinsiViewModel.getProvinsi().observe(
                viewLifecycleOwner,
                {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                binding.progressBar.visibility = View.GONE
                                if (resource.data?.isSuccessful!!) {
                                    Log.d("getDate: ", resource.data.body()?.last_date.toString())
                                    Log.d("getProvinsi: ", resource.data.body()?.list_data.toString())
                                    showProvinsi(resource.data.body()?.list_data!! as List<Provinsi>, key)
                                    binding.textDateContent.text = resource.data.body()?.last_date
                                } else {
                                    Toast.makeText(context, "Gagal Load Data Provinsi", Toast.LENGTH_SHORT).show()
                                }
                            }
                            Status.ERROR -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(context, "Gagal Memuat Data", Toast.LENGTH_SHORT).show()
                                Log.e("error", it.message.toString())
                            }
                            Status.LOADING -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                        }
                    }
                })
    }

    private fun showProvinsi(provinsi : List<Provinsi> , key : String) {
        for (prov in provinsi){
            if (prov.key == key){
                binding.include.textTerkonfirmasi.text = prov.jumlah_dirawat.toString()
                binding.include2.textKasus.text = prov.jumlah_kasus.toString()
                binding.include3.textSembuh.text = prov.jumlah_sembuh.toString()
                binding.include4.textMeninggal.text = prov.jumlah_meninggal.toString()

                Log.d( "Provinsi", prov.key.toString())
                Log.d( "Dirawat", prov.jumlah_dirawat.toString())
                Log.d( "Kasus", prov.jumlah_kasus.toString())
                Log.d( "Sembuh", prov.jumlah_sembuh.toString())
                Log.d( "Meninggal", prov.jumlah_meninggal.toString())
            }
        }
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