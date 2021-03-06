package com.doublea.td2.network

import android.content.Context
import androidx.preference.PreferenceManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class Api(private val context: Context) {

    companion object {
        // constantes qui serviront à faire les requêtes
        private const val BASE_URL = "https://android-tasks-api.herokuapp.com/api/"
        //private const val TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjozNzIsImV4cCI6MTY0MDYxNTAwN30.vX5YksCywF-QDMb0tISi8fa0y04yFzQJbVyoaNr0zoE"
        lateinit var INSTANCE: Api
    }

    fun getToken() : String? {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(SHARED_PREF_TOKEN_KEY, "")
    }



    // on construit une instance de parseur de JSON:
    private val jsonSerializer = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    // instance de convertisseur qui parse le JSON renvoyé par le serveur:
    private val converterFactory =
            jsonSerializer.asConverterFactory("application/json".toMediaType())

    // client HTTP
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
                .addInterceptor { chain ->
                    // intercepteur qui ajoute le `header` d'authentification avec votre token:
                    val newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer ${getToken()}")
                            .build()
                    chain.proceed(newRequest)
                }
                .build()
    }

    // permettra d'implémenter les services que nous allons créer:
    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()

    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    val userWebService: UserWebService by lazy {
        retrofit.create(UserWebService::class.java)
    }

    val tasksWebService: TaskWebService by lazy {
        retrofit.create(TaskWebService::class.java)
    }
}