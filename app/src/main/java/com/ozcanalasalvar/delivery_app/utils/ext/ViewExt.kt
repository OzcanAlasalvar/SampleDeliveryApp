package com.ozcanalasalvar.delivery_app.utils.ext

import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar


inline fun SearchView.onQueryTextChanged(crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }

    })
}


fun RecyclerView.scrollToNext() {
    val layoutManager = this.layoutManager
    val adapter = this.adapter
    if (layoutManager is LinearLayoutManager && adapter != null) {
        if (layoutManager.findLastCompletelyVisibleItemPosition() < (this.adapter!!.itemCount - 1)) {
            layoutManager.smoothScrollToPosition(
                this,
                null,
                layoutManager.findLastCompletelyVisibleItemPosition() + 1
            )
        }
    }
}

fun RecyclerView.scrollToPrevious() {
    val layoutManager = this.layoutManager
    val adapter = this.adapter
    if (layoutManager is LinearLayoutManager && adapter != null) {
        if (layoutManager.findLastCompletelyVisibleItemPosition() > 0) {
            layoutManager.smoothScrollToPosition(
                this,
                null,
                layoutManager.findLastCompletelyVisibleItemPosition() - 1
            )
        }
    }
}


fun View.visible() {
    this.visibility = View.VISIBLE
}


fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.showSuccessPopup(msg: String) {
    Snackbar.make(this, msg, Snackbar.LENGTH_LONG)
        .apply {
            val sbView = this.view
            sbView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    android.R.color.holo_green_dark
                )
            )
        }.show()
}

fun View.showFailurePopup(msg: String) {
    Snackbar.make(this, msg, Snackbar.LENGTH_LONG)
        .apply {
            val sbView = this.view
            sbView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    android.R.color.holo_red_dark
                )
            )
        }.show()
}

inline fun View.showNetworkError(msg: String, crossinline listener: () -> Unit) {
    Snackbar.make(this, msg, Snackbar.LENGTH_LONG)
        .apply {
            val sbView = this.view
            sbView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    android.R.color.holo_red_dark
                )
            )
        }.setAction("RETRY") {
            listener()
        }.show()
}

inline fun SeekBar.OnProgressChanged(crossinline listener: (Int) -> Unit) {
    this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
            listener(progress)
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {

        }

        override fun onStopTrackingTouch(p0: SeekBar?) {

        }

    })
}
