package com.example.storymeow.view.maps

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.storymeow.R
import com.example.storymeow.data.Result
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.storymeow.databinding.ActivityMapsBinding
import com.example.storymeow.view.ViewModelFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val boundsBuilder = LatLngBounds.Builder()
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel by viewModels<MapsViewModel>{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setMapStyle()
        getLocation()

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true

    }

    private fun setMapStyle(){
        try {
            val success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.map_style_day))
            if (!success){
                Log.e("MapsActivity","Style parsing failed")
            }
        }catch (exc: Resources.NotFoundException){
            Log.e("MapsActivity","Cant find style. Error: ", exc)
        }
    }

    private fun getLocation(){
        lifecycleScope.launch {
            viewModel.getLocation().observe(this@MapsActivity){location->
                when(location){
                    is Result.Error->{
                        val error = location.error
                        Toast.makeText(this@MapsActivity, error, Toast.LENGTH_SHORT).show()
                    }
                    is Result.Loading->{}
                    is Result.Success->{
                       location.data.forEach {
                           val latLng = LatLng(it.lat, it.lon)
                           mMap.addMarker(
                               MarkerOptions()
                                   .position(latLng)
                                   .title(it.name)
                                   .snippet(it.description)
                           )
                           boundsBuilder.include(latLng)
                           val bounds: LatLngBounds = boundsBuilder.build()
                           mMap.animateCamera(
                               CameraUpdateFactory.newLatLngBounds(
                                   bounds,
                                   resources.displayMetrics.widthPixels,
                                   resources.displayMetrics.heightPixels,
                                   300
                               )
                           )
                       }
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_maps, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.day_maps->{
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.map_style_day))
            }
            R.id.night_maps->{
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.map_style))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}