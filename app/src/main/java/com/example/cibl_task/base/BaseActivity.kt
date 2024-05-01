package com.example.cibl_task.base

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.cibl_task.model.PaymentTypeModel
import java.io.Serializable

@Suppress("UNCHECKED_CAST")
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getLayoutResourceId()?.let { setContentView(it) }
        init(savedInstanceState)
    }

    protected abstract fun init(savedInstanceState: Bundle?)

    protected abstract fun getLayoutResourceId(): View?

    fun invokeActivity(cls: Class<*>?, key: String, value: PaymentTypeModel) {
        val intent = Intent(this, cls).apply {
            putExtra(key, value)
        }
        startActivity(intent)
    }

    fun <T : Serializable?> getSerializable(intent: Intent, key: String, className: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= 33)
            intent.getSerializableExtra(key, className)!!
        else
            intent.getSerializableExtra(key) as T
    }


}