package my.edu.tarc.listing_and_navigation_of_google_map.ui.map

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.room.Room
import com.example.madassignment3.ui.database.FacilityDatabase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import my.edu.tarc.listing_and_navigation_of_google_map.R
import my.edu.tarc.listing_and_navigation_of_google_map.databinding.ActivityMaps2Binding
import java.util.*

class MapsActivity2 : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap2: GoogleMap
    private lateinit var binding: ActivityMaps2Binding

    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMaps2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val connectivityManager : ConnectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected : Boolean = activeNetwork?.isConnectedOrConnecting == true
        if (isConnected){
            Toast.makeText(this, "Internet is Connected!! Pinning all location...", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this , "Internet is Not Connected!! Please connect to internet to view map",
                Toast.LENGTH_LONG).show()
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()

    }

    private fun fetchLocation() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1000)
            return

        }

        val task = fusedLocationProviderClient?.lastLocation
        task?.addOnSuccessListener { location->
            if(location != null){
                this.currentLocation = location
                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map2) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1000 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                fetchLocation()
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap2 = googleMap

        val database = Room.databaseBuilder(
            this, FacilityDatabase::class.java,"facility_database"
        )
            .allowMainThreadQueries()
            .build()

        val serviceList = database.facilityDao().getAllFacility()

        val boundsBuilder = LatLngBounds.builder()

        for (i in serviceList.indices) {

            val facilityPosition = LatLng(serviceList[i].latitude,serviceList[i].longitude)

            boundsBuilder.include(facilityPosition)

            mMap2.setInfoWindowAdapter(CustomInfoWindowForGoogleMap(this))

            mMap2.addMarker(MarkerOptions().position(facilityPosition).title(serviceList[i].facilityName).snippet(serviceList[i].facilityDesc))
        }

        val latlong = LatLng(currentLocation?.latitude!!, currentLocation?.longitude!!)
        boundsBuilder.include(latlong)
        drawMarker(latlong)

        mMap2.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(),1000,1000,200))



    }

    private fun drawMarker(latlong : LatLng) {
        val markerOption = MarkerOptions().position(latlong).title("Current Location")
            .snippet(getAddress(latlong.latitude, latlong.longitude)).icon(
                BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_AZURE))

        mMap2.addMarker(markerOption)
    }

    private fun getAddress(lat: Double, lon: Double): String {
        val geoCoder = Geocoder(this, Locale.getDefault())
        val addresses = geoCoder.getFromLocation(lat, lon, 1)
        return addresses[0].getAddressLine(0).toString()
    }
}