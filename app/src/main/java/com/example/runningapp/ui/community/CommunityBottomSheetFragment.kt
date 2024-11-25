package com.example.runningapp.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.runningapp.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CommunityBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var itemName: String
    private lateinit var itemDetail: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_community_bottom_sheet, container, false)

        // BottomSheet의 기본 높이 설정
        dialog?.setOnShowListener { dialog ->
            val bottomSheet = (dialog as BottomSheetDialog)
                .findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true

                // 상단 여백만큼 피크 높이 설정 (mypage_top_info_height 만큼 띄우기)
                val displayMetrics = resources.displayMetrics
                val screenHeight = displayMetrics.heightPixels
                val marginTop = resources.getDimensionPixelSize(R.dimen.top_info_height)
                behavior.peekHeight = screenHeight - marginTop
            }
        }

        val textItemName = view.findViewById<TextView>(R.id.text_item_name)
        val textItemDetail = view.findViewById<TextView>(R.id.text_item_detail)
        textItemName.text = itemName
        textItemDetail.text = itemDetail

        return view
    }

    fun setItemDetails(name: String, detail: String) {
        itemName = name
        itemDetail = detail
    }
}