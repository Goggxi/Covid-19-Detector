package com.goggxi.covid19detector.data.remote

import com.google.gson.annotations.SerializedName

data class NewsDetailResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class DataItem(

	@field:SerializedName("tanggal")
	val tanggal: String? = null,

	@field:SerializedName("body")
	val body: String? = null,

	@field:SerializedName("judul")
	val judul: String? = null,

	@field:SerializedName("poster")
	val poster: String? = null
)
