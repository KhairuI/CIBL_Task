package com.example.cibl_task.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cibl_task.databinding.ListItemPaymentBinding
import com.example.cibl_task.databinding.ListItemTextBinding
import com.example.cibl_task.model.PaymentTypeModel

class PaymentAdapter(
    private val listener: OnClickListener
) : RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {

    private var paymentTypeList: MutableList<PaymentTypeModel> = mutableListOf()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PaymentViewHolder {
        Log.d("xxx", "onCreateViewHolder")
        return PaymentViewHolder(
            ListItemTextBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        Log.d("xxx", "item count: ${paymentTypeList.size}")
        return if (paymentTypeList.isEmpty()) 0 else paymentTypeList.size
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val type = paymentTypeList[position]
        Log.d("xxx", "onBindViewHolder: $type")
        holder.binding.apply {

            type.title?.let { tvName.text = it }
          //  type.resource?.let { imgLogo.setImageResource(it) }

            tvName.setOnClickListener {
                listener.onClick(type)
            }
        }
    }

    class PaymentViewHolder(val binding: ListItemTextBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(paymentTypeList: MutableList<PaymentTypeModel>) {
        this.paymentTypeList = paymentTypeList
        notifyDataSetChanged()
        Log.d("xxx", "updateList: ${paymentTypeList.size}")
    }

    interface OnClickListener {
        fun onClick(type: PaymentTypeModel)
    }
}