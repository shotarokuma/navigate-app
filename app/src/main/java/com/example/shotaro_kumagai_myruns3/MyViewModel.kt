package com.example.shotaro_kumagai_myruns3

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel:ViewModel() {
    val userImage = MutableLiveData<Bitmap>()
}