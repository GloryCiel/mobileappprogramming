package com.example.runningapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.runningapp.BuildConfig
import com.example.runningapp.databinding.FragmentHomeBinding
// naver api import
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.util.FusedLocationSource

class HomeFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap

    private var isRunning = false // 러닝 상태 여부 for runButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Naver Map SDK 초기화 (클라이언트 ID 설정)
        NaverMapSdk.getInstance(requireContext()).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_MAPS_CLIENT_ID)

        // MapView 초기화
        mapView = MapView(requireContext())
        binding.mapContainer.addView(mapView) // fragment_home.xml의 FrameLayout에 추가
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        // 버튼 클릭 리스너 설정
        binding.runButton.setOnClickListener {
            // runButton 내용을 "러닝 시작!" -> "러닝 중!"로 변경
            // 다시 이 버튼이 눌리면 Dialog 띄우기
            // Dialog에서 종료 선택 시, 초기화면으로 돌아감
            Toast.makeText(requireContext(), "Run Button Clicked", Toast.LENGTH_SHORT).show()
        }

        binding.moreInfo.setOnClickListener {
            // moreInfo 클릭 시, BottomSheet를 올려줌
            // 해당 Sheet 내에 추가정보와 토글된 아이콘들이 존재
            // 아이콘 선택 시, Naver Map에 해당 정보를 마커(혹은 다른 방식)로 띄워줌
            Toast.makeText(requireContext(), "More Info Clicked", Toast.LENGTH_SHORT).show()
        }

        binding.runInfo.setOnClickListener {
            // runInfo 클릭 시, BottomSheet를 올려줌
            // 해당 Sheet 내에 러닝 상태에 대한 상세 정보를 더 보여주고 끝
            Toast.makeText(requireContext(), "Run Info Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        // 추가로 Naver Map 설정 가능
    }

    // Fragment 생명주기에 따라 MapView 상태를 업데이트
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
