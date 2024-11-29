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

class find_crew_item(
    val title:String,
    val content:String,
    val date:String,
    val img:String,
    val location:String
)

fun getLocations(context: Context): Array<String> {
    return context.resources.getStringArray(R.array.location_list)
}



class MyViewHolder(val binding: FindCrewItemRecyclerviewBinding) :
    RecyclerView.ViewHolder(binding.root)

class MyAdapter(val datas: MutableList<find_crew_item>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var filterdList:MutableList<find_crew_item> = datas

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
        binding.itemTitle.text = filterdList[position].title
        binding.itemDate.text = filterdList[position].date
        binding.itemContent.text = filterdList[position].content
        binding.itemLocation.text = filterdList[position].location

        // 아이템 클릭 리스너 추가
        holder.itemView.setOnClickListener {
            val bottomSheet = FindCrewBottomSheetFragment()
            bottomSheet.setItemDetails(filterdList[position].title, "상세 정보") // 필요한 상세 정보를 설정

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

        val datas = mutableListOf<find_crew_item>()
        for (i in 1..10) {
            datas.add(find_crew_item(
                "제목 $i",
                "상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용",
                "2024-$i-26",
                "res/drawable/ic_launcher_background.xml",
                "수성구"))
            datas.add(find_crew_item(
                "$i 제목",
                "상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용",
                "2024-$i-26",
                "res/drawable/ic_launcher_background.xml",
                "중구"))
        }

        val locations = getLocations(requireContext())
        val spinneradapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, locations)
        binding.selectLocation.adapter = spinneradapter

        binding.selectLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
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