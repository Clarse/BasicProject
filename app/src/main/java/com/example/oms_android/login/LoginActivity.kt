package com.example.oms_android.login

import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.example.oms_android.R
import com.example.oms_android.base.BaseActivity
import com.example.oms_android.databinding.ActivityLoginBinding
import com.example.oms_android.notification.NotificationActivity
import com.example.oms_android.utilities.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zj.mvi.core.observeEvent
import com.zj.mvi.core.observeState

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    private lateinit var dialog: AlertDialog

    override fun initView() {
//        PermissionX.init(this)
//            .permissions(Manifest.permission.CAMERA)
//            .explainReasonBeforeRequest()
//            .onExplainRequestReason { scope, deniedList ->
//                scope.showRequestReasonDialog(
//                    deniedList,
//                    getString(R.string.permission_reason),
//                    getString(R.string.text_ok),
//                    getString(R.string.text_cancel)
//                )
//            }
//            .onForwardToSettings { scope, deniedList ->
//                scope.showForwardToSettingsDialog(
//                    deniedList,
//                    getString(R.string.permission_setting),
//                    getString(R.string.text_ok),
//                    getString(R.string.text_cancel)
//                )
//            }
//            .request { allGranted, grantedList, deniedList ->
//                if (allGranted) {
//                    toast(getString(R.string.permission_granted))
//                } else {
//                    toast("These permissions are denied: $deniedList")
//                }
//            }

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
                is LoginViewEvent.LoginSuccess -> startActivity(
                    Intent(
                        this,
                        NotificationActivity::class.java
                    )
                )
            }
        }
    }

    private fun showLoadingDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null)
        dialog = MaterialAlertDialogBuilder(this).setView(view).create()
        dialog.setCancelable(false)
        dialog.window?.let {
            val layoutParams = it.attributes
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            it.attributes = layoutParams
            it.setGravity(Gravity.CENTER)
        }
        dialog.show()
    }

    private fun dismissLoadingDialog() {
        dialog.takeIf { it.isShowing }?.dismiss()
    }

}