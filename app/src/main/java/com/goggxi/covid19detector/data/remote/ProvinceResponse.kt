package com.goggxi.covid19detector.data.remote

import com.goggxi.covid19detector.data.model.Province


data class ProvinceResponse(
		val list_data: List<Province?>? = null,
		val last_date: String? = null
)
