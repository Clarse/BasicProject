package com.example.oms_android.document

import com.example.oms_android.base.BaseFragment
import com.example.oms_android.databinding.FragmentDocumentBinding

/**
 * @author: Clarse
 * @date: 2022/7/11
 */
class DocumentFragment : BaseFragment<FragmentDocumentBinding, DocumentViewModel>() {

    override fun initView() {
        requireActivity().intent?.let {
            vb.tvLabel.text = "${it.getStringExtra("title")}\n" + "${it.getStringExtra("content")}"
        }
    }

    override fun initViewStates() {

    }

    override fun initViewEvents() {
    }
}