package com.goggxi.covid19detector.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.goggxi.covid19detector.data.repository.MainRepository
import com.goggxi.covid19detector.utils.Resource
import kotlinx.coroutines.Dispatchers

class NewsDetailViewModel (private val mainRepository: MainRepository) : ViewModel() {
    fun getNewsDetail(url: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getNewsDetail(url)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}