package com.goggxi.covid19detector.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.goggxi.covid19detector.data.model.Province
import com.goggxi.covid19detector.data.remote.HarianItem
import com.goggxi.covid19detector.data.remote.NewsResponse
import com.goggxi.covid19detector.databinding.FragmentHomeBinding
import com.goggxi.covid19detector.ui.adapter.CovidSparkAdapter
import com.goggxi.covid19detector.ui.adapter.NewsAdapter
import com.goggxi.covid19detector.ui.viewmodel.IndonesiaDetailViewModel
import com.goggxi.covid19detector.ui.viewmodel.NewsViewModel
import com.goggxi.covid19detector.ui.viewmodel.ProvinceViewModel
import com.goggxi.covid19detector.ui.viewmodel.ViewModelFactory
import com.goggxi.covid19detector.utils.Status
import com.robinhood.ticker.TickerUtils
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var provinceViewModel: ProvinceViewModel
    private lateinit var indonesiaDetailViewModel: IndonesiaDetailViewModel
    private lateinit var newsViewModel: NewsViewModel

    companion object {
        const val TAG = "HomeFragment"
    }


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

//        if(activity is AppCompatActivity){
//            (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
//        }

        val location = resources.getStringArray(R.array.dataProvinsi)
        val locationAdapter = ArrayAdapter(
            requireContext(),
            R.layout.location_dropdown_item,
            location
        )
        binding.locationDropdownMenu.setAdapter(locationAdapter)
    }

    override fun onStart() {
        super.onStart()

        if (binding.locationDropdownMenu.text.isEmpty()){
            binding.locationDropdownMenu.setText("SULAWESI SELATAN")
            val locationDefault = "SULAWESI SELATAN"
            getProvince(locationDefault)
        }

        binding.locationDropdownMenu.onItemClickListener = AdapterView.OnItemClickListener {
            parent, _ , position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            getProvince(selectedItem)
        }

        getIndonesiaDetail()
        getNews()
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

    private fun setupViewModel() {
        provinceViewModel = ViewModelProviders.of(
                this,
                ViewModelFactory(ApiHelper(ApiClient().getApiService()))
        ).get(ProvinceViewModel::class.java)

        indonesiaDetailViewModel = ViewModelProviders.of(
                this,
                ViewModelFactory(ApiHelper(ApiClient().getApiService()))
        ).get(IndonesiaDetailViewModel::class.java)

        newsViewModel = ViewModelProviders.of(
                this,
                ViewModelFactory(ApiHelper(ApiClient().getApiServiceNews()))
        ).get(NewsViewModel::class.java)
    }

    private fun getProvince(key : String) {
        provinceViewModel.getProvince().observe(
                viewLifecycleOwner,
                {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                binding.progressBar.visibility = View.GONE
                                if (resource.data?.isSuccessful!!) {
//                                    Log.d("getDate: ", resource.data.body()?.last_date.toString())
//                                    Log.d("getProvinsi: ", resource.data.body()?.list_data.toString())
                                    @Suppress("UNCHECKED_CAST")
                                    showProvince(resource.data.body()?.list_data!! as List<Province>, key)
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

    private fun showProvince(province : List<Province>, key : String) {
        for (prov in province){
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

    private fun getIndonesiaDetail() {
        indonesiaDetailViewModel.getIndonesiaDetail().observe(
                viewLifecycleOwner,
                {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                binding.progressBar.visibility = View.GONE
                                if (resource.data?.isSuccessful!!) {
//                                    Log.e( "INDONESIA DETAIL", resource.data.body()?.update?.harian?.reversed().toString())
                                    @Suppress("UNCHECKED_CAST")
                                    showDaily(resource.data.body()!!.update?.harian as List<HarianItem>)
//                                    showTotal(resource.data.body()!!.update?.total!!)
                                } else {
                                    Toast.makeText(context, "Gagal Load Data grafik", Toast.LENGTH_SHORT).show()
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

    private fun showDaily(daily : List<HarianItem>) {

        val sparkAdapter = CovidSparkAdapter(daily)
        binding.include6.sparkViewGraph.adapter = sparkAdapter

        binding.include6.radioButtonAktif.isChecked = true
        binding.include6.radioButtonMax.isChecked = true
        val dayLast = daily.last()
        updateDisplayWhitData(dayLast)
    }

    private fun updateDisplayWhitData(dayLast: HarianItem) {
        val outputDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)

        Log.e("showDailyFormat ", outputDateFormat.format(dayLast.keyAsString))
        Log.e("showDailyPositif ", dayLast.jumlahPositif?.value.toString())

        binding.include6.tvDateLabel.text = outputDateFormat.format(dayLast.keyAsString)
        binding.include6.tickerView.setCharacterLists(TickerUtils.provideNumberList());
        binding.include6.tickerView.text = NumberFormat.getInstance().format(dayLast.jumlahPositif?.value)
    }


    private fun getNews() {
        newsViewModel.getNews().observe(
                viewLifecycleOwner,
                {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                binding.progressBar.visibility = View.GONE
                                if (resource.data?.isSuccessful!!) {
                                    Log.d( "getNews: ", resource.data.body().toString())
                                    showNews(resource.data.body()!!)
                                } else {
                                    Toast.makeText(context, "Gagal Load Data Berita", Toast.LENGTH_SHORT).show()
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

    private fun showNews(newsResponse: NewsResponse) {
        val data = newsResponse.data
        binding.recyclerNews.layoutManager = LinearLayoutManager(context)
        val newsAdapter =  NewsAdapter(data)
        binding.recyclerNews.adapter = newsAdapter
    }
}