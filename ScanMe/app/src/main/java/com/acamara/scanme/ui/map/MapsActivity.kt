package com.acamara.scanme.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ccy.focuslayoutmanager.FocusLayoutManager
import ccy.focuslayoutmanager.FocusLayoutManager.dp2px
import com.acamara.scanme.R
import com.acamara.scanme.adapters.NearProductRecyclerViewAdapter
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_maps.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    private var wayLatitude :Double = 0.0
    private val REQUEST_PERMISSION_LOCATION = 991
    private  var wayLongitude :Double = 0.0
    private var googleApiClient: GoogleApiClient? = null
    private var lastKnownLocation: Location? = null
    private val UPDATE_INTERVAL = 10 * 1000 /* 10 secs */.toLong()
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */
    private var focusLayoutManager : FocusLayoutManager? = null
    private var recyclerView : RecyclerView? = null
    private lateinit var mMap: GoogleMap
    private lateinit var mapViewModel: MapsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setupGoogleApiClient();
        supportActionBar?.hide()
        //var adapter = NearProductRecyclerViewAdapter()
        mapViewModel =
            ViewModelProvider(this).get(MapsViewModel::class.java)
        var adapter = NearProductRecyclerViewAdapter()
//        var linearLayoutManager = LinearLayoutManager(this)
//        linearLayoutManager.orientation = RecyclerView.HORIZONTAL
//        product_near_view.layoutManager = linearLayoutManager
        focusLayoutManager = FocusLayoutManager.Builder()
            .layerPadding(dp2px(this, 2f))
            .normalViewGap(dp2px(this, 2f))
            .focusOrientation(FocusLayoutManager.FOCUS_LEFT)
            .isAutoSelect(true)
            .maxLayerCount(1)
            .setOnFocusChangeListener { focusdPosition, lastFocusdPosition -> }
            .build()
        product_near_view.adapter = adapter
        product_near_view.layoutManager = focusLayoutManager
        mapViewModel.productList.observe(this, Observer { productList ->
            System.out.println(productList.size)
            productList.let { adapter.setProduct(it) }
        })

    }

    private fun setupGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .build()
        }
    }

    override fun onStart() {
        super.onStart()
        googleApiClient?.connect();
    }

    //method for taking user permission
    private fun requestPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {

                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {

                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied) {
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap= googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        updateLastLocation();
        //setupMapOnCameraChange();
    }
    private fun updateLastLocation() {
        System.out.println("Updating the location")
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSION_LOCATION
            )
            return
        }
        lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(
            googleApiClient
        )
        mMap.setMyLocationEnabled(true)
        if (lastKnownLocation != null) {
            mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(lastKnownLocation!!.getLatitude(), lastKnownLocation!!.getLongitude()),
                    15f
                )
            )
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15f))
        }
    }

    override fun onConnected(p0: Bundle?) {
        updateLastLocation();
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }
}