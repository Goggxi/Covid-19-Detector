package com.goggxi.covid19detector.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.goggxi.covid19detector.data.repository.MainRepository
import com.goggxi.covid19detector.utils.Resource
import kotlinx.coroutines.Dispatchers

class ProvinsiViewModel (private val mainRepository: MainRepository) : ViewModel() {
    fun getProvinsi() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getProvinsi()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}