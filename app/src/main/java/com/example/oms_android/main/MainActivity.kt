package com.example.oms_android.main

import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import com.example.oms_android.R
import com.example.oms_android.base.BaseActivity
import com.example.oms_android.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(),
    NavigationBarView.OnItemSelectedListener {


    override fun onStart() {
        super.onStart()
//        val navController = findNavController(R.id.nav_host_fragment)
//        vb.navView.setupWithNavController(navController)
    }

    override fun initView() {
        vb.viewPager.adapter = MainViewpagerAdapter(this)
        vb.navView.setOnItemSelectedListener(this)
        vb.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                vb.navView.menu.getItem(position).isChecked = true
            }
        })
        val badge = vb.navView.getOrCreateBadge(R.id.navigation_mine)
        badge.isVisible = true
        badge.number = 99
    }

    override fun initViewStates() {

    }

    override fun initViewEvents() {

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_document -> vb.viewPager.currentItem = 0
            R.id.navigation_report -> vb.viewPager.currentItem = 1
            R.id.navigation_mine -> vb.viewPager.currentItem = 2
        }
        return false
    }

}