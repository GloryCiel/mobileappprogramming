package com.example.runningapp.ui.findCrew

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FindCrewViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is find crew Fragment"
    }
    val text: LiveData<String> = _text

}