package com.example.oms_android.login

data class LoginViewState(val username: String = "", val password: String = "") {
    val isLoginEnable: Boolean get() = username.isNotEmpty() && password.length >= 6
    val passwordTipVisible: Boolean get() = password.length in 1..5
}

sealed class LoginViewEvent {
    data class ShowToast(val message: String) : LoginViewEvent()
    object ShowLoadingDialog : LoginViewEvent()
    object DismissLoadingDialog : LoginViewEvent()
    object LoginSuccess : LoginViewEvent()
}

sealed class LoginViewAction {
    data class UpdateUserName(val username: String) : LoginViewAction()
    data class UpdatePassword(val password: String) : LoginViewAction()
    object Login : LoginViewAction()
}