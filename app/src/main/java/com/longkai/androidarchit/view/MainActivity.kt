package com.longkai.androidarchit.view


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.longkai.androidarchit.R
import com.longkai.androidarchit.model.MainModel
import com.longkai.androidarchit.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.view.*

class MainActivity : AppCompatActivity() {
  object MovieInfo {
    const val RANTING = "rating"
    const val TITLE = "title"
    const val YEAR = "year"
    const val DATE = "date"
  }

  private lateinit var addressAdapter: AddressAdapter
  private lateinit var viewModel: MainViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    addressAdapter = AddressAdapter()
    viewModel = MainViewModel(MainModel())
    addressAdapter setItemClickMethod viewModel::doOnItemClick
    val layoutManager = LinearLayoutManager(this)
    layoutManager.orientation = LinearLayoutManager.VERTICAL
    recyclerView.layoutManager = layoutManager
    recyclerView.adapter = addressAdapter
    listenToObservable()
    search_movie_btn.setOnClickListener {
      showProgressBar();viewModel.findAddress(
      edit_text.text
        .toString()
    )
    }

  }

  @SuppressLint("NotifyDataSetChanged")
  fun updateRecyclerView(t: List<String>) {
    addressAdapter.updateList(t)
    addressAdapter.notifyDataSetChanged()
  }

  @SuppressLint("CheckResult")
  private fun listenToObservable() {
    viewModel.resultListObservable.subscribe { hideProgressBar();updateRecyclerView(it) }
    viewModel.itemObservable.subscribe { DetailActivity.startDetailActivity(this, it) }
    viewModel.resultListErrorObservable.subscribe { hideProgressBar();showError() }
  }

  override fun onResume() {
    super.onResume()
    hideProgressBar()
  }

  override fun onStop() {
    super.onStop()
    viewModel.cancelNetworkConnections()
  }

  fun showError() {
    Toast.makeText(
      this@MainActivity,
      R.string.error_getting_results,
      Toast.LENGTH_SHORT
    ).show()
  }


  fun showProgressBar() {
    progress_bar.visibility = View.VISIBLE
  }

  fun hideProgressBar() {
    progress_bar.visibility = View.GONE
  }

  class AddressAdapter : RecyclerView
  .Adapter<AddressAdapter.Holder>() {
    var mList: List<String> = arrayListOf()
    private lateinit var mOnClick: (position: Int) -> Unit

    override fun getItemCount(): Int {
      return mList.size
    }

    fun updateList(list: List<String>) {
      mList = list
    }

    class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
      val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
      return Holder(view)
    }

    infix fun setItemClickMethod(onClick: (position: Int) -> Unit) {
      mOnClick = onClick
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
      holder.itemView.item_textview.text = mList[position]
      holder.itemView.setOnClickListener { mOnClick(position) }
    }
  }

}