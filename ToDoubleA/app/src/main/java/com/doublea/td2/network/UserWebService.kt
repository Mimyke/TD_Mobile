package com.doublea.td2.network

import android.net.Uri
import com.doublea.td2.network.UserInfo
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part

interface UserWebService {

    @Multipart
    @PATCH("users/update_avatar")
    suspend fun updateAvatar(@Part avatar: MultipartBody.Part): Response<UserInfo>


}