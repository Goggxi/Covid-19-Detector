package com.goggxi.covid19detector.data.model


data class Province(
		val penambahan: Penambahan? = null,
		val doc_count: Double? = null,
		val lokasi: Lokasi? = null,
		val jumlah_meninggal: Int? = null,
		val kelompok_umur: List<KelompokUmurItem?>? = null,
		val jumlah_kasus: Int? = null,
		val jumlah_sembuh: Int? = null,
		val jenis_kelamin: List<JenisKelaminItem?>? = null,
		val key: String? = null,
		val jumlah_dirawat: Int? = null
)

data class Penambahan(
		val meninggal: Int? = null,
		val positif: Int? = null,
		val sembuh: Int? = null
)

data class JenisKelaminItem(
		val doc_count: Int? = null,
		val key: String? = null
)

data class Lokasi(
		val lon: Double? = null,
		val lat: Double? = null
)

data class KelompokUmurItem(
		val usia: Usia? = null,
		val doc_count: Int? = null,
		val key: String? = null
)

data class Usia(
		val value: Double? = null
)