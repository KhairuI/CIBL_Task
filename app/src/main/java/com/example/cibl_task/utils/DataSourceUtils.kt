package com.example.cibl_task.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.view.View
import com.example.cibl_task.R
import com.example.cibl_task.model.PaymentType
import com.example.cibl_task.model.PaymentTypeModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object DataSourceUtils {

    fun paymentTypeList(context: Context): MutableList<PaymentTypeModel> {
        return mutableListOf(
            PaymentTypeModel(
                context.getString(R.string.bkash),
                R.drawable.ic_bkash_logo,
                PaymentType.Bkash
            ),
            PaymentTypeModel(
                context.getString(R.string.nagad),
                R.drawable.ic_nagad_logo,
                PaymentType.Nagad
            )
        )
    }

    @SuppressLint("CommitPrefEdits")
    fun saveLocation(context: Context, location: Location) {
        val geocoder = Geocoder(context, Locale.getDefault())
        val address = geocoder.getFromLocation(location.latitude, location.longitude, 5)
        val exactAddress = "${address?.get(0)?.getAddressLine(0)},${address?.get(0)?.countryName}"
        val mPrefs = context.getSharedPreferences("shared_db", Context.MODE_PRIVATE)
        mPrefs.edit().putString("address", exactAddress).apply()

    }

    fun readLocation(context: Context): String? {
        val mPrefs = context.getSharedPreferences("shared_db", Context.MODE_PRIVATE)
        return mPrefs.getString("address", "")
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        return sdf.format(Date())
    }

    fun getViewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val bgDrawable = view.background

        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return bitmap
    }

    fun createPDF(bitmap:Bitmap): PdfDocument {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()
        paint.color = Color.WHITE
        canvas.drawPaint(paint)
        val newBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, true)
        canvas.drawBitmap(newBitmap, 0f, 0f, null)
        document.finishPage(page)
        return document
    }


}

