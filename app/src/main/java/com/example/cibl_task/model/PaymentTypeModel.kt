package com.example.cibl_task.model

import java.io.Serializable


data class PaymentTypeModel(
    var title: String? = null,
    var resource: Int? = null,
    var type: PaymentType? = null
):Serializable