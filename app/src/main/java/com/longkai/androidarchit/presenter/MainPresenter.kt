package com.longkai.androidarchit.presenter

import com.longkai.androidarchit.model.MainModel
import com.longkai.androidarchit.model.SchedulesWrapper
import com.longkai.androidarchit.view.MainActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver

class MainPresenter() {
  private val compositeDisposable: CompositeDisposable = CompositeDisposable()
  private var schedulesWrapper: SchedulesWrapper = SchedulesWrapper()
  private lateinit var model: MainModel
  private lateinit var activity: MainActivity

  constructor(model: MainModel) : this() {
    this.model = model;
  }

  infix fun hasView(activity: MainActivity) {
    this.activity = activity
  }

  fun findAddress(address: String) {
    val disposable: Disposable =
      model.fetchAddress(address)?.subscribeOn(schedulesWrapper.io())!!.observeOn(
        schedulesWrapper.main()
      )
        .subscribeWith(object : DisposableObserver<List<MainModel.ResultEntity>?>() {
          override fun onNext(t: List<MainModel.ResultEntity>) {
            activity.hideProgressBar()
            activity.updateRecyclerView(t)
          }

          override fun onStart() {
            activity.showProgressBar()
          }

          override fun onComplete() {
          }

          override fun onError(e: Throwable) {
            activity.hideProgressBar()
            activity.showError()
          }
        })
    compositeDisposable.add(disposable)
  }

  fun onStop() {
    compositeDisposable.clear()
  }
}