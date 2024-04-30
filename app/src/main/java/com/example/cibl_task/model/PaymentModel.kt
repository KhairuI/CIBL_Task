package com.example.cibl_task.model

import java.io.Serializable


data class PaymentModel(
    var title: String? = null,
    var resource: Int? = null,
    var number: String? = null,
    var name: String? = null,
    var amount: String? = null,
    var address: String? = null,
    var dateTime: String? = null,
    var narration: String? = null
) : Serializable