package com.example.cibl_task.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.os.bundleOf
import com.example.cibl_task.base.BaseDialogFragment
import com.example.cibl_task.databinding.DialogPaymentBinding
import com.example.cibl_task.model.ActionType
import com.example.cibl_task.model.PaymentModel
import com.example.cibl_task.utils.DataSourceUtils
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.Objects


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
        binding.tvDownload.setOnClickListener { downloadPDF(ActionType.Save) }
        binding.imgShare.setOnClickListener { downloadPDF(ActionType.Share) }
    }

    private fun downloadPDF(type: ActionType) {
        val pdfDocument =
            DataSourceUtils.createPDF(DataSourceUtils.getViewToBitmap(binding.layoutInvoice))
        try {

                val contentResolver: ContentResolver = requireContext().contentResolver
                val contentValues = ContentValues()
                contentValues.put(
                    MediaStore.MediaColumns.DISPLAY_NAME,
                    "CIBL${DataSourceUtils.getCurrentDateTime()}.pdf"
                )
                contentValues.put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DOCUMENTS + File.separator + "CIBLFolder"
                )
                val pdfUri = contentResolver.insert(
                    MediaStore.Files.getContentUri("external"),
                    contentValues
                )

                val fos = contentResolver.openOutputStream(Objects.requireNonNull(pdfUri)!!)
                Objects.requireNonNull(fos)
                pdfDocument.writeTo(fos)
                pdfDocument.close()
                fos?.close()

                when (type) {
                    ActionType.Save -> {
                        downloadSuccess?.invoke().also { dismiss() }
                    }

                    ActionType.Share -> {
                        val share = Intent(Intent.ACTION_SEND)
                        share.setType("application/pdf")
                        share.putExtra(Intent.EXTRA_STREAM, pdfUri)
                        startActivity(Intent.createChooser(share, "Share Via"))
                    }
                }

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

