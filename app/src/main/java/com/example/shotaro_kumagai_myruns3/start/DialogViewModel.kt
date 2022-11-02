package com.example.shotaro_kumagai_myruns3.start

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import java.util.*
import kotlin.collections.ArrayList


class DialogViewModel:ViewModel() {
    val dateTime = MutableLiveData<Calendar>(Calendar.getInstance())
    val duration = MutableLiveData<Double>(0.0)
    val distance = MutableLiveData<Double>(0.0)
    val calorie = MutableLiveData<Double>(0.0)
    val heartRate = MutableLiveData<Double>(0.0)
    val comment = MutableLiveData<String>("")
}

