package com.example.cibl_task.view

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.Location
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import java.util.Locale

class DashBoardActivity : BaseActivity() {

    // init all variable
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var paymentAdapter: PaymentAdapter
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentType = PaymentType.Bkash.id

    override fun getLayoutResourceId(): View {
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init(savedInstanceState: Bundle?) {
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)

        binding.cardBkash.setOnClickListener { checkLocationPermission(PaymentType.Bkash) }

        binding.cardNagad.setOnClickListener { checkLocationPermission(PaymentType.Nagad) }

        // setData()

    }

    private fun checkLocationPermission(type: PaymentType) {
        currentType = type.id
        if (PermissionUtils.checkGPSPermission(this)) {
            checkIsGPSEnable(currentType)

        } else {
            requestMultiplePermissions.launch(
                arrayOf(
                    PermissionUtils.ACCESS_FINE_LOCATION,
                    PermissionUtils.ACCESS_COARSE_LOCATION
                )
            )
        }

    }

    @SuppressLint("MissingPermission")
    private fun checkIsGPSEnable(type: Int) {
        if (PermissionUtils.isGpsEnable(this)) {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                val location = it.result
                DataSourceUtils.saveLocation(this,location)
                parsePaymentActivity(type)
            }

        } else {
            PermissionUtils.enableGps(this)
        }

    }

    private fun parsePaymentActivity(type: Int) {
        when (type) {
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

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
            if (permission[PermissionUtils.ACCESS_FINE_LOCATION] == true && permission[PermissionUtils.ACCESS_COARSE_LOCATION] == true) {
                checkIsGPSEnable(currentType)
            } else {
                Log.d("xxx", "Permission Not Granted")
            }

        }


    private fun setData() {
        /*paymentAdapter = PaymentAdapter(object : PaymentAdapter.OnClickListener {
            override fun onClick(type: PaymentTypeModel) {
                invokeActivity(PaymentActivity::class.java,"type",type)
            }
        })

        binding.listPayment.apply {
            setHasFixedSize(true)
            adapter = paymentAdapter
        }
        paymentAdapter.updateList(MyDataSource.paymentTypeList(this))*/
    }

}