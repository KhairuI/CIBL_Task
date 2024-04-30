package com.example.cibl_task.model

import java.io.Serializable

sealed class PaymentType(val id: Int) : Serializable {
    data object Bkash : PaymentType(0) {
        private fun readResolve(): Any = Bkash
    }

    data object Nagad : PaymentType(1) {
        private fun readResolve(): Any = Nagad
    }
}
