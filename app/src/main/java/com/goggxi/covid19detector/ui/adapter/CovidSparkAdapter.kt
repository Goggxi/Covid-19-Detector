package com.goggxi.covid19detector.ui.adapter

import android.util.Half.toFloat
import com.goggxi.covid19detector.data.remote.HarianItem
import com.robinhood.spark.SparkAdapter

class CovidSparkAdapter(private val daily: List<HarianItem>) : SparkAdapter() {

    override fun getY(index: Int): Float {
        val choseDayData = daily[index]
        return choseDayData.jumlahPositif?.value!!.toFloat()
    }

    override fun getItem(index: Int) = daily[index]

    override fun getCount() = daily.size
}
