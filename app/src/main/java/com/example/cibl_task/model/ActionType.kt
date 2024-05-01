package com.example.cibl_task.model


sealed class ActionType(val id: Int) {
    data object Save : ActionType(0)
    data object Share : ActionType(1)
}
