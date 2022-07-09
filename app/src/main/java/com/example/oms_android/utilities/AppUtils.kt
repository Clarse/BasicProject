package com.example.oms_android.utilities

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.MyApplication

fun Context.toast(message: String?, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun Int.asColor() = ContextCompat.getColor(MyApplication.getInstance(), this)