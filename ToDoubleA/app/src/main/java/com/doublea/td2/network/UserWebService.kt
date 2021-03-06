package com.doublea.td2.network

import android.net.Uri
import com.doublea.td2.authentification.LoginForm
import com.doublea.td2.authentification.LoginResponse
import com.doublea.td2.authentification.SignUpForm
import com.doublea.td2.authentification.SignUpResponse
import com.doublea.td2.network.UserInfo
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserWebService {

    @GET("users/info")
    suspend fun getInfo(): Response<UserInfo>

    @Multipart
    @PATCH("users/update_avatar")
    suspend fun updateAvatar(@Part avatar: MultipartBody.Part): Response<UserInfo>

    @PATCH("users")
    suspend fun update(@Body user: UserInfo): Response<UserInfo>

    @POST("users/login")
    suspend fun login(@Body user: LoginForm): Response<LoginResponse>

    @POST("users/sign_up")
    suspend fun signup(@Body user: SignUpForm): Response<SignUpResponse>
}