package com.goggxi.covid19detector.data.api

import com.goggxi.covid19detector.data.model.Referral
import com.goggxi.covid19detector.data.remote.GrapResponse
import com.goggxi.covid19detector.data.remote.NewsDetailResponse
import com.goggxi.covid19detector.data.remote.NewsResponse
import com.goggxi.covid19detector.data.remote.ProvinceResponse
import com.goggxi.covid19detector.utils.Constants.INDONESIA_DETAIL_URL
import com.goggxi.covid19detector.utils.Constants.NEWS_DETAIL_URL
import com.goggxi.covid19detector.utils.Constants.NEWS_URL
import com.goggxi.covid19detector.utils.Constants.PROVINCE_URL
import com.goggxi.covid19detector.utils.Constants.REFERRAL_URL
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created By Goggxi on 16/12/2020.
 *
 */
interface ApiService {

    @GET(PROVINCE_URL)
    suspend fun getProvince(
    ): Response<ProvinceResponse>

    @GET(INDONESIA_DETAIL_URL)
    suspend fun getIndonesiaDetail(
    ): Response<GrapResponse>

    @GET(NEWS_URL)
    suspend fun getNews(
    ): Response<NewsResponse>

    @GET("/detail/")
    suspend fun getNewsDetail(
        @Query("url") link: String
    ): Response<NewsDetailResponse>

    @GET(REFERRAL_URL)
    suspend fun getReferral(
    ): Response<List<Referral>>

//    @GET(PROVINSI_URL)
//    suspend fun getProvinsi(
//    ): Response<List<Provinsi>>

//    @FormUrlEncoded
//    @POST(REGISTER_URL)
//    suspend fun createUser(
//        @Field("nama") nama: String?,
//        @Field("email") email: String?,
//        @Field("password") password: String?,
//        @Field("image") image: String?
//    ): Response<UserResponse>
//
//    @POST(LOGIN_URL)
//    suspend fun loginUser(
//        @Body loginRequest: LoginRequest
//    ): Response<UserResponse>
//
//    @GET(LOGIN_GOOGLE_URL)
//    suspend fun loginGoogle(
//        @Query("code") code: String?,
//    ): Response<UserResponse>
//
//    @GET(LOGOUT_URL)
//    suspend fun logoutUser(
//        @Header("Authorization") token: String
//    ): Response<LogoutResponse>
//
//    @GET(USER_URL)
//    suspend fun getDetailUser(
//        @Header("Authorization") token: String,
//        @Path("id") id : Int
//    ): Response<UserResponse>
//
//    @PUT(USER_URL)
//    suspend fun updateDetailUser(
//        @Header("Authorization") token: String,
//        @Path("id") id : Int,
//        @Body userRequest: UserRequest
//    ): Response<UserResponse>
}