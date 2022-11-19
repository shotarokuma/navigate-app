package com.example.shotaro_kumagai_myruns4.start.map

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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.shotaro_kumagai_myruns4.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.shotaro_kumagai_myruns4.databinding.ActivityMapsBinding
import com.example.shotaro_kumagai_myruns4.start.Start
import com.google.android.gms.maps.model.PolylineOptions
import com.example.shotaro_kumagai_myruns4.db.*
import java.util.*
import kotlin.collections.ArrayList

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {
    private lateinit var mMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var binding: ActivityMapsBinding
    private lateinit var markerOptions: MarkerOptions
    private lateinit var polylineOptions: PolylineOptions
    private lateinit var locationList: ArrayList<LatLng>
    private lateinit var viewModelFactory: ActionViewModelFactory
    private lateinit var database: ActionDatabase
    private lateinit var databaseDao: ActionDao
    private lateinit var repository: ActionRepository
    private lateinit var actionViewModel: ActionViewModel
    private lateinit var action: Action
    private var activityType = -1
    private var centered = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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


    fun onSave (view: View){
        action = Action(
            inputType = intent.getIntExtra(Start.SELECTED_INPUT, -1),
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

    override fun onLocationChanged(location: Location) {
        val lat: Double =  location.latitude
        val lng: Double = location.longitude
        val latlng: LatLng = LatLng(lat, lng)
        if(!centered){
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, 16f)
            mMap.animateCamera(cameraUpdate)
            mMap.addMarker(markerOptions.position(latlng))
            locationList.add(latlng)
            locationList.forEach {
                polylineOptions.add(it)
            }
            centered = true
        }
        mMap.addMarker(markerOptions.position(latlng))
        locationList.add(latlng)
        polylineOptions.add(latlng)
        mMap.addPolyline(polylineOptions.color(Color.BLACK).width(5f))
    }
}