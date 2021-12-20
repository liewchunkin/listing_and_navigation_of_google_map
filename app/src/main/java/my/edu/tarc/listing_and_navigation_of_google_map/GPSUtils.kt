package com.example.madassignment3

import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

class GPSUtils(context: Context) {
    private val TAG = "GPS"
    private val mContext: Context = context


    private var mSettingClient: SettingsClient? = null
    private var mLocationSettingsRequest: LocationSettingsRequest? = null

    private var mLocationManager: LocationManager? = null
    private var mLocationRequest: LocationRequest? = null


    init {
        mLocationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as? LocationManager

        mSettingClient = LocationServices.getSettingsClient(mContext)

        mLocationRequest = LocationRequest.create()
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        mLocationRequest?.interval = 1000
        mLocationRequest?.fastestInterval = 500


        if (mLocationRequest != null) {
            val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(mLocationRequest!!)
            mLocationSettingsRequest = builder.build()
        }
    }

    fun turnOnGPS() {
        if (mLocationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == false) {
            mSettingClient?.checkLocationSettings(mLocationSettingsRequest)
                ?.addOnSuccessListener(mContext as Activity) {
                    Log.d(TAG, "turnOnGPS: Already Enabled")
                }
                ?.addOnFailureListener { ex ->
                    if ((ex as ApiException).statusCode
                        == LocationSettingsStatusCodes.RESOLUTION_REQUIRED
                    ) {
                        try {
                            val resolvableApiException = ex as ResolvableApiException
                            resolvableApiException.startResolutionForResult(
                                mContext,
                                IConstant.DEFAULTS.GPS_CODE
                            )
                        } catch (e: Exception) {
                            Log.d(TAG, "turnOnGPS: Unable to start default functionality of GPS")
                        }

                    } else {
                        if (ex.statusCode == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE) {
                            val errorMessage =
                                "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings."
                            Log.e(TAG, errorMessage)
                            Toast.makeText(
                                mContext,
                                errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
        }
    }
}