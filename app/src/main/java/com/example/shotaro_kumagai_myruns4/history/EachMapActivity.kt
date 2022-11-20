package com.example.shotaro_kumagai_myruns4.history

import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.shotaro_kumagai_myruns4.R
import com.example.shotaro_kumagai_myruns4.Unit
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.shotaro_kumagai_myruns4.databinding.ActivityEachMapBinding
import com.example.shotaro_kumagai_myruns4.start.Start
import com.google.android.gms.maps.model.PolylineOptions
import com.example.shotaro_kumagai_myruns4.db.*
import java.util.*
import kotlin.collections.ArrayList

class EachMapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityEachMapBinding
    private lateinit var markerOptions: MarkerOptions
    private lateinit var polylineOptions: PolylineOptions
    private lateinit var locationList: ArrayList<LatLng>
    private lateinit var viewModelFactory: ActionViewModelFactory
    private lateinit var database: ActionDatabase
    private lateinit var databaseDao: ActionDao
    private lateinit var repository: ActionRepository
    private lateinit var actionViewModel: ActionViewModel
    private lateinit var typeTextView: TextView
    private lateinit var avgSpeedTextView: TextView
    private lateinit var currSpeedTextView: TextView
    private lateinit var climbTextView: TextView
    private lateinit var caloriesTextView: TextView
    private lateinit var distanceTextView: TextView
    private lateinit var startTime: Calendar
    private lateinit var currTime: Calendar
    private var target: Action? = null
    private var activityType = -1
    private var ind = -1


    //fetching data from db
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEachMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        typeTextView = findViewById(R.id.map_type)
        climbTextView = findViewById(R.id.map_climb)
        distanceTextView = findViewById(R.id.map_distance)


        database = ActionDatabase.getInstance(this)
        databaseDao = database.actionDao
        repository = ActionRepository(databaseDao)
        viewModelFactory = ActionViewModelFactory(repository)
        actionViewModel = ViewModelProvider(this, viewModelFactory)[ActionViewModel::class.java]
        activityType = intent.getIntExtra(Start.SELECTED_ACTIVITIES,0)
        ind = intent.getIntExtra(HistoryAdapter.EACH_ACTION, -1)
        actionViewModel.allActionsLiveData.observe(this) {
            if(ind > -1){
                target = actionViewModel.eachAction(ind)
                locationList = target!!.locationList
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
                setText()
            }
        }
    }

    //showing map
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        markerOptions = MarkerOptions()
        polylineOptions = PolylineOptions()
        if(locationList.isEmpty()){
            return
        }
        val latlng = locationList.last()
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, 16f)
        mMap.animateCamera(cameraUpdate)
        mMap.addMarker(markerOptions.position(latlng))
        locationList.forEach {
            polylineOptions.add(it)
        }
        mMap.addMarker(markerOptions.position(latlng))
        mMap.addPolyline(polylineOptions.color(Color.BLACK).width(5f))
    }

    //app bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId != R.id.delete_button) {
            return  false
        }
        val id: Long = target?.id ?: return false
        ind -= 1
        actionViewModel.deleteSelect(id)
        finish()
        return true
    }

    //display data
    private fun setText(){
        typeTextView.text = "Type: " +
                "${resources.getStringArray(R.array.activities)[target!!.activityType]}"
        val curr: LatLng = locationList.last()
        val unit: Unit = Unit.getInstance()
        val res: FloatArray = floatArrayOf(0F,0F,0F)
        Location.distanceBetween(locationList[0].latitude, locationList[0].longitude, curr.latitude, curr.longitude, res)
        val fixUnit: Double = if (unit.isMile) 1.0 else 1.60934
        val strUnit: String = if (unit.isMile) "Miles" else "Kilometer"
        distanceTextView.text = "Distance: " + "${res[0] * fixUnit}" + strUnit
        climbTextView.text = "Climb: " + "${res[0] * fixUnit}" + strUnit
    }
}