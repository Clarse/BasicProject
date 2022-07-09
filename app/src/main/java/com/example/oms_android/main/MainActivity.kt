package com.example.oms_android.main

import com.example.oms_android.base.BaseActivity
import com.example.oms_android.databinding.ActivityMainBinding
import com.gyf.immersionbar.ktx.immersionBar

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override fun initView() {
        immersionBar {
            statusBarDarkFont(true, 0.2f)
        }
    }

    override fun initViewStates() {

    }

    override fun initViewEvents() {

    }


}