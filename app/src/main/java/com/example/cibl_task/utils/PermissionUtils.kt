package com.example.cibl_task.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.util.Log
import com.google.android.gms.location.LocationRequest
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes

object PermissionUtils {

    const val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    const val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION

    // check permission
    fun checkGPSPermission(context: Context?): Boolean {
        return (context?.let {
            ContextCompat.checkSelfPermission(
                it,
                ACCESS_FINE_LOCATION
            )
        } == PackageManager.PERMISSION_GRANTED) &&
                (context.let {
                    ContextCompat.checkSelfPermission(
                        it,
                        ACCESS_COARSE_LOCATION
                    )
                } == PackageManager.PERMISSION_GRANTED)

    }

    fun isGpsEnable(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun enableGps(context: Context) {
        val mSettingsClient = LocationServices.getSettingsClient(context)
        val mLocationSettingsRequest: LocationSettingsRequest?
        val mLocationRequest = LocationRequest.create()
        mLocationRequest.apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = (30 * 1000)
            fastestInterval = (5 * 1000)
        }
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        mLocationSettingsRequest = builder.build()
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest).addOnSuccessListener {
            Log.d("xxx", "enableGps: Already enable")
        }.addOnFailureListener {
            if ((it as com.google.android.gms.common.api.ApiException).statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {

                try {
                    val resolvableApiException = it as ResolvableApiException
                    resolvableApiException.startResolutionForResult(context as Activity, 101)
                } catch (e: Exception) {
                    Log.d("xxx", "enableGps: Unable to start")
                }

            } else {

                if ((it as com.google.android.gms.common.api.ApiException).statusCode == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE) {
                    Log.d("xxx", "enableGps: An error occured")
                }
            }
        }
    }

    interface OnClickListener {
        fun onClick(value: Boolean)
    }
}
