package com.goggxi.covid19detector.data.repository

import com.goggxi.covid19detector.data.api.ApiHelper


/**
 * Created By Gogxi on 16/12/2020.
 *
 */

class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun getProvince() = apiHelper.getProvince()

    suspend fun getNews() = apiHelper.getNews()

    suspend fun getReferral() = apiHelper.getReferral()

//    suspend fun createUser(nama : String, email: String, password: String, image: String) = apiHelper.createUser(nama, email, password, image)
//
//    suspend fun loginGoogle(code : String) = apiHelper.loginGoogle(code)
//
//    suspend fun loginUser(loginRequest: LoginRequest) = apiHelper.loginUser(loginRequest)
//
//    suspend fun logoutUser(token : String) = apiHelper.logoutUser(token)
//
//    suspend fun getDetailUser(token : String, id : Int) = apiHelper.getDetailUser(token, id)
//
//    suspend fun updateDetailUser(token : String, id : Int, userRequest: UserRequest) = apiHelper.updateDetailUser(token, id, userRequest)
}