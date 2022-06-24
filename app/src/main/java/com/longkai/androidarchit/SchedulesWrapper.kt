package com.longkai.androidarchit

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SchedulesWrapper {
  fun io(): Scheduler {
    return Schedulers.io();
  }

  fun main(): Scheduler {
    return AndroidSchedulers.mainThread()
  }
}