package com.xazhuxj.smartroute.ui.points

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PointsViewModel : ViewModel() {

    private val _textView_curve = MutableLiveData<String>().apply {
        value = ""
    }
    val textView_curve: LiveData<String> = _textView_curve
}