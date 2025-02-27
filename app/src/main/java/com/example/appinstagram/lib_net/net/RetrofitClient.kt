package com.example.appinstagram.lib_net.net


import com.example.appinstagram.lib_net.interceptor.CommonInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitClient private constructor() {

    private var retrofit: Retrofit

    companion object {
        val instance: RetrofitClient by lazy { RetrofitClient() }
    }


    init {

        retrofit = Retrofit.Builder()
            .client(initClient())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://www.wanandroid.com")
            .build()

    }
    // Khởi tạo client
    private fun initClient(): OkHttpClient {

        return OkHttpClient.Builder()
            //.addInterceptor(initLogInterceptor())
            .addInterceptor(CommonInterceptor()) //Thêm các header mặc định
            .connectTimeout(10, TimeUnit.SECONDS) // Timeout để kết nối Api
            .readTimeout(10, TimeUnit.SECONDS) //Timeout khi đọc dữ liệu
            .build()
    }
//
//    private fun initLogInterceptor(): HttpLoggingInterceptor {
//        val loggingInterceptor = HttpLoggingInterceptor()
//        if (BuildConfig.DEBUG) {
//            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
//        } else {
//            loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
//        }
//        return loggingInterceptor
//    }


    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }


}