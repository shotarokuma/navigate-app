package com.example.shotaro_kumagai_myruns5.sensor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class Receiver: BroadcastReceiver()  {

    //send data to activity from service
    override fun onReceive(context: Context, intent: Intent) {
        println(currString)
        currString = intent.getStringExtra(ACTIVITY_DATA).toString()
    }

    companion object {
        var currString = ""
        const val ACTIVITY_DATA = "activity_data"
    }
}