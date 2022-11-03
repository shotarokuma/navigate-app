package com.example.shotaro_kumagai_myruns3

import android.app.Application

class Unit: Application() {
    var isMile:Boolean = true

    companion object {
        private var instance : Unit? = null

        fun  getInstance(): Unit {
            if (instance == null)
                instance = Unit()

            return instance!!
        }
    }
}