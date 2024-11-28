package com.example.runningapp.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // BottomSheet의 최소 높이 95%로 변경 (수동으로 맞춘 값)
        val displayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val minHeight = (screenHeight * 0.95).toInt()
        view.findViewById<LinearLayout>(R.id.bottom_sheet_container).minimumHeight = minHeight // screenHeight

        // BottomSheet의 동작 설정
        val behavior = (dialog as BottomSheetDialog)?.behavior
        behavior?.apply {
            // behavior 세팅 값
            state = BottomSheetBehavior.STATE_HALF_EXPANDED
            halfExpandedRatio = 0.8f
            isDraggable = true
            isHideable = true
            isFitToContents = true

            setPeekHeight((resources.displayMetrics.heightPixels * 0.5).toInt()) // 접힌 상태(STATE_COLLAPSED)의 높이

            // 시작 스크롤 위치 설정
            view.post {
                behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                view.findViewById<NestedScrollView>(R.id.nestedScrollView).scrollTo(0, 0)
            }
        }
    }

    fun setItemDetails(name: String, detail: String) {
        itemName = name
        itemDetail = detail
    }
}