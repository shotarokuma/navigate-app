package com.example.shotaro_kumagai_myruns3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar

class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val actionBar:ActionBar? = supportActionBar
        if (actionBar !== null) {
            actionBar!!.title = "Map";
        }
    }


    fun onSave (view:View){
        finish()
    }

    fun onCancel(view:View){
        finish()
    }

}