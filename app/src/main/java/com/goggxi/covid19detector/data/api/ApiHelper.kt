package com.goggxi.covid19detector.data.api

/**
 * Created By Goggxi on 16/12/2020.
 *
 */
class ApiHelper (private val apiService: ApiService) {

    suspend fun getProvinsi() = apiService.getProvinsi()

//    suspend fun createUser(nama : String, email: String, password: String, image: String) = apiService.createUser(nama, email, password, image)
//
//    suspend fun loginGoogle(code : String) = apiService.loginGoogle(code)
//
//    suspend fun loginUser(loginRequest: LoginRequest) = apiService.loginUser(loginRequest)
//
//    suspend fun logoutUser(token : String) = apiService.logoutUser(token)
//
//    suspend fun getDetailUser(token : String, id : Int) = apiService.getDetailUser(token, id)
//
//    suspend fun updateDetailUser(token : String, id : Int, userRequest: UserRequest) = apiService.updateDetailUser(token, id, userRequest)
}