package com.example.cibl_task.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.cibl_task.R
import com.example.cibl_task.base.BaseViewHolder
import com.example.cibl_task.databinding.ListItemPaymentBinding
import com.example.cibl_task.model.PaymentTypeModel

class PaymentAdapter : ListAdapter<PaymentTypeModel, BaseViewHolder>(DiffCallback) {

    private var clicked: ((type: PaymentTypeModel) -> Unit)? = null

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) = holder.onBind(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_payment, parent, false)
    )

    inner class ViewHolder(view: View) : BaseViewHolder(view) {
        private val binding = ListItemPaymentBinding.bind(view)

        override fun clear() {
            binding.tvName.text = ""
        }

        override fun onBind(position: Int) {
            with(getItem(position)) {
                title?.let { binding.tvName.text = it }
                resource?.let { binding.imgLogo.setImageResource(it) }

                binding.cardBody.setOnClickListener {
                    clicked?.invoke(this)
                }
            }
        }
    }

    fun clicked(clicked: (type: PaymentTypeModel) -> Unit) {
        this.clicked = clicked
    }

    private object DiffCallback : DiffUtil.ItemCallback<PaymentTypeModel>() {
        override fun areItemsTheSame(
            oldItem: PaymentTypeModel,
            newItem: PaymentTypeModel
        ): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: PaymentTypeModel,
            newItem: PaymentTypeModel
        ): Boolean =
            oldItem == newItem
    }
}

