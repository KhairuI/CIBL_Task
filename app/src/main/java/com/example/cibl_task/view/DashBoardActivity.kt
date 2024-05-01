package com.example.cibl_task.view

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.example.cibl_task.R
import com.example.cibl_task.adapter.PaymentAdapter
import com.example.cibl_task.base.BaseActivity
import com.example.cibl_task.databinding.ActivityDashboardBinding
import com.example.cibl_task.model.PaymentType
import com.example.cibl_task.model.PaymentTypeModel
import com.example.cibl_task.utils.DataSourceUtils
import com.example.cibl_task.utils.PermissionUtils
import com.example.cibl_task.utils.with
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class DashBoardActivity : BaseActivity() {

    // init all variable
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentType = PaymentType.Bkash.id
    private lateinit var paymentAdapter: PaymentAdapter

    override fun getLayoutResourceId(): View {
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init(savedInstanceState: Bundle?) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        paymentAdapter = PaymentAdapter()

        binding.listPayment.with(paymentAdapter.apply {
            clicked {
                currentType = it.type.id
                checkStoragePermissions()
            }
        })

        setData()
    }

    private fun checkStoragePermissions() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            // need to take storage permissions for android 11 or lower
            if (PermissionUtils.checkStoragePermission(this)) {
                checkLocationPermission()
            } else {
                requestStoragePermissions.launch(
                    arrayOf(
                        PermissionUtils.WRITE_EXTERNAL_STORAGE,
                        PermissionUtils.READ_EXTERNAL_STORAGE
                    )
                )
            }

        } else { checkLocationPermission() }
    }

    private fun checkLocationPermission() {
        if (PermissionUtils.checkGPSPermission(this)) {
            checkIsGPSEnable()

        } else {
            requestGpsPermissions.launch(
                arrayOf(
                    PermissionUtils.ACCESS_FINE_LOCATION,
                    PermissionUtils.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun checkIsGPSEnable() {
        if (PermissionUtils.isGpsEnable(this)) {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                val location = it.result
                DataSourceUtils.saveLocation(this, location).also {
                    parsePaymentActivity()
                }
            }

        } else {
            PermissionUtils.turnOnGPS(this)
        }
    }

    private fun parsePaymentActivity() {
        when (currentType) {
            PaymentType.Bkash.id -> {
                invokeActivity(
                    PaymentActivity::class.java, "type",
                    PaymentTypeModel(
                        resources.getString(R.string.bkash),
                        R.drawable.ic_bkash_logo,
                        PaymentType.Bkash
                    )
                )
            }

            PaymentType.Nagad.id -> {
                invokeActivity(
                    PaymentActivity::class.java, "type",
                    PaymentTypeModel(
                        resources.getString(R.string.nagad),
                        R.drawable.ic_nagad_logo,
                        PaymentType.Nagad
                    )
                )
            }
        }
    }

    private val requestGpsPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
            if (permission[PermissionUtils.ACCESS_FINE_LOCATION] == true && permission[PermissionUtils.ACCESS_COARSE_LOCATION] == true) {
                checkIsGPSEnable()
            } else {
                Log.d("xxx", "Permission Not Granted")
            }
        }

    private val requestStoragePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
            if (permission[PermissionUtils.WRITE_EXTERNAL_STORAGE] == true && permission[PermissionUtils.READ_EXTERNAL_STORAGE] == true) {
                checkLocationPermission()
            } else {
                Log.d("xxx", "Permission Not Granted")
            }

        }

    private fun setData() {
        paymentAdapter.submitList(DataSourceUtils.paymentTypeList(this))
    }
}