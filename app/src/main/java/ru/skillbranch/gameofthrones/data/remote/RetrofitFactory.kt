package ru.skillbranch.gameofthrones.data.remote

import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.skillbranch.gameofthrones.AppConfig.BASE_URL
import ru.skillbranch.gameofthrones.AppConfig.MAX_REQUEST
import ru.skillbranch.gameofthrones.BuildConfig

class RetrofitFactory {
    companion object {
        private fun getOkHttpClient() =
            OkHttpClient()
                .newBuilder()
                .dispatcher(Dispatcher().apply { maxRequests = MAX_REQUEST })
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) BODY else NONE
                }).build()

        private fun getRetrofitClient() =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        fun getApi() = getRetrofitClient().create(Api::class.java)
    }
}