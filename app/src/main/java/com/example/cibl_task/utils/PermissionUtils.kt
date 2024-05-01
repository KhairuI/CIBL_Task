package com.example.cibl_task.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task

object PermissionUtils {

    const val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    const val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    const val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
    const val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE

    // check GPS permission
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

    // check GPS permission
    fun checkStoragePermission(context: Context?): Boolean {
        return (context?.let {
            ContextCompat.checkSelfPermission(
                it,
                WRITE_EXTERNAL_STORAGE
            )
        } == PackageManager.PERMISSION_GRANTED) &&
                (context.let {
                    ContextCompat.checkSelfPermission(
                        it,
                        READ_EXTERNAL_STORAGE
                    )
                } == PackageManager.PERMISSION_GRANTED)

    }

    fun isGpsEnable(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun turnOnGPS(context: Context) {
        val request = LocationRequest.create().apply {
            interval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(request)
        val client: SettingsClient = LocationServices.getSettingsClient(context)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnFailureListener {
            if (it is ResolvableApiException) {
                try {
                    it.startResolutionForResult(context as Activity, 12345)
                } catch (_: IntentSender.SendIntentException) {

                }
            }
        }.addOnSuccessListener {
            //here GPS is On

        }
    }
}
