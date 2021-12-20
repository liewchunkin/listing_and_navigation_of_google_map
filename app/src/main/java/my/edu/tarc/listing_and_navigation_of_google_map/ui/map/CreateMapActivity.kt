package my.edu.tarc.listing_and_navigation_of_google_map.ui.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import my.edu.tarc.listing_and_navigation_of_google_map.R
import my.edu.tarc.listing_and_navigation_of_google_map.databinding.ActivityCreateMapBinding
import java.io.IOException
import java.util.*

private const val TAG = "CreateMapActivity"
class CreateMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var markers: MutableList<Marker> = mutableListOf()
    private lateinit var binding: ActivityCreateMapBinding
    private val REQUEST_LOCATION_PERMISSION = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.idSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                val location: String = binding.idSearchView.query.toString()

                var addressList: List<Address>? = null

                val geocoder = Geocoder(this@CreateMapActivity)
                try {
                    addressList = geocoder.getFromLocationName(location, 1)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                val address: Address = addressList!![0]

                val latLng = LatLng(address.latitude, address.longitude)

                mMap.addMarker(MarkerOptions().position(latLng).title(location))

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))

                val intent = intent
                val name = intent.getStringExtra("Name")
                val desc = intent.getStringExtra("Desc")
                val sList = intent.getStringExtra("Service")

                val intent1 = Intent(this@CreateMapActivity, InsertFacility::class.java)
                intent1.putExtra("Name", name)
                intent1.putExtra("Desc", desc)
                intent1.putExtra("Service", sList)
                intent1.putExtra("La", latLng.latitude)
                intent1.putExtra("Long", latLng.longitude)
                finish()
                startActivity(intent1)

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map2) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mapFragment.view?.let {
            Snackbar.make(it, "Long press to pin the location!", Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", {})
                .setActionTextColor(ContextCompat.getColor(this, android.R.color.white))
                .show()
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
        mMap = googleMap

        mMap.setOnInfoWindowClickListener { markerToDelete ->
            Log.i(TAG, "onWindowClickListener- delete this marker")
            markers.remove(markerToDelete)
            markerToDelete.remove()
        }

//        mMap.setOnMapLongClickListener { latLng ->
//            Log.i(TAG, "setOnMapLongClickListener")
//            showAlertDialog(latLng)
//
//        }

        enableMyLocation()
        setMapLongClick(mMap)

    }

    // Called when user makes a long press gesture on the map.
    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            // A Snippet is Additional text that's displayed below the title.
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )

            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(getString(R.string.dropped_pin))
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )

            val intent = intent
            val name = intent.getStringExtra("Name")
            val desc = intent.getStringExtra("Desc")
            val sList = intent.getStringExtra("Service")

            val intent1 = Intent(this@CreateMapActivity, InsertFacility::class.java)
            intent1.putExtra("Name", name)
            intent1.putExtra("Desc", desc)
            intent1.putExtra("Service", sList)
            intent1.putExtra("La", latLng.latitude)
            intent1.putExtra("Long", latLng.longitude)
            finish()
            startActivity(intent1)
        }


    }

    // Checks that users have given permission
    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Checks if users have given their location and sets location enabled if so.
    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            mMap.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    // Callback for the result from requesting permissions.
    // This method is invoked for every call on requestPermissions(android.app.Activity, String[],
    // int).
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Check if location permissions are granted and if so enable the
        // location data layer.
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }

}