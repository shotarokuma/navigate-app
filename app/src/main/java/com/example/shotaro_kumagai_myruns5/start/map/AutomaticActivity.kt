package com.example.shotaro_kumagai_myruns5.start.map

import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.shotaro_kumagai_myruns5.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.shotaro_kumagai_myruns5.databinding.ActivityAutomaticBinding
import com.example.shotaro_kumagai_myruns5.start.Start
import com.google.android.gms.maps.model.PolylineOptions
import com.example.shotaro_kumagai_myruns5.db.*
import com.example.shotaro_kumagai_myruns5.Unit
import java.util.*
import kotlin.collections.ArrayList

class AutomaticActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {
    private lateinit var mMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var binding: ActivityAutomaticBinding
    private lateinit var markerOptions: MarkerOptions
    private lateinit var polylineOptions: PolylineOptions
    private lateinit var locationList: ArrayList<LatLng>
    private lateinit var viewModelFactory: ActionViewModelFactory
    private lateinit var database: ActionDatabase
    private lateinit var databaseDao: ActionDao
    private lateinit var repository: ActionRepository
    private lateinit var actionViewModel: ActionViewModel
    private lateinit var action: Action
    private lateinit var typeTextView: TextView
    private lateinit var avgSpeedTextView: TextView
    private lateinit var currSpeedTextView: TextView
    private lateinit var climbTextView: TextView
    private lateinit var caloriesTextView: TextView
    private lateinit var distanceTextView: TextView
    private lateinit var startLatLng: LatLng
    private lateinit var startTime: Calendar
    private lateinit var prevTime: Calendar
    private lateinit var currTime: Calendar
    private var activityType = -1
    private var centered = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAutomaticBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar
        if (actionBar !== null) {
            actionBar.title = "Map";
        }

        typeTextView = findViewById(R.id.maps_type)
        currSpeedTextView = findViewById(R.id.maps_curr_speed)
        avgSpeedTextView = findViewById(R.id.maps_avg_speed)
        climbTextView = findViewById(R.id.maps_climb)
        caloriesTextView = findViewById(R.id.maps_calorie)
        distanceTextView = findViewById(R.id.maps_distance)

        database = ActionDatabase.getInstance(this)
        databaseDao = database.actionDao
        repository = ActionRepository(databaseDao)
        viewModelFactory = ActionViewModelFactory(repository)
        actionViewModel = ViewModelProvider(this, viewModelFactory)[ActionViewModel::class.java]
        activityType = intent.getIntExtra(Start.SELECTED_ACTIVITIES,0)
        locationList = actionViewModel.latestLocationList()
        actionViewModel.allActionsLiveData.observe(this) {
            locationList = actionViewModel.latestLocationList()
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        markerOptions = MarkerOptions()
        polylineOptions = PolylineOptions()
        checkPermission()
    }


    //save data in db
    fun onSave (view: View){
        action = Action(
            inputType = 2,
            activityType = intent.getIntExtra(Start.SELECTED_ACTIVITIES, 0),
            dateTime = Calendar.getInstance(),
            duration = 0.0,
            distance = 0.0,
            calorie = 0.0,
            heartRate = 0.0,
            comment = "",
            locationList = locationList
        )
        actionViewModel.insert(action)
        Toast.makeText(this, "Entry${ActionViewModel.sum} Saved", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun onCancel(view:View){
        finish()
    }
    override fun onDestroy() {
        super.onDestroy()
        if (locationManager != null){
            locationManager.removeUpdates(this)
        }
    }

    //check permission
    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermission() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
        }else{
            initLocation()
        }
    }

    //initialize data
    private fun initLocation(){
        try {
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_FINE
            val provider : String? = locationManager.getBestProvider(criteria, true)
            if(provider != null) {
                val location = locationManager.getLastKnownLocation(provider)
                if (location != null){
                    onLocationChanged(location)
                }
                locationManager.requestLocationUpdates(provider, 0, 0f, this)
            }
        } catch (e: SecurityException) {
        }
    }

    //updating where uer is
    override fun onLocationChanged(location: Location) {
        val lat: Double =  location.latitude
        val lng: Double = location.longitude
        val latlng: LatLng = LatLng(lat, lng)
        if(!centered){
            startLatLng = latlng
            startTime = Calendar.getInstance()
            currTime = Calendar.getInstance()
            prevTime = Calendar.getInstance()
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, 16f)
            mMap.animateCamera(cameraUpdate)
            mMap.addMarker(markerOptions.position(latlng))
            locationList.add(latlng)
            locationList.forEach {
                polylineOptions.add(it)
            }
            setText()
            centered = true
        }
        prevTime = currTime
        currTime = Calendar.getInstance()
        mMap.addMarker(markerOptions.position(latlng))
        locationList.add(latlng)
        polylineOptions.add(latlng)
        mMap.addPolyline(polylineOptions.color(Color.BLACK).width(5f))
        setText()
    }

    //display data
    private fun setText(){
        typeTextView.text = "Type: " +
                "${resources.getStringArray(R.array.activities)[intent.getIntExtra(Start.SELECTED_ACTIVITIES, 0)]}"
        val curr: LatLng = locationList.last()
        val prev: LatLng = if(locationList.size >= 2) locationList[locationList.size - 2] else locationList.last()
        val unit:Unit = Unit.getInstance()
        val res: FloatArray = floatArrayOf(0F,0F,0F)
        val res2: FloatArray = floatArrayOf(0F,0F,0F)
        Location.distanceBetween(startLatLng.latitude, startLatLng.longitude, curr.latitude, curr.longitude, res)
        Location.distanceBetween(prev.latitude, prev.longitude, curr.latitude, curr.longitude, res2)
        val fixUnit: Double = if (unit.isMile) 1.0 else 1.60934
        val strUnit: String = if (unit.isMile) "Miles" else "Kilometer"
        val strUnit2: String = if (unit.isMile) "mi/h" else "km/h"
        val dur: Long = currTime.time.time - startTime.time.time
        val dur2: Long = currTime.time.time - prevTime.time.time
        distanceTextView.text = "Distance: " + "${res[0] * fixUnit}" + strUnit
        currSpeedTextView.text = "Curr speed: ${res2[0] * fixUnit /  dur2 * 3600000}" + strUnit2
        avgSpeedTextView.text = "Avg speed: ${res[0] * fixUnit /  dur * 3600000}" + strUnit2
        climbTextView.text = "Climb: " + "${res[0] * fixUnit}" + strUnit
        caloriesTextView.text = "Calorie" + "${60 * 1.05 * dur /3600000}"
    }
}