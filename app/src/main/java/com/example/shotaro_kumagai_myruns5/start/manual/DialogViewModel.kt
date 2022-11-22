package com.example.shotaro_kumagai_myruns5.start.manual

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*


class DialogViewModel:ViewModel() {
    val dateTime = MutableLiveData<Calendar>(Calendar.getInstance())
    val duration = MutableLiveData<Double>(0.0)
    val distance = MutableLiveData<Double>(0.0)
    val calorie = MutableLiveData<Double>(0.0)
    val heartRate = MutableLiveData<Double>(0.0)
    val comment = MutableLiveData<String>("")
}

