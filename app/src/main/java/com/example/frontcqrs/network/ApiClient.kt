package com.example.frontcqrs.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private var retrofitService1: Retrofit? = null
    private var retrofitService2: Retrofit? = null

    // Servicio para el primer backend (por ejemplo, en puerto 3000)
    fun getService1(): Retrofit {
        if (retrofitService1 == null) {
            // Configurar interceptor para logs
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            retrofitService1 = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")  // URL del backend en puerto 3000
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofitService1!!
    }

    // Servicio para el segundo backend (por ejemplo, en puerto 3001)
    fun getService2(): Retrofit {
        if (retrofitService2 == null) {
            // Configurar interceptor para logs
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            retrofitService2 = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3001/")  // URL del backend en puerto 3001
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofitService2!!
    }
}
