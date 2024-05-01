package com.example.cibl_task.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.cibl_task.R
import com.example.cibl_task.base.BaseActivity
import com.example.cibl_task.databinding.ActivityPaymentBinding
import com.example.cibl_task.dialog.PaymentDialog
import com.example.cibl_task.model.PaymentModel
import com.example.cibl_task.model.PaymentTypeModel
import com.example.cibl_task.utils.DataSourceUtils

class PaymentActivity : BaseActivity() {

    // init all variable
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var type: PaymentTypeModel

    override fun getLayoutResourceId(): View {
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init(savedInstanceState: Bundle?) {

        getData()

        binding.imgBack.setOnClickListener { finish() }

        binding.btnConfirm.setOnClickListener {
            if (isValidate()) {
                setDialog()
            }
        }
        binding.imgLogo.setOnClickListener {
            binding.inputNumber.setText("01515267153")
            binding.inputName.setText("Khairul Islam")
            binding.inputAmount.setText("500")
            binding.inputNarration.setText("Fund Transfer")
        }
    }

    private fun setDialog() {
        val model = PaymentModel(
            type.title,
            type.resource,
            getNumber(),
            getName(),
            getAmount(),
            DataSourceUtils.readLocation(this),
            DataSourceUtils.getCurrentDateTime(),
            getNarration()
        )

        val dialog = PaymentDialog.newInstance(model)
        dialog.isCancelable = false
        dialog.downloadSuccess {
            Toast.makeText(this, "Download Successfully", Toast.LENGTH_SHORT).show()
            finish()
        }
        dialog.cancel { finish() }
        dialog.show(supportFragmentManager, PaymentDialog.TAG)


    }

    private fun getData() {
        type = getSerializable(intent, "type", PaymentTypeModel::class.java)

        binding.tvTitle.text = type.title

        binding.inputAmount.hint = getString(R.string.enter_amount)
        binding.inputNarration.hint = getString(R.string.narration)
        type.resource?.let { binding.imgLogo.setImageResource(it) }
        binding.inputNumber.hint = getString(R.string.enter_number, type.title)
        binding.inputName.hint = getString(R.string.enter_name, type.title)

    }

    private fun getNumber(): String = binding.inputNumber.text.toString()

    private fun getName(): String = binding.inputName.text.toString()

    private fun getAmount(): String = binding.inputAmount.text.toString()

    private fun getNarration(): String = binding.inputNarration.text.toString()


    private fun isValidate(): Boolean {
        if (getNumber().isEmpty()) {
            binding.layoutNumber.error = getString(R.string.missing_number)
            binding.inputNumber.requestFocus()
            return false
        }
        if (getNumber().length != 11) {
            binding.layoutNumber.error = getString(R.string.invalid_number)
            binding.inputNumber.requestFocus()
            return false
        }
        if (getName().isEmpty()) {
            binding.layoutName.error = getString(R.string.missing_name)
            binding.inputName.requestFocus()
            return false
        }
        if (getAmount().isEmpty()) {
            binding.layoutAmount.error = getString(R.string.missing_amount)
            binding.inputAmount.requestFocus()
            return false
        }
        if (getNarration().isEmpty()) {
            binding.layoutNarration.error = getString(R.string.missing_narration)
            binding.inputNarration.requestFocus()
            return false
        }

        return true
    }

}