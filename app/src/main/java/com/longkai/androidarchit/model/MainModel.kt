package com.longkai.androidarchit.model

import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainModel {
  private var mRetrofit: Retrofit? = null

   fun fetchAddress(address: String): Observable<List<ResultEntity>>? {
    return getRetrofit()?.create(AddressService::class.java)?.fetchLocationFromServer(address)
  }

  private fun getRetrofit():Retrofit?{
    if (mRetrofit == null) {
      val loggingInterceptor = HttpLoggingInterceptor()
      loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
      val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
      mRetrofit = Retrofit.Builder().baseUrl("http://bechdeltest.com/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(client).build()
    }
    return mRetrofit
  }

  interface AddressService {
    @GET("getMoviesByTitle")
    fun fetchLocationFromServer(@Query("title") title: String): Observable<List<ResultEntity>>
  }

  class ResultEntity(val title: String, val rating: String, val data: String, val year: String)
}