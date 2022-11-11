package com.example.shotaro_kumagai_myruns4.start.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Criteria
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.shotaro_kumagai_myruns4.R
import com.example.shotaro_kumagai_myruns4.start.map.databinding.ActivityMapBinding

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

abstract class MapActivity: AppCompatActivity(), OnMapReadyCallback, LocationListener,
    GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapBinding
    private val PERMISSION_REQUEST_CODE = 0
    private lateinit var locationManager: LocationManager
    private lateinit var polylineOptions: PolylineOptions
    private lateinit var polylines: ArrayList<MarkerOptions>
    private lateinit var markerOptions: MarkerOptions


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val actionBar:ActionBar? = supportActionBar
        if (actionBar !== null) {
            actionBar!!.title = "Map";
        }

        binding = ActivityMapBinding.inflate(layoutInflater)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.setOnMapClickListener(this)
        mMap.setOnMapLongClickListener(this)
        polylineOptions = PolylineOptions()
        polylineOptions.color(Color.BLACK)
        polylines = ArrayList()
        markerOptions = MarkerOptions()

        checkPermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (locationManager != null){
            locationManager.removeUpdates(this)
        }
    }


    fun initLocationManager() {
        try {
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_FINE
            val provider : String? = locationManager.getBestProvider(criteria, true)
            if(provider != null) {
                val location = locationManager.getLastKnownLocation(provider)

                if (location != null)
                    onLocationChanged(location)
                locationManager.requestLocationUpdates(provider, 0, 0f, this)
            }
        } catch (e: SecurityException) {
        }
    }


    fun checkPermission() {
        if (Build.VERSION.SDK_INT < 23) return
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
        else
            initLocationManager()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
             if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initLocationManager()
                }
            }
        }


    fun onSave (view:View){
        finish()
    }

    fun onCancel(view:View){
        finish()
    }

}