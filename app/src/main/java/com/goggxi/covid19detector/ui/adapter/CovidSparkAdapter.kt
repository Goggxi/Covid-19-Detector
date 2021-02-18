package com.goggxi.covid19detector.ui.adapter

import android.graphics.RectF
import com.goggxi.covid19detector.data.remote.HarianItem
import com.goggxi.covid19detector.utils.Metric
import com.goggxi.covid19detector.utils.TimeScale
import com.robinhood.spark.SparkAdapter

class CovidSparkAdapter(private val daily: List<HarianItem>) : SparkAdapter() {

    var daysAgo = TimeScale.MAX
    var metric = Metric.POSITIVE

    override fun getY(index: Int): Float {
        val chosenDayData = daily[index]
        return when (metric) {
            Metric.TAKECARE -> chosenDayData.jumlahDirawat?.value!!.toFloat()
            Metric.NEGATIVE -> chosenDayData.jumlahSembuh?.value!!.toFloat()
            Metric.POSITIVE -> chosenDayData.jumlahPositif?.value!!.toFloat()
            Metric.DEATH -> chosenDayData.jumlahMeninggal?.value!!.toFloat()
        }
    }

    override fun getItem(index: Int) = daily[index]

    override fun getCount() = daily.size

    override fun getDataBounds(): RectF {
        val bounds = super.getDataBounds()
        if (daysAgo != TimeScale.MAX) {
            bounds.left = count - daysAgo.numDays.toFloat()
        }
        return bounds
    }
}
