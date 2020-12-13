package com.psi.nusaku.Activity.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.psi.nusaku.Model.Posting

class DashboardViewModel : ViewModel() {
    private var post = MutableLiveData<MutableList<Posting>>()
    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

}