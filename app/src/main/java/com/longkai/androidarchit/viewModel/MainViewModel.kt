package com.longkai.androidarchit.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.longkai.androidarchit.model.MainModel
import com.longkai.androidarchit.model.SchedulesWrapper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import retrofit2.HttpException

class MainViewModel(val model: MainModel) {
  private var resultListObservable = MutableLiveData<List<String>>()
  private var resultListErrorObservable = MutableLiveData<HttpException>()
  private var itemObservable = MutableLiveData<MainModel.ResultEntity>()
  private lateinit var entityList: List<MainModel.ResultEntity>
  private val compositeDisposable: CompositeDisposable = CompositeDisposable()
  private var schedulesWrapper: SchedulesWrapper = SchedulesWrapper()


  fun findAddress(address: String) {
    val disposable: Disposable =
      model.fetchAddress(address)?.subscribeOn(schedulesWrapper.io())!!.observeOn(
        schedulesWrapper.main()
      )
        .subscribeWith(object : DisposableObserver<List<MainModel.ResultEntity>?>() {
          override fun onNext(t: List<MainModel.ResultEntity>) {
            entityList = t
            resultListObservable.postValue(fetchItemTextFrom(t))
          }

          override fun onComplete() {
          }

          override fun onError(e: Throwable) {
            resultListErrorObservable.postValue(e as HttpException)
          }
        })
    compositeDisposable.add(disposable)
  }

  private fun fetchItemTextFrom(it: List<MainModel.ResultEntity>): ArrayList<String> {
    val li = arrayListOf<String>()
    for (resultEntity in it) {
      li.add("${resultEntity.year}: ${resultEntity.title}")
    }
    return li
  }

  fun cancelNetworkConnections() {
    compositeDisposable.clear()
  }

  fun doOnItemClick(position: Int) {
    itemObservable.postValue(entityList[position])
  }

  fun getResultListObservable(): LiveData<List<String>> = resultListObservable
  fun getResultListErrorObservable(): LiveData<HttpException> = resultListErrorObservable
  fun getItemObservable(): LiveData<MainModel.ResultEntity> = itemObservable


}