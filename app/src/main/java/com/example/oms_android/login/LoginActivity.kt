package com.example.oms_android.login

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import com.example.oms_android.R
import com.example.oms_android.base.BaseActivity
import com.example.oms_android.databinding.ActivityLoginBinding
import com.example.oms_android.utilities.toast
import com.zj.mvi.core.observeEvent
import com.zj.mvi.core.observeState

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    private lateinit var dialog: AlertDialog

    override fun initView() {
        vb.editUserName.addTextChangedListener {
            vm.dispatch(LoginViewAction.UpdateUserName(it.toString()))
        }
        vb.editPassword.addTextChangedListener {
            vm.dispatch(LoginViewAction.UpdatePassword(it.toString()))
        }
        vb.btnLogin.setOnClickListener {
            vm.dispatch(LoginViewAction.Login)
        }
    }

    override fun initViewStates() {
        vm.viewStates.let { state ->
            state.observeState(this, LoginViewState::username) {
                vb.editUserName.setText(it)
                vb.editUserName.setSelection(it.length)
            }
            state.observeState(this, LoginViewState::password) {
                vb.editPassword.setText(it)
                vb.editPassword.setSelection(it.length)
            }
            state.observeState(this, LoginViewState::isLoginEnable) {
                vb.btnLogin.isEnabled = it
                vb.btnLogin.alpha = if (it) 1f else 0.5f
            }
            state.observeState(this, LoginViewState::passwordTipVisible) {
                vb.tvLabel.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
    }

    override fun initViewEvents() {
        vm.viewEvents.observeEvent(this) {
            when (it) {
                is LoginViewEvent.ShowToast -> toast(it.message)
                is LoginViewEvent.ShowLoadingDialog -> showLoadingDialog()
                is LoginViewEvent.DismissLoadingDialog -> dismissLoadingDialog()
            }
        }
    }

    private fun showLoadingDialog() {
        progressDialog(this)
    }

    private fun dismissLoadingDialog() {
        dialog.takeIf { it.isShowing }?.dismiss()
    }

    private fun progressDialog(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null)
        dialog = AlertDialog.Builder(context).create()
        dialog.show()
        dialog.setCancelable(false)
        dialog.window?.let {
            it.decorView.setPadding(0, 0, 0, 0)
            val layoutParams = it.attributes
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            it.attributes = layoutParams
            it.setBackgroundDrawable(ColorDrawable(0))
            it.setContentView(view)
            it.setGravity(Gravity.CENTER)
        }
    }

}