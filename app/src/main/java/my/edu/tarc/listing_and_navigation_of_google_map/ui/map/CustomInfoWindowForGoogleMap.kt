package my.edu.tarc.listing_and_navigation_of_google_map.ui.map

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import my.edu.tarc.listing_and_navigation_of_google_map.R

class CustomInfoWindowForGoogleMap(context: Context) : GoogleMap.InfoWindowAdapter {

    @SuppressLint("InflateParams")
    var mWindow: View = (context as Activity).layoutInflater.inflate(R.layout.info_window_map, null)

    private fun rendowWindowText(marker: Marker, view: View){

        val tvTitle = view.findViewById<TextView>(R.id.title)
        val tvSnippet = view.findViewById<TextView>(R.id.snippet)

        tvTitle.text = marker.title
        tvSnippet.text = marker.snippet

    }

    override fun getInfoContents(marker: Marker): View {
        rendowWindowText(marker, mWindow)
        return mWindow
    }

    override fun getInfoWindow(marker: Marker): View {
        rendowWindowText(marker, mWindow)
        return mWindow
    }
}