package com.example.testnauka.utils

import android.view.View
import android.widget.AdapterView

object SpinnerItemListener: AdapterView.OnItemSelectedListener {
    var position: Int = 0

    override fun onNothingSelected(p0: AdapterView<*>?) {
        position = -1
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        position = p2
    }
}
