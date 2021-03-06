package com.goggxi.covid19detector.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.goggxi.covid19detector.data.api.ApiHelper
import com.goggxi.covid19detector.data.repository.MainRepository

/**
 * Created By Gogxi on 16/12/2020.
 *
 */

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProvinceViewModel::class.java)) {
            return ProvinceViewModel(MainRepository(apiHelper)) as T
        } else if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(MainRepository(apiHelper)) as T
        } else if (modelClass.isAssignableFrom(NewsDetailViewModel::class.java)) {
            return NewsDetailViewModel(MainRepository(apiHelper)) as T
        } else if (modelClass.isAssignableFrom(ReferralViewModel::class.java)) {
            return ReferralViewModel(MainRepository(apiHelper)) as T
        } else if (modelClass.isAssignableFrom(IndonesiaDetailViewModel::class.java)) {
            return IndonesiaDetailViewModel(MainRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}