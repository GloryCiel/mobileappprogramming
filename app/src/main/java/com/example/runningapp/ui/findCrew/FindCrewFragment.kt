package com.example.runningapp.ui.findCrew

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.runningapp.R
import com.example.runningapp.databinding.FindCrewItemRecyclerviewBinding
import com.example.runningapp.databinding.FragmentFindCrewBinding

class MyViewHolder(val binding: FindCrewItemRecyclerviewBinding) :
    RecyclerView.ViewHolder(binding.root)

class MyAdapter(val datas: MutableList<FindCrewItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var filterdList:MutableList<FindCrewItem> = datas

    override fun getItemCount(): Int {
        return filterdList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder =
        MyViewHolder(
            FindCrewItemRecyclerviewBinding.inflate(LayoutInflater.from(
                parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        val item = filterdList[position]
        binding.itemTitle.text = item.title
        binding.itemDate.text = item.date
        binding.itemContent.text = item.content
        binding.itemLocation.text = item.location
        binding.itemImage.setImageResource(item.userImage)

        // 아이템 클릭 리스너 추가
        holder.itemView.setOnClickListener {
            val bottomSheet = FindCrewBottomSheetFragment()
            bottomSheet.setItemDetails(title = item.title,
                location = item.location,
                content = item.content,
                userImage = item.userImage,
                userName = item.userName,
                userRank = item.userRank) // 필요한 상세 정보를 설정

            // BottomSheet 보여주기
            bottomSheet.show((holder.itemView.context as AppCompatActivity).supportFragmentManager, bottomSheet.tag)
        }
    }

    fun filterData(location: String) {
        filterdList = if (location.isEmpty()) { datas }
        else { (datas.filter { it.location.contains(location) }).toMutableList() }

        notifyDataSetChanged()
    }
}

class FindCrewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFindCrewBinding.inflate(inflater, container, false)

        // Toolbar 숨기기
        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        val datas = mutableListOf<FindCrewItem>()
        for (i in 1..10) {
            datas.add(
                FindCrewItem(
                title = "제목 $i",
                content = "상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용",
                date = "2024-$i-26",
                userImage = R.drawable.icon_findcrew,
                userName = "사용자$i",
                userRank = "초보 러너",
                location = "수성구")
            )
            datas.add(FindCrewItem(
                title = "제목 $i",
                content = "상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용",
                date = "2024-$i-26",
                userImage = R.drawable.icon_findcrew,
                userName = "사용자$i",
                userRank = "초보 러너",
                location = "중구")
            )
        }

        val locations = FindCrewTags.locations.toMutableList()
        val spinneradapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, locations)
        binding.findCrewTopInfo.selectLocation.adapter = spinneradapter

        binding.findCrewTopInfo.selectLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLocation = locations[position]
                (binding.findcrewRecyclerview.adapter as? MyAdapter)?.filterData(selectedLocation)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                (binding.findcrewRecyclerview.adapter as? MyAdapter)?.filterData("")
            }
        }

        val layoutManager = LinearLayoutManager(activity)
        binding.findcrewRecyclerview.layoutManager = layoutManager

        val adapter = MyAdapter(datas)
        binding.findcrewRecyclerview.adapter = adapter

        return binding.root
    }
}