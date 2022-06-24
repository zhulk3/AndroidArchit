package com.longkai.androidarchit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val bundle = intent.extras
    setContentView(R.layout.activity_detail)
    if (bundle != null) {
      Log.d(Companion.TAG, "onCreate: " + (bundle.getString(MainActivity.MovieInfo.RANTING)))
    }else{
      Log.d(Companion.TAG, "onCreate: bundle is null")
    }
    if (bundle != null) {
      detail_rating.text = "${MainActivity.MovieInfo.RANTING.capitalize()}: ${
        bundle.getString(MainActivity.MovieInfo.RANTING)
      }"
    }
    if (bundle != null) {
      detail_title.text =
        "${MainActivity.MovieInfo.TITLE.capitalize()}: ${bundle.getString(MainActivity.MovieInfo.TITLE)}"
    }
    if (bundle != null) {
      detail_year.text =
        "${MainActivity.MovieInfo.YEAR.capitalize()}: ${bundle.getString(MainActivity.MovieInfo.YEAR)}"
    }
    if (bundle != null) {
      detail_date.text =
        "${MainActivity.MovieInfo.DATE.capitalize()}: ${bundle.getString(MainActivity.MovieInfo.DATE)}"
    }
    detail_back.setOnClickListener { onBackPressed() }
  }

  companion object {
    private const val TAG = "DetailActivity"
  }
}