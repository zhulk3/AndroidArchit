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
import com.longkai.androidarchit.presenter.MainPresenter
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
  private lateinit var presenter: MainPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    addressAdapter = AddressAdapter(onClick = { item ->
      DetailActivity.startDetailActivity(this, item)
    })

    presenter = MainPresenter(MainModel())
    presenter hasView this

    val layoutManager = LinearLayoutManager(this)
    layoutManager.orientation = LinearLayoutManager.VERTICAL
    recyclerView.layoutManager = layoutManager
    recyclerView.adapter = addressAdapter
    search_movie_btn.setOnClickListener { presenter.findAddress(edit_text.text.toString()) }

  }

  @SuppressLint("NotifyDataSetChanged")
  fun updateRecyclerView(results: List<MainModel.ResultEntity>) {
    addressAdapter.updateList(results)
    addressAdapter.notifyDataSetChanged()
  }


  override fun onResume() {
    super.onResume()
    hideProgressBar()
  }

  override fun onStop() {
    super.onStop()
    presenter.onStop()
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

  class AddressAdapter(val onClick: (item: MainModel.ResultEntity) -> Unit) : RecyclerView
  .Adapter<AddressAdapter.Holder>() {
    var mList: List<MainModel.ResultEntity> = arrayListOf()

    override fun getItemCount(): Int {
      return mList.size
    }

    fun updateList(list: List<MainModel.ResultEntity>) {
      mList = list
    }

    class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
      val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
      return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
      holder.itemView.item_textview.text = "${mList[position].year}: ${mList[position].title}"
      holder.itemView.setOnClickListener { onClick(mList[position]) }
    }
  }

}