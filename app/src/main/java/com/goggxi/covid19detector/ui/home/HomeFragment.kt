package com.goggxi.covid19detector.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
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
import com.goggxi.covid19detector.data.model.KelompokUmurItem
import com.goggxi.covid19detector.data.model.Province
import com.goggxi.covid19detector.data.remote.HarianItem
import com.goggxi.covid19detector.data.remote.NewsResponse
import com.goggxi.covid19detector.databinding.FragmentHomeBinding
import com.goggxi.covid19detector.ui.adapter.CovidSparkAdapter
import com.goggxi.covid19detector.ui.adapter.NewsAdapter
import com.goggxi.covid19detector.ui.detailcovidprovince.DetailCovidActivity
import com.goggxi.covid19detector.ui.viewmodel.IndonesiaDetailViewModel
import com.goggxi.covid19detector.ui.viewmodel.NewsViewModel
import com.goggxi.covid19detector.ui.viewmodel.ProvinceViewModel
import com.goggxi.covid19detector.ui.viewmodel.ViewModelFactory
import com.goggxi.covid19detector.utils.Metric
import com.goggxi.covid19detector.utils.Status
import com.goggxi.covid19detector.utils.TimeScale
import com.robinhood.ticker.TickerUtils
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var sparkAdapter: CovidSparkAdapter
    private lateinit var binding: FragmentHomeBinding
    private lateinit var provinceViewModel: ProvinceViewModel
    private lateinit var indonesiaDetailViewModel: IndonesiaDetailViewModel
    private lateinit var currentlyShownData: List<HarianItem>
    private lateinit var newsViewModel: NewsViewModel

    private lateinit var usia1 : String
    private lateinit var jumlahUsia1 : String
    private lateinit var usia2 : String
    private lateinit var jumlahUsia2 : String
    private lateinit var usia3 : String
    private lateinit var jumlahUsia3 : String
    private lateinit var usia4 : String
    private lateinit var jumlahUsia4 : String
    private lateinit var usia5 : String
    private lateinit var jumlahUsia5 : String
    private lateinit var usia6 : String
    private lateinit var jumlahUsia6 : String

    private lateinit var pria : String
    private lateinit var jumlahPria : String
    private lateinit var wanita : String
    private lateinit var jumlahWanita : String

    private lateinit var provinsi : String
    private lateinit var tanggal : String
    private lateinit var penambahanPositif : String
    private lateinit var penambahanMeninggal : String
    private lateinit var penambahanSembuh : String
    private lateinit var positif : String
    private lateinit var sembuh : String
    private lateinit var meninggal : String
    private lateinit var terkonfirmasi : String

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
        buttonSelengkapnya()
    }

    private fun buttonSelengkapnya(){
        binding.btnSelengkapnya.setOnClickListener {
            val intent = Intent(requireContext() , DetailCovidActivity::class.java)
            intent.putExtra(DetailCovidActivity.EXTRA_USIA1, usia1)
            intent.putExtra(DetailCovidActivity.EXTRA_JUMLAH_USIA1, jumlahUsia1)
            intent.putExtra(DetailCovidActivity.EXTRA_USIA2, usia2)
            intent.putExtra(DetailCovidActivity.EXTRA_JUMLAH_USIA2, jumlahUsia2)
            intent.putExtra(DetailCovidActivity.EXTRA_USIA3, usia3)
            intent.putExtra(DetailCovidActivity.EXTRA_JUMLAH_USIA3, jumlahUsia3)
            intent.putExtra(DetailCovidActivity.EXTRA_USIA4, usia4)
            intent.putExtra(DetailCovidActivity.EXTRA_JUMLAH_USIA4, jumlahUsia4)
            intent.putExtra(DetailCovidActivity.EXTRA_USIA5, usia5)
            intent.putExtra(DetailCovidActivity.EXTRA_JUMLAH_USIA5, jumlahUsia5)
            intent.putExtra(DetailCovidActivity.EXTRA_USIA6, usia6)
            intent.putExtra(DetailCovidActivity.EXTRA_JUMLAH_USIA6, jumlahUsia6)

            intent.putExtra(DetailCovidActivity.EXTRA_PRIA, pria)
            intent.putExtra(DetailCovidActivity.EXTRA_JUMLAH_PRIA, jumlahPria)
            intent.putExtra(DetailCovidActivity.EXTRA_WANITA, wanita)
            intent.putExtra(DetailCovidActivity.EXTRA_JUMLAH_WANITA, jumlahWanita)

            intent.putExtra(DetailCovidActivity.EXTRA_DATE, tanggal)
            intent.putExtra(DetailCovidActivity.EXTRA_PENAMBAHAN_MENINGGAL, penambahanMeninggal)
            intent.putExtra(DetailCovidActivity.EXTRA_PENAMBAHAN_POSITIF, penambahanPositif)
            intent.putExtra(DetailCovidActivity.EXTRA_PENAMBAHAN_SEMBUH, penambahanSembuh)
            intent.putExtra(DetailCovidActivity.EXTRA_POSITIF, positif)
            intent.putExtra(DetailCovidActivity.EXTRA_MENINGGAL, meninggal)
            intent.putExtra(DetailCovidActivity.EXTRA_TERKONFIRMASI, terkonfirmasi)
            intent.putExtra(DetailCovidActivity.EXTRA_SEMBUH, sembuh)
            intent.putExtra(DetailCovidActivity.EXTRA_PROVINSI, provinsi)


            requireContext().startActivity(intent)
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
                                    @Suppress("UNCHECKED_CAST")
                                    showProvince(resource.data.body()?.list_data!! as List<Province>, key)
                                    tanggal = resource.data.body()?.last_date.toString()
                                    binding.textDateContent.text = tanggal
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
                binding.include.textTerkonfirmasi.text = NumberFormat.getInstance().format(prov.jumlah_dirawat?.toFloat())
                binding.include2.textKasus.text = NumberFormat.getInstance().format(prov.jumlah_kasus?.toFloat())
                binding.include3.textSembuh.text = NumberFormat.getInstance().format(prov.jumlah_sembuh?.toFloat())
                binding.include4.textMeninggal.text = NumberFormat.getInstance().format(prov.jumlah_meninggal?.toFloat())

                terkonfirmasi = prov.jumlah_dirawat.toString()
                positif = prov.jumlah_kasus.toString()
                meninggal = prov.jumlah_meninggal.toString()
                sembuh = prov.jumlah_sembuh.toString()


//                Log.d( "Provinsi", prov.key.toString())
//                Log.d( "Dirawat", prov.jumlah_dirawat.toString())
//                Log.d( "Kasus", prov.jumlah_kasus.toString())
//                Log.d( "Sembuh", prov.jumlah_sembuh.toString())
//                Log.d( "Meninggal", prov.jumlah_meninggal.toString())

                provinsi = prov.key

                pria = prov.jenis_kelamin!![0]?.key.toString()
                jumlahPria = prov.jenis_kelamin[0]?.doc_count.toString()
                wanita = prov.jenis_kelamin!![1]?.key.toString()
                jumlahWanita = prov.jenis_kelamin[1]?.doc_count.toString()

                usia1 = prov.kelompok_umur!![0]?.key.toString()
                jumlahUsia1 = prov.kelompok_umur!![0]?.doc_count.toString()
                usia2 = prov.kelompok_umur!![1]?.key.toString()
                jumlahUsia2 = prov.kelompok_umur!![1]?.doc_count.toString()
                usia3 = prov.kelompok_umur!![2]?.key.toString()
                jumlahUsia3 = prov.kelompok_umur!![2]?.doc_count.toString()
                usia4 = prov.kelompok_umur!![3]?.key.toString()
                jumlahUsia4 = prov.kelompok_umur!![3]?.doc_count.toString()
                usia5 = prov.kelompok_umur!![4]?.key.toString()
                jumlahUsia5 = prov.kelompok_umur!![4]?.doc_count.toString()
                usia6 = prov.kelompok_umur!![5]?.key.toString()
                jumlahUsia6 = prov.kelompok_umur!![5]?.doc_count.toString()

                penambahanPositif = prov.penambahan?.positif.toString()
                penambahanMeninggal = prov.penambahan?.meninggal.toString()
                penambahanSembuh = prov.penambahan?.sembuh.toString()

                Log.e("Kelompok Usia", prov.kelompok_umur.toString())

//                binding.btnSelengkapnya.setOnClickListener {
//                    val intent = Intent(requireContext() , DetailCovidActivity::class.java)
//                    intent.putExtra(DetailCovidActivity.EXTRA_USIA, usia)
//                    requireContext().startActivity(intent)
//                }
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

                                    setupEventListeners()
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

    private fun setupEventListeners() {
        binding.include6.sparkViewGraph.isScrubEnabled = true
        binding.include6.sparkViewGraph.setScrubListener { itemData ->
            if (itemData is HarianItem) {
                updateDisplayWhitData(itemData)
            }
        }

        binding.include6.tickerView.setCharacterLists(TickerUtils.provideNumberList())

        // Respond to radio button selected events
        binding.include6.radioGroupTimeSelection.setOnCheckedChangeListener { _, checkedId ->
            sparkAdapter.daysAgo = when (checkedId) {
                R.id.radioButtonWeek -> TimeScale.WEEK
                R.id.radioButtonMonth -> TimeScale.MONTH
                else -> TimeScale.MAX
            }
            // Display the last day of the metric
            updateDisplayWhitData(currentlyShownData.last())
            sparkAdapter.notifyDataSetChanged()
        }
        binding.include6.radioGroupMetricSelection.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonDirawat -> updateDisplayMetric(Metric.TAKECARE)
                R.id.radioButtonSembuh -> updateDisplayMetric(Metric.NEGATIVE)
                R.id.radioButtonAktif-> updateDisplayMetric(Metric.POSITIVE)
                R.id.radioButtonMeninggal -> updateDisplayMetric(Metric.DEATH)
            }
        }
    }

    private fun updateDisplayMetric(metric: Metric) {
        // Update color of the chart
        @ColorRes val colorRes = when (metric) {
            Metric.TAKECARE -> R.color.blue1
            Metric.NEGATIVE -> R.color.green1
            Metric.POSITIVE -> R.color.orange1
            Metric.DEATH -> R.color.red1
        }
        @ColorInt val colorInt = ContextCompat.getColor(requireContext(), colorRes)
        binding.include6.sparkViewGraph.lineColor = colorInt
        binding.include6.tickerView.textColor = colorInt

        // Update metric on the adapter
        sparkAdapter.metric = metric
        sparkAdapter.notifyDataSetChanged()

        updateDisplayWhitData(currentlyShownData.last())
    }

    private fun showDaily(daily : List<HarianItem>) {
        currentlyShownData = daily

        sparkAdapter = CovidSparkAdapter(daily)
        binding.include6.sparkViewGraph.adapter = sparkAdapter

        binding.include6.radioButtonAktif.isChecked = true
        binding.include6.radioButtonMax.isChecked = true
        val dayLast = daily.last()
        updateDisplayWhitData(dayLast)
    }

    private fun updateDisplayWhitData(dayLast: HarianItem) {

        val numCases = when (sparkAdapter.metric) {
            Metric.TAKECARE -> dayLast.jumlahDirawat?.value!!.toFloat()
            Metric.NEGATIVE -> dayLast.jumlahSembuh?.value!!.toFloat()
            Metric.POSITIVE -> dayLast.jumlahPositif?.value!!.toFloat()
            Metric.DEATH -> dayLast.jumlahMeninggal?.value!!.toFloat()
        }

        val outputDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
        Log.e("showDailyFormat ", outputDateFormat.format(dayLast.keyAsString))
        Log.e("showDailyPositif ", dayLast.jumlahPositif?.value.toString())

        binding.include6.tvDateLabel.text = outputDateFormat.format(dayLast.keyAsString)
        binding.include6.tickerView.setCharacterLists(TickerUtils.provideNumberList());
        binding.include6.tickerView.text = NumberFormat.getInstance().format(numCases)
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
                                Toast.makeText(context, "Error Memuat Data Berita", Toast.LENGTH_SHORT).show()
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