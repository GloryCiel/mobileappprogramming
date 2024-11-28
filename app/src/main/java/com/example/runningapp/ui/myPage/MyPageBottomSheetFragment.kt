package com.example.runningapp.ui.myPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.runningapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MyPageBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var itemName: String
    private lateinit var itemDetail: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_my_page_bottom_sheet, container, false)

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
