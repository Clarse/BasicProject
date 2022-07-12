package com.example.oms_android.main

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.oms_android.document.DocumentFragment
import com.example.oms_android.mine.MineFragment
import com.example.oms_android.report.ReportFragment

/**
 * @author: Clarse
 * @date: 2022/7/12
 */
const val DOCUMENT_PAGE_INDEX = 0
const val REPORT_PAGE_INDEX = 1
const val MINE_PAGE_INDEX = 2

class MainViewpagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    private val fragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        DOCUMENT_PAGE_INDEX to { DocumentFragment() },
        REPORT_PAGE_INDEX to { ReportFragment() },
        MINE_PAGE_INDEX to { MineFragment() }
    )

    override fun getItemCount(): Int = fragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return fragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }

}