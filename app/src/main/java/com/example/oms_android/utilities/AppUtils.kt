package com.example.oms_android.utilities

import android.content.Context
import android.content.res.Resources
import android.util.Patterns
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.MyApplication
import com.google.android.material.snackbar.Snackbar

fun Context.toast(message: String?, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun Int.asColor() = ContextCompat.getColor(MyApplication.getInstance(), this)

fun Int.asDrawable() = ContextCompat.getDrawable(MyApplication.getInstance(), this)

//Email Validation
fun String.isValidEmail(): Boolean =
    this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun View.snackBarWithAction(
    message: String, actionLabel: String,
    block: () -> Unit
) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        .setAction(actionLabel) {
            block()
        }.show()
}

private val displayMetrics = Resources.getSystem().displayMetrics

//扩展属性
val Float.px get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, displayMetrics)

//转px
val Int.dp get() = this.toFloat().px

fun Float.dp2Px(context: Context): Int {
    val scale: Float = context.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

fun Float.px2dp(context: Context): Int {
    val scale: Float = context.resources.displayMetrics.density
    return (this / scale + 0.5f).toInt()
}