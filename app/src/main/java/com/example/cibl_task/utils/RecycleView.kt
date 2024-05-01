package com.example.cibl_task.utils

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cibl_task.base.BaseViewHolder


internal fun RecyclerView.with(
    adapt: RecyclerView.Adapter<BaseViewHolder>,
    decoration: RecyclerView.ItemDecoration? = null
) {
    with(this) {
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        itemAnimator = DefaultItemAnimator()
        decoration?.let { deco -> addItemDecoration(deco) }
        isNestedScrollingEnabled = true
        adapter = adapt
    }
}

internal fun RecyclerView.slide(
    adapt: RecyclerView.Adapter<BaseViewHolder>,
    decoration: RecyclerView.ItemDecoration? = null
) {
    with(this) {
        layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        itemAnimator = DefaultItemAnimator()
        decoration?.let { deco -> addItemDecoration(deco) }
        isNestedScrollingEnabled = true
        adapter = adapt
    }
}
