package com.longkai.androidarchit.model

import com.longkai.androidarchit.SchedulesWrapper
import com.longkai.androidarchit.controller.MainController
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainModel(val controller: MainController) {
  private var mRetrofit: Retrofit? = null
  var mList = listOf<ResultEntity>()
  private val compositeDisposable: CompositeDisposable = CompositeDisposable()
  private var schedulesWrapper: SchedulesWrapper = SchedulesWrapper()

  fun findAddress(address: String) {
    val disposable: Disposable =
      fetchAddress(address)?.subscribeOn(schedulesWrapper.io())!!.observeOn(
        schedulesWrapper.main())
        .subscribeWith(object : DisposableObserver<List<ResultEntity>?>() {
          override fun onNext(t: List<ResultEntity>) {
            mList = t
            controller.doWhenResultIsReady()
          }

          override fun onStart() {
          }

          override fun onComplete() {
          }

          override fun onError(e: Throwable) {
            controller.doWhenError()
          }
        })
    compositeDisposable.add(disposable)
  }

  fun stopLoadingList() {
    compositeDisposable.clear()
  }

  private fun fetchAddress(address: String): Observable<List<ResultEntity>>? {
    if (mRetrofit == null) {
      val loggingInterceptor = HttpLoggingInterceptor()
      loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
      val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
      mRetrofit = Retrofit.Builder().baseUrl("http://bechdeltest.com/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(client).build()
    }
    return mRetrofit?.create(AddressService::class.java)?.fetchLocationFromServer(address)
  }

  interface AddressService {
    @GET("getMoviesByTitle")
    fun fetchLocationFromServer(@Query("title") title: String): Observable<List<ResultEntity>>
  }

  class ResultEntity(val title: String, val rating: String, val data: String, val year: String)
}