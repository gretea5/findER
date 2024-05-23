package com.gretea5.finder.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gretea5.finder.data.model.ERDetail

class ERViewModel : ViewModel() {
    private val _erDetailData = MutableLiveData<ERDetail>()
    val erDetailData : LiveData<ERDetail>
        get() = _erDetailData
}