package com.example.shuttleserviceapp.feature.maps

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.shuttleserviceapp.R
import com.example.shuttleserviceapp.api.ApiInterface
import com.example.shuttleserviceapp.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapsBinding
    private lateinit var markerOptions: MarkerOptions
    private lateinit var marker: Marker
    private lateinit var cameraPosition: CameraPosition
    var defaultLongitude = -122.088426
    var defaultLatitude  = 37.388064
    private lateinit var googleMap: GoogleMap
    lateinit var pusher: Pusher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        markerOptions = MarkerOptions()
        val latLng = LatLng(defaultLatitude,defaultLongitude)
        markerOptions.position(latLng)
        cameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(17f).build()

        binding.simulateButton.setOnClickListener {
            callServerToSimulate()
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this@MapsActivity)
        setupPusher()
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    fun getRetrofitObject(): ApiInterface {
        val httpClient = OkHttpClient.Builder()
        val builder = Retrofit.Builder()
            .baseUrl("http://10.0.3.2:4000/")

        val retrofit = builder
            .client(httpClient.build())
            .build()
        return retrofit.create(ApiInterface::class.java)
    }

    private fun callServerToSimulate() {
        val jsonObject = JSONObject()
        jsonObject.put("latitude",defaultLatitude)
        jsonObject.put("longitude",defaultLongitude)

        val body = RequestBody.create(
            MediaType.parse("application/json"),
            jsonObject.toString()
        )

        getRetrofitObject().sendCoordinates(body).enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.d("TAG",response!!.body().toString())
            }

            override fun onFailure(call: Call<String>?, t: Throwable?) {
                Log.d("TAG",t!!.message.orEmpty())
            }
        })
    }

    private fun setupPusher() {
        val options = PusherOptions()
        options.setCluster("us-west-2")
        pusher = Pusher("1a2b3c4d5e6f7g8h9i0j", options)

        val channel = pusher.subscribe("my-channel")

        channel.bind("new-values") { channelName, eventName, data ->
            val jsonObject = JSONObject(data)
            val lat:Double = jsonObject.getString("latitude").toDouble()
            val lon:Double = jsonObject.getString("longitude").toDouble()

            runOnUiThread {
                val newLatLng = LatLng(lat, lon)
                marker.position = newLatLng
                cameraPosition = CameraPosition.Builder()
                    .target(newLatLng)
                    .zoom(17f).build()
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        pusher.connect()
    }

    override fun onPause() {
        super.onPause()
        pusher.disconnect()
    }

    override fun onMapReady(p0: GoogleMap) {
        this.googleMap = p0
        val latLng = LatLng(defaultLatitude, defaultLongitude)
        googleMap.addMarker(MarkerOptions().position(latLng).title("Default Location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
    }
}
