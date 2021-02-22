package com.goggxi.covid19detector.ui.detailcovidprovince

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.goggxi.covid19detector.R
import org.jetbrains.annotations.NotNull
import java.text.NumberFormat

class DetailCovidActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USIA1 = "usia1"
        const val EXTRA_JUMLAH_USIA1 = "jumlah usia1"
        const val EXTRA_USIA2 = "usia2"
        const val EXTRA_JUMLAH_USIA2 = "jumlah usia2"
        const val EXTRA_USIA3 = "usia3"
        const val EXTRA_JUMLAH_USIA3 = "jumlah usia3"
        const val EXTRA_USIA4 = "usia4"
        const val EXTRA_JUMLAH_USIA4 = "jumlah usia4"
        const val EXTRA_USIA5 = "usia5"
        const val EXTRA_JUMLAH_USIA5 = "jumlah usia5"
        const val EXTRA_USIA6 = "usia6"
        const val EXTRA_JUMLAH_USIA6 = "jumlah usia6"

        const val EXTRA_PRIA = "pria"
        const val EXTRA_JUMLAH_PRIA = "jumlah pria"
        const val EXTRA_WANITA = "wanita"
        const val EXTRA_JUMLAH_WANITA = "jumlah wanita"

        const val EXTRA_PROVINSI = "provinsi"
        const val EXTRA_DATE = "tanggal"
        const val EXTRA_PENAMBAHAN_POSITIF = "penambahan positif"
        const val EXTRA_PENAMBAHAN_MENINGGAL = "penambahan meninggal"
        const val EXTRA_PENAMBAHAN_SEMBUH = "penambahan sembuh"
        const val EXTRA_POSITIF = "positif"
        const val EXTRA_MENINGGAL = "meninggal"
        const val EXTRA_SEMBUH = "sembuh"
        const val EXTRA_TERKONFIRMASI = "terkonfirmasi"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_covid)

        val toolbar: Toolbar = findViewById(R.id.toolbar1)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        val provinsi : TextView = findViewById(R.id.txtProvince)
        provinsi.text = intent.getStringExtra(EXTRA_PROVINSI)
        val tanggal : TextView = findViewById(R.id.txtDateCovidDetail)
        tanggal.text = intent.getStringExtra(EXTRA_DATE)


        chartKelompokUmur()
        totalKasus()
        penambahan()
        charJenisKelamin()
    }

    private fun totalKasus() {
        val positif: TextView = findViewById(R.id.txtJumlahPositif)
        val terkonfirmasi: TextView = findViewById(R.id.txtJumlahTerkonfirmasi)
        val meninggal: TextView = findViewById(R.id.txtJumlahMeninggal)
        val sembuh: TextView = findViewById(R.id.txtJumlahSembuh)

        positif.text = NumberFormat.getInstance().format(intent.getStringExtra(EXTRA_POSITIF)?.toFloat())
        terkonfirmasi.text = NumberFormat.getInstance().format(intent.getStringExtra(EXTRA_TERKONFIRMASI)?.toFloat())
        meninggal.text = NumberFormat.getInstance().format(intent.getStringExtra(EXTRA_MENINGGAL)?.toFloat())
        sembuh.text = NumberFormat.getInstance().format(intent.getStringExtra(EXTRA_SEMBUH)?.toFloat())
    }

    private fun penambahan(){
        val penambahanPositif : TextView = findViewById(R.id.txtJumlahPenambahanPositif)
        val penambahanMeninggal : TextView = findViewById(R.id.txtJumlahPenambahanMeninggal)
        val penambahanSembuh : TextView = findViewById(R.id.txtJumlahPenambahanSembuh)

        penambahanPositif.text = NumberFormat.getInstance().format(intent.getStringExtra(EXTRA_PENAMBAHAN_POSITIF)?.toFloat())
        penambahanMeninggal.text = NumberFormat.getInstance().format(intent.getStringExtra(EXTRA_PENAMBAHAN_MENINGGAL)?.toFloat())
        penambahanSembuh.text = NumberFormat.getInstance().format(intent.getStringExtra(EXTRA_PENAMBAHAN_SEMBUH)?.toFloat())

//        penambahanPositif.text = intent.getStringExtra(NumberFormat.getInstance().format(EXTRA_PENAMBAHAN_POSITIF).toFloat().toString())
//        penambahanMeninggal.text = intent.getStringExtra(NumberFormat.getInstance().format(EXTRA_PENAMBAHAN_MENINGGAL).toFloat().toString())
//        penambahanSembuh.text = intent.getStringExtra(NumberFormat.getInstance().format(EXTRA_PENAMBAHAN_SEMBUH).toFloat().toString())
    }


    private fun charJenisKelamin() {
        val pria = intent.getStringExtra(EXTRA_PRIA)
        val jumlahPria = intent.getStringExtra(EXTRA_JUMLAH_PRIA)
        val wanita = intent.getStringExtra(EXTRA_WANITA)
        val jumlahWanita = intent.getStringExtra(EXTRA_JUMLAH_WANITA)

        val chartJenisKelamin : PieChart = findViewById(R.id.chartJenisKelamin)

        val jenisKelaminEntry  = ArrayList<PieEntry>()
        jenisKelaminEntry.add(PieEntry( jumlahPria!!.toFloat() , "Pria"))
        jenisKelaminEntry.add(PieEntry( jumlahWanita!!.toFloat() , "Wanita"))

        val pieDataSet = PieDataSet(jenisKelaminEntry, "")
        val color1 = applicationContext.let { ContextCompat.getColor(it, R.color.green1) }
        val color2 = applicationContext.let { ContextCompat.getColor(it, R.color.orange1) }
        pieDataSet.colors = mutableListOf(color1, color2)
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.valueTextSize = 11f

        val pieData = PieData(pieDataSet)

        chartJenisKelamin.data = pieData
        chartJenisKelamin.description.isEnabled = false
        chartJenisKelamin.centerText  = "Jenis Kelamin Terdampak Covid-19"
        chartJenisKelamin.animation
    }


    private fun chartKelompokUmur(){
        val usia1 = intent.getStringExtra(EXTRA_USIA1)
        val jumlahUsia1 = intent.getStringExtra(EXTRA_JUMLAH_USIA1)
        val usia2 = intent.getStringExtra(EXTRA_USIA2)
        val jumlahUsia2 = intent.getStringExtra(EXTRA_JUMLAH_USIA2)
        val usia3 = intent.getStringExtra(EXTRA_USIA3)
        val jumlahUsia3 = intent.getStringExtra(EXTRA_JUMLAH_USIA3)
        val usia4 = intent.getStringExtra(EXTRA_USIA4)
        val jumlahUsia4 = intent.getStringExtra(EXTRA_JUMLAH_USIA4)
        val usia5 = intent.getStringExtra(EXTRA_USIA5)
        val jumlahUsia5 = intent.getStringExtra(EXTRA_JUMLAH_USIA5)
        val usia6 = intent.getStringExtra(EXTRA_USIA6)
        val jumlahUsia6 = intent.getStringExtra(EXTRA_JUMLAH_USIA6)

        val chartUsia: PieChart = findViewById(R.id.chartUsia)

        val usiaEntity  = ArrayList<PieEntry>()
        usiaEntity.add(PieEntry( jumlahUsia1!!.toFloat() , usia1))
        usiaEntity.add(PieEntry( jumlahUsia2!!.toFloat() , usia2))
        usiaEntity.add(PieEntry( jumlahUsia3!!.toFloat() , usia3))
        usiaEntity.add(PieEntry( jumlahUsia4!!.toFloat() , usia4))
        usiaEntity.add(PieEntry( jumlahUsia5!!.toFloat() , usia5))
        usiaEntity.add(PieEntry( jumlahUsia6!!.toFloat() , usia6))

        val pieDataSet = PieDataSet(usiaEntity, "")
        val color1 = applicationContext.let { ContextCompat.getColor(it, R.color.orange1) }
        val color2 = applicationContext.let { ContextCompat.getColor(it, R.color.green1) }
        val color3 = applicationContext.let { ContextCompat.getColor(it, R.color.blue1) }
        val color4 = applicationContext.let { ContextCompat.getColor(it, R.color.red1) }
        val color5 = applicationContext.let { ContextCompat.getColor(it, R.color.color3) }
        val color6 = applicationContext.let { ContextCompat.getColor(it, R.color.color5) }

        pieDataSet.colors = mutableListOf(color1, color2, color3, color4, color5, color6)
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.valueTextSize = 16f

        val pieData = PieData(pieDataSet)

        chartUsia.data = pieData
        chartUsia.description.isEnabled = false
        chartUsia.centerText  = "Kelompok Umur Terdampak Covid-19"
        chartUsia.animation

        //        val chartUsia: BarChart = findViewById(R.id.chartUsia)
//
//        val usiaEntity  = ArrayList<BarEntry>()
//        usiaEntity.add(BarEntry(jumlahUsia1!!.toFloat() , 0f))
//        usiaEntity.add(BarEntry(jumlahUsia2!!.toFloat(), 1f ))
//        usiaEntity.add(PieEntry( jumlahUsia3!!.toFloat() , usia3))
//        usiaEntity.add(PieEntry( jumlahUsia4!!.toFloat() , usia4))
//        usiaEntity.add(PieEntry( jumlahUsia5!!.toFloat() , usia5))
//        usiaEntity.add(PieEntry( jumlahUsia6!!.toFloat() , usia6))

//        val pieDataSet = BarDataSet(usiaEntity, "Kelompok Umur")
//        val color1 = applicationContext.let { ContextCompat.getColor(it, R.color.color1) }
//        val color2 = applicationContext.let { ContextCompat.getColor(it, R.color.color2) }
//        val color3 = applicationContext.let { ContextCompat.getColor(it, R.color.color3) }
//        val color4 = applicationContext.let { ContextCompat.getColor(it, R.color.color4) }
//        val color5 = applicationContext.let { ContextCompat.getColor(it, R.color.color5) }
//        val color6 = applicationContext.let { ContextCompat.getColor(it, R.color.color6) }
//
//        pieDataSet.colors = mutableListOf(color1, color2, color3, color4, color5, color6)
//        pieDataSet.valueTextColor = Color.BLACK
//        pieDataSet.valueTextSize = 16f
//
//        val labels = ArrayList<String>()
//        labels.add("18-Jan")
//        labels.add("19-Jan")
//        labels.add("20-Jan")
//        labels.add("21-Jan")
//        labels.add("22-Jan")
//        labels.add("23-Jan")
//        val data = BarData(labels, pieDataSet)
//
////        val pieData = BarData(pieDataSet)
//
//        chartUsia.data = data
//        chartUsia.description.text  = "Statistik Terdampak Covid-19 Berdasarkan Usia"
//        chartUsia.animation
    }

    override fun onOptionsItemSelected(@NotNull item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}