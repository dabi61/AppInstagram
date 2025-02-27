package com.example.appinstagram.di

import com.example.appinstagram.api.ApiService
import com.example.appinstagram.utils.Constants.BASE_URL
import com.example.appinstagram.utils.Constants.NETWORK_TIMEOUT
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tencent.mmkv.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val baseUrl = BASE_URL
const val networkTimeOut = NETWORK_TIMEOUT

fun provideGson() : Gson = GsonBuilder().setLenient().create()

fun provideOkHttpClient() = if(BuildConfig.DEBUG){
    val logginInterceptor = HttpLoggingInterceptor()
    logginInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
    logginInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    val requestInterceptor = Interceptor{ chain ->
        val url = chain.request()
            .url
            .newBuilder()
            .build()

        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()

        return@Interceptor chain.proceed(request)
    }
    OkHttpClient
        .Builder()
        .addInterceptor(logginInterceptor)
        .addInterceptor(requestInterceptor)
        .build()
} else {
    OkHttpClient
        .Builder()
        .build()
}

fun provideRetrofit(baseUrl: String, gson: Gson, client: OkHttpClient) : ApiService =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ApiService::class.java)

val apiModule = module {
    single { baseUrl }
    single { networkTimeOut }
    single { provideGson() }
    single { provideOkHttpClient() }
    single { provideRetrofit(get(), get(), get()) }
}