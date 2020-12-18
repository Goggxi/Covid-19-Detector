package com.goggxi.covid19detector.data.remote

import com.goggxi.covid19detector.data.model.News


data class NewsResponse (
     val data: ArrayList<News?>? = null,
     val status: Int? = null
)

