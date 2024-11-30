package com.example.runningapp.ui.community

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.runningapp.R
import com.example.runningapp.databinding.CommunityItemRecyclerviewBinding
import com.example.runningapp.databinding.FragmentCommunityBinding

class MyViewHolder(val binding: CommunityItemRecyclerviewBinding) :
    RecyclerView.ViewHolder(binding.root)

class MyAdapter(val datas: MutableList<CommunityItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder =
        MyViewHolder(
            CommunityItemRecyclerviewBinding.inflate(LayoutInflater.from(
                parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        val item = datas[position]
        
        // 각 뷰에 데이터 바인딩
        binding.itemTag.text = item.tag
        binding.itemTitle.text = item.title
        binding.itemContent.text = item.content

        // 아이템 클릭 리스너
        holder.itemView.setOnClickListener {
            val bottomSheet = CommunityBottomSheetFragment()
            bottomSheet.setItemDetails(
                title = item.title,
                content = item.content,
                userImage = item.userImage,
                userName = item.userName,
                userRank = item.userRank
            )
            bottomSheet.show((holder.itemView.context as AppCompatActivity).supportFragmentManager, bottomSheet.tag)
        }
    }
}

class CommunityFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentCommunityBinding.inflate(inflater, container, false)

        // Toolbar 숨기기
        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        val datas = mutableListOf<CommunityItem>()
        for (i in 1..10) {
            datas.add(CommunityItem(
                tag = "#태그${i}",
                title = "제목 ${i}",
                content = "게시글 내용 ${i}입니다. 여기에 더 많은 내용이 들어갈 수 있습니다. 다음은 긴 글에 대한 테스트입니다. item_recycle에 ... 이 보이면 됩니다.",
                userImage = R.drawable.icon_community,
                userName = "사용자${i}",
                userRank = "초보 러너"
            ))
        }

        val layoutManager = LinearLayoutManager(activity)
        binding.communityRecyclerview.layoutManager = layoutManager

        val adapter = MyAdapter(datas)
        binding.communityRecyclerview.adapter = adapter

        // fab 클릭 리스너 (write post 띄우기)
        binding.fabWrite.setOnClickListener {
            WritePostBottomSheetFragment().show(
                childFragmentManager,
                "WritePostBottomSheet"
            )
        }

        // viewModel 사용법 확인을 위해 남겨둠 (지우지 마세요)
//        val communityViewModel =
//            ViewModelProvider(this).get(CommunityViewModel::class.java)
//        val textView: TextView = binding.textCommunity
//        communityViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        return binding.root
    }
}