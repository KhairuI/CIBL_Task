package com.example.cibl_task.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.os.bundleOf
import com.example.cibl_task.base.BaseDialogFragment
import com.example.cibl_task.databinding.DialogPaymentBinding
import com.example.cibl_task.model.PaymentModel
import com.example.cibl_task.utils.DataSourceUtils
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class PaymentDialog : BaseDialogFragment() {


    private var _binding: DialogPaymentBinding? = null
    private val binding get() = _binding!!

    private var downloadSuccess: (() -> Unit)? = null
    private var cancel: (() -> Unit)? = null

    companion object {
        private const val KEY_MODEL = "key_model"
        const val TAG: String = "PaymentDialog"

        fun newInstance(model: PaymentModel?): PaymentDialog =
            PaymentDialog().apply {
                arguments = bundleOf(KEY_MODEL to model)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = DialogPaymentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    @SuppressLint("SetTextI18n")
    override fun setup(view: View) {

        getPaymentModel()?.let { model ->
            model.resource?.let { binding.imgLogo.setImageResource(it) }
            binding.tvAddress.text = "Address: ${model.address}"
            binding.tvPaymentMethodValue.text = model.title
            binding.tvAmountValue.text = model.amount
            binding.tvDateValue.text = model.dateTime
            binding.tvNarrationValue.text = model.narration
            binding.tvNumberValue.text = model.number
            binding.tvNameValue.text = model.name
            binding.tvTotalValue.text = model.amount

        }

        binding.imgCancel.setOnClickListener { cancel?.invoke().also { dismiss() } }
        binding.tvDownload.setOnClickListener { createPDF() }
        binding.imgShare.setOnClickListener { }
    }

    private fun createPDF() {
        var bitmap = DataSourceUtils.getViewToBitmap(binding.layoutInvoice)
        val document = PdfDocument()
        val pageInfo = PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = document.startPage(pageInfo)

        val canvas = page.canvas
        val paint = Paint()
        paint.color = Color.WHITE
        canvas.drawPaint(paint)

        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, true)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        document.finishPage(page)

        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val filePath = File(downloadsDir, "Payment_Receipt.pdf")

        try {
            val fos = FileOutputStream(filePath)
            document.writeTo(fos)
            document.close()
            fos.close()
            downloadSuccess?.invoke().also { dismiss() }

        } catch (e: FileNotFoundException) {
            Log.d("xxx", "Error while writing $e")
            throw RuntimeException(e)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }


    private fun getPaymentModel(): PaymentModel? =
        arguments?.serializable(KEY_MODEL, PaymentModel::class.java)

    fun downloadSuccess(downloadSuccess: () -> Unit) {
        this.downloadSuccess = downloadSuccess
    }

    fun cancel(cancel: () -> Unit) {
        this.cancel = cancel
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

