package com.example.cibl_task.base

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.cibl_task.utils.wrap
import java.io.Serializable

abstract class BaseDialogFragment : AppCompatDialogFragment() {

    override fun onResume() {
        super.onResume()
        dialog.wrap()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup(view)

    }

    abstract fun setup(view: View)

    inline fun <reified T : Serializable> Bundle.serializable(key: String, mClass: Class<T>): T? =
        when {
            Build.VERSION.SDK_INT >= 33 -> getSerializable(key, mClass)
            else -> @Suppress("DEPRECATION") getSerializable(key) as? T
        }

}