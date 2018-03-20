package com.technology.codespace.rem.Activity.Patient

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.awesome.shorty.AwesomeToast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.technology.codespace.rem.Data.Patient
import com.technology.codespace.rem.R
import kotlinx.android.synthetic.main.activity_patient_sign_up1.*
import kotlinx.android.synthetic.main.activity_patient_username_login.*
import java.util.*

class PatientSignUp1 : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var lastLocation: Location
    private lateinit var mMap: GoogleMap
    private  val LOCATION_PERMISSION_REQUEST_CODE = 1
    lateinit var  locationManager:LocationManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onMarkerClick(p0: Marker?) = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_sign_up1)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)



        signUpNextBtn.setOnClickListener {
            if(!TextUtils.isEmpty(nameText.text.toString()) && nameText.text.toString().trim() != "" &&
                    !TextUtils.isEmpty(userNameText.text.toString()) && userNameText.text.toString().trim() != "" &&
                    !TextUtils.isEmpty(passwordText.text.toString()) && passwordText.text.toString().trim() != "" &&
                    !TextUtils.isEmpty(emailText.text.toString()) && emailText.text.toString().trim() != "" &&
                    !TextUtils.isEmpty(locationText.text.toString()) && locationText.text.toString().trim() != "" ){
                val intent = Intent(this,PatientSignUp3::class.java)

                var name = nameText.text.toString()
                Log.d("Name",name)
                intent.putExtra("Name",name)
                intent.putExtra("Username",userNameText!!.text.toString())
                intent.putExtra("Password",passwordText.text.toString())
                intent.putExtra("Email",emailText.text.toString())
                intent.putExtra("Location",locationText.text.toString())

                startActivity(intent)
            }
            else
            {
                AwesomeToast.error(this,"Enter all details!",Toast.LENGTH_SHORT).show()
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true


        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->

            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                placeMarkerOnMap(currentLatLng)
                var geocoder = Geocoder(this, Locale.getDefault())
                var address = geocoder.getFromLocation(location.latitude,location.longitude,1)
                locationText.setText(address.get(0).getAddressLine(0).toString() )
            }
        }
    }

    private fun placeMarkerOnMap(location: LatLng) {
        // 1
        val markerOptions = MarkerOptions().position(location)
        // 2
        mMap.addMarker(markerOptions)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.getUiSettings().setZoomControlsEnabled(true)
        mMap.setOnMarkerClickListener(this)
        setUpMap()
    }

}
