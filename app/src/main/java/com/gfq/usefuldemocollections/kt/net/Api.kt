package com.gfq.usefuldemocollections.kt.net

import androidx.lifecycle.LiveData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class BaseApiBean<T>(val code: Int, val message: String, val result: T)

interface Api {
    //https://api.apiopen.top/
    @GET("getJoke?page=1&count=2&type=video")
    fun getJoke(): Call<LiveData<BaseApiBean<String>>>
}

object ApiService {
    var instance: Api

    init {
        val re = Retrofit.Builder()
            .baseUrl("https://api.apiopen.top/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().apply {
                addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            }.build()).build()

        instance = re.create(Api::class.java)
    }


}


