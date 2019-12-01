package com.teamtreehouse.berbyt.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import com.mapbox.mapboxsdk.Mapbox
import com.teamtreehouse.berbyt.R
import com.teamtreehouse.berbyt.data.*
import com.mapbox.mapboxsdk.maps.MapView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.GsonBuilder
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.location.LocationEngineRequest
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.Style.OnStyleLoaded
import com.mapbox.mapboxsdk.maps.Style.MAPBOX_STREETS
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import com.mapbox.mapboxsdk.style.layers.PropertyFactory

/*
* Author: Deana Mareková
* Description: MapActivity for show map
* Licence: MIT
* */
class MapActivity : BaseActivity(), OnMapReadyCallback, PermissionsListener {

    private var mapView: MapView? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mCloud: FirebaseFirestore
    private lateinit var mapboxMap: MapboxMap
    private var permissionsManager: PermissionsManager = PermissionsManager(this)

    val disposition_desc = mapOf(
        "1" to "garsonka",
        "2" to "1+kk",
        "3" to "1+1",
        "4" to "2+kk",
        "5" to "2+1",
        "6" to "3+kk",
        "7" to "3+1",
        "8" to "4+kk",
        "9" to "4+1",
        "16" to "atypický",
        "29" to "dúm",
        "5_and_more" to "5 a více"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(this, "pk.eyJ1IjoiZGVkdWxrYTIyIiwiYSI6ImNrMnh0MG55bzAwdmozYmt4MWY2emk5YTYifQ.5vLbencWHN4T_1UTJRTwvA")

        setContentView(R.layout.activity_map)

        initToolbar(R.id.toolbar)

        initLayout(R.layout.content_map)

        initBottomNav()

        mProgressBar = ProgressDialog(this)
        mAuth = FirebaseAuth.getInstance()
        mCloud = FirebaseFirestore.getInstance()


        mapView = findViewById(R.id.mapView)
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync(OnMapReadyCallback { mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS, object : Style.OnStyleLoaded {
                override fun onStyleLoaded(style: Style) {

                    val mapList = arrayListOf<MarkerOptions>()

                    var allLatS = arrayListOf<String>()
                    var allLotS = arrayListOf<String>()
                    var allNameS = arrayListOf<String>()
                    var allPriceS = arrayListOf<String>()

                    var allLatU = arrayListOf<String>()
                    var allLotU = arrayListOf<String>()
                    var allNameU = arrayListOf<String>()
                    var allPriceU = arrayListOf<String>()

                    var user = mAuth.currentUser!!.uid

                    val cacheFeeds = mCloud.collection("CacheFeeds").document(user)

                    cacheFeeds.get()
                        .addOnSuccessListener { document ->
                        if (document.data != null) {
                            val resSR = document["feedsSR"].toString()
                            val resUD = document["feedsUD"].toString()

                            val gson = GsonBuilder().create()
                            val srealityFeed = gson.fromJson(resSR, SrealityFeed::class.java)
                            val ulovdomovFeed = gson.fromJson(resUD,UlovDomovFeed::class.java)

                            var listSR = srealityFeed._embedded.estates
                            for (i in 0..listSR.size -1) {
                                allLatS.add(listSR[i].gps.getLat().toString())
                                allLotS.add(listSR[i].gps.getLon().toString())
                                allNameS.add(listSR[i].name.toString())
                                allPriceS.add(listSR[i].price.toString())

                                mapList.add(MarkerOptions()
                                    .position(LatLng(allLatS[i].toDouble(), allLotS[i].toDouble(), 0.0))
                                    .title("Sreality: " + allNameS[i])
                                    .setSnippet("cena: ${allPriceS[i]} Kč"))
                            }

                            var listUD = ulovdomovFeed.offers
                            for (i in 0..listUD.size -1) {
                                allLatU.add(listUD[i].lat.toString())
                                allLotU.add(listUD[i].lng.toString())

                                val disp_desc = listUD[i].disposition_id
                                val disposition = disposition_desc[disp_desc.toString()]
                                var nameUD = "Pronájem bytu " + disposition + " " + listUD[i].acreage + " m2"
                                allNameU.add(nameUD)
                                allPriceU.add(listUD[i].price_rental.toString())

                                mapList.add(MarkerOptions()
                                    .position(LatLng(allLatU[i].toDouble(), allLotU[i].toDouble(), 0.0))
                                    .title("UlovDomov: " + allNameU[i])
                                    .setSnippet("cena: ${allPriceU[i]} Kč"))
                            }

                            var latlot = LatLng(allLatS[0].toDouble(), allLotS[0].toDouble())
                            var latlot2 = LatLng(allLatS[1].toDouble(), allLotS[1].toDouble())
                            val latLngBounds = LatLngBounds.Builder()
                                .include(latlot)
                                .include(latlot2)
                                .build()

                            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10))
                            mapboxMap.addMarkers(mapList)

                        }
                    }
                }

            })

        })

    }

    /**
    * Set up the LocationEngine and the parameters for querying the device's location

    @SuppressLint("MissingPermission")
    private fun initLocationEngine() {
    val locationEngine = LocationEngineProvider.getBestLocationEngine(this)

    val  request = LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
    .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
    .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build()

    locationEngine.requestLocationUpdates(request, callback, getMainLooper())
    locationEngine.getLastLocation(callback)
    }*/

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.setStyle(Style.Builder().fromUri(
            "mapbox://styles/mapbox/cjerxnqt3cgvp2rmyuxbeqme7")) {

            // Map is set up and the style has loaded. Now you can add data or make other map adjustments
            enableLocationComponent(it)
        }
    }



    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: Style) {
    // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

        // Create and customize the LocationComponent's options
            val customLocationComponentOptions = LocationComponentOptions.builder(this)
                .trackingGesturesManagement(true)
                .accuracyColor(ContextCompat.getColor(this, R.color.com_facebook_blue))
                .build()

            val locationComponentActivationOptions = LocationComponentActivationOptions.builder(this, loadedMapStyle)
                .locationComponentOptions(customLocationComponentOptions)
                .build()

        // Get an instance of the LocationComponent and then adjust its settings
            mapboxMap.locationComponent.apply {

                // Activate the LocationComponent with options
                activateLocationComponent(locationComponentActivationOptions)

        // Enable to make the LocationComponent visible
                isLocationComponentEnabled = true

        // Set the LocationComponent's camera mode
                cameraMode = CameraMode.TRACKING

        // Set the LocationComponent's render mode
                renderMode = RenderMode.COMPASS
            }
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
        Toast.makeText(this, "onExplanationNeeded", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocationComponent(mapboxMap.style!!)
        } else {
            Toast.makeText(this, "onPermissionResult", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    public override fun onStart() {
        super.onStart()
        mapView!!.onStart()
    }

    public override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    public override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    public override fun onStop() {
        super.onStop()
        mapView!!.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }
}

