package com.longkai.androidarchit.viewModel

import com.longkai.androidarchit.model.MainModel
import com.longkai.androidarchit.model.SchedulesWrapper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException

class MainViewModel() {
  lateinit var resultListObservable: PublishSubject<List<String>>
  lateinit var resultListErrorObservable: PublishSubject<HttpException>
  lateinit var itemObservable: PublishSubject<MainModel.ResultEntity>
  private lateinit var entityList: List<MainModel.ResultEntity>
  private val compositeDisposable: CompositeDisposable = CompositeDisposable()
  private var schedulesWrapper: SchedulesWrapper = SchedulesWrapper()
  private lateinit var model: MainModel


  constructor(model: MainModel) : this() {
    this.model = model
    resultListObservable = PublishSubject.create();
    resultListErrorObservable = PublishSubject.create()
    itemObservable = PublishSubject.create()
  }


  fun findAddress(address: String) {
    val disposable: Disposable =
      model.fetchAddress(address)?.subscribeOn(schedulesWrapper.io())!!.observeOn(
        schedulesWrapper.main()
      )
        .subscribeWith(object : DisposableObserver<List<MainModel.ResultEntity>?>() {
          override fun onNext(t: List<MainModel.ResultEntity>) {
            entityList = t
            resultListObservable.onNext(fetchItemTextFrom(t))
          }

          override fun onComplete() {
          }

          override fun onError(e: Throwable) {
            resultListErrorObservable.onNext(e as HttpException)
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
    itemObservable.onNext(entityList[position])
  }
}