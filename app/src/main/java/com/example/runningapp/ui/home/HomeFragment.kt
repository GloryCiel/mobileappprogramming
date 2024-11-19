package com.example.runningapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.runningapp.BuildConfig
import com.example.runningapp.databinding.FragmentHomeBinding
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.NaverMapSdk

class HomeFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // API 키 설정 - 로컬 프로퍼티에서 가져오기
        NaverMapSdk.getInstance(requireContext()).setClient(
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_MAPS_CLIENT_ID)
        )

        // MapView 초기화
        mapView = MapView(requireContext())
        val mapContainer: FrameLayout = binding.mapContainer
        mapContainer.addView(mapView) // FrameLayout에 MapView 추가

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        homeViewModel.text.observe(viewLifecycleOwner) {
        }
        return root
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        // 카메라 설정 코드 제거
    }

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