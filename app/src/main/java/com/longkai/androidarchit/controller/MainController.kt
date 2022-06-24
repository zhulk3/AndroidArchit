package com.longkai.androidarchit.controller

import com.longkai.androidarchit.model.MainModel
import com.longkai.androidarchit.view.MainActivity

class MainController() {
  private lateinit var model: MainModel
  private lateinit var activity: MainActivity

  infix fun hasView(mainActivity: MainActivity){
    activity = mainActivity
  }
  infix fun hasModel(model: MainModel){
    this.model = model
  }

  fun findAddress(address:String){
    activity.showProgressBar()
    model.findAddress(address)
  }

  fun onStop(){
    model.stopLoadingList()
  }

  fun doWhenResultIsReady(){
    activity.hideProgressBar()
    activity.showResult()
  }

  fun doWhenError(){
    activity.hideProgressBar()
    activity.showError()
  }
}