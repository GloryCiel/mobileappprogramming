package com.example.runningapp.ui.myPage

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
import com.example.runningapp.databinding.FragmentMyPageBinding
import com.example.runningapp.databinding.MyPageItemRecyclerviewBinding

class MyViewHolder(val binding: MyPageItemRecyclerviewBinding) :
    RecyclerView.ViewHolder(binding.root)

class MyAdapter(val datas: MutableList<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder =
        MyViewHolder(
            MyPageItemRecyclerviewBinding.inflate(LayoutInflater.from(
                parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        binding.itemMypage.text = datas[position]

        // 아이템 클릭 리스너 추가
        holder.itemView.setOnClickListener {
            val bottomSheet = MyPageBottomSheetFragment()
            bottomSheet.setItemDetails(datas[position], "상세 정보") // 필요한 상세 정보를 설정

            // BottomSheet 보여주기
            bottomSheet.show((holder.itemView.context as AppCompatActivity).supportFragmentManager, bottomSheet.tag)
        }
    }
}

class MyDecoration(val context: Context): RecyclerView.ItemDecoration() {
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val width = parent.width
        val height = parent.height

        val dr: Drawable? = ResourcesCompat.getDrawable(context.getResources(),
            R.drawable.icon_mypage, null)
        val drWidth = dr?.intrinsicWidth
        val drHeight = dr?.intrinsicHeight

        val left = width / 2 - drWidth?.div(2) as Int
        val top = height / 2 - drHeight?.div(2) as Int

        c.drawBitmap(
            BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_mypage),
            left.toFloat(),
            top.toFloat(),
            null
        )


    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val index = parent.getChildAdapterPosition(view) + 1
        if (index % 3 == 0) // left, top, right, bottom
            outRect.set(10, 10, 10, 60)
        else
            outRect.set(10, 10, 10, 0)
        view.setBackgroundColor(Color.parseColor("#28A0FF"))
        ViewCompat.setElevation(view, 20.0f)
    }
}

class MyPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMyPageBinding.inflate(inflater, container, false)

        // Toolbar 숨기기
        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        val datas = mutableListOf<String>()
        for (i in 1..10) {
            datas.add("Item $i")
        }

        val layoutManager = LinearLayoutManager(activity)
        binding.mypageRecyclerview.layoutManager = layoutManager

        val adapter = MyAdapter(datas)
        binding.mypageRecyclerview.adapter = adapter
        binding.mypageRecyclerview.addItemDecoration(MyDecoration(activity as Context))

        return binding.root
    }
}