package com.goggxi.covid19detector.ui.news

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.text.Layout.JUSTIFICATION_MODE_INTER_WORD
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import coil.load
import coil.transform.RoundedCornersTransformation
import com.codesgood.views.JustifiedTextView
import com.goggxi.covid19detector.R
import com.goggxi.covid19detector.data.api.ApiClient
import com.goggxi.covid19detector.data.api.ApiHelper
import com.goggxi.covid19detector.data.remote.DataItem
import com.goggxi.covid19detector.ui.viewmodel.NewsDetailViewModel
import com.goggxi.covid19detector.ui.viewmodel.ViewModelFactory
import com.goggxi.covid19detector.utils.Status
import org.jetbrains.annotations.NotNull


class NewsDetailActivity : AppCompatActivity() {
    private lateinit var newsDetailViewModel: NewsDetailViewModel

    companion object {
        const val EXTRA_NEWS_DETAIL = "news_detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        val url = intent.getStringExtra(EXTRA_NEWS_DETAIL)

        Log.e("Result NEWS DETAIL", url!! )
        setupViewModel()
        getNewsDetail(url)
    }

    override fun onOptionsItemSelected(@NotNull item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViewModel() {
        newsDetailViewModel = ViewModelProviders.of(
                this,
                ViewModelFactory(ApiHelper(ApiClient().getApiServiceNews()))
        ).get(NewsDetailViewModel::class.java)
    }

    private fun getNewsDetail(url: String){
        val progressBar: ProgressBar = findViewById(R.id.progressBar)

        newsDetailViewModel.getNewsDetail(url).observe(

                this,
                {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                progressBar.visibility = View.GONE
                                if (resource.data?.isSuccessful!!) {
                                    Log.d("getNewsDetails: ", resource.data.body()?.data.toString())
                                    @Suppress("UNCHECKED_CAST")
                                    showNewsDetail(resource.data.body()?.data as List<DataItem>)
                                } else {
                                    Log.e("Error NEWS DETAIL", url )
                                    Toast.makeText(this, "Gagal Load Data Detail Berita", Toast.LENGTH_SHORT).show()
                                }
                            }
                            Status.ERROR -> {
                                progressBar.visibility = View.GONE
                                Toast.makeText(this, "Error Memuat Data Detail Berita", Toast.LENGTH_SHORT).show()
                                Log.e("error", it.message.toString())
                            }
                            Status.LOADING -> {
                                progressBar.visibility = View.VISIBLE
                            }
                        }
                    }
                })
    }

    @SuppressLint("WrongConstant")
    private fun showNewsDetail(newsDetail: List<DataItem>){
        val txtBlank: TextView = findViewById(R.id.txtBlank)
        val txtTitle: TextView = findViewById(R.id.txtTitleNewsDetail)
        val txtDate: TextView = findViewById(R.id.txtDateNewsDetail)
        val txtBody: JustifiedTextView = findViewById(R.id.txtBodyNewsDetail)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            txtBody.justificationMode = JUSTIFICATION_MODE_INTER_WORD
        }
        val imgNewsDetail: ImageView = findViewById(R.id.imgNewsDetail)

        for (detail in newsDetail){
            Log.d("showNewsDetail: ", detail.judul.toString())

            if(detail.judul == null ) {
                Log.d("showNewsDetail", detail.judul.toString())
                txtBlank.text = "Data Kosong"
            } else {
                txtTitle.text = Html.fromHtml(detail.judul)
                txtDate.text = Html.fromHtml(detail.tanggal)
                txtBody.text = Html.fromHtml(detail.body)
                imgNewsDetail.load(detail.poster){
                    crossfade(true)
                    crossfade(500)
                    transformations(RoundedCornersTransformation(5F, 5F, 5F, 5F))
                }
            }
        }
    }
}