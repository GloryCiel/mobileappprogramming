package com.example.runningapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.NaverMapSdk

class maps_for_test : AppCompatActivity(), OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps_for_test)

        // NaverMapSdk 초기화
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_MAPS_CLIENT_ID)

        // MapFragment 설정
        val fm = supportFragmentManager
        var mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance()
            fm.beginTransaction().add(R.id.map_fragment, mapFragment).commit()
        }
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(naverMap: NaverMap) {
        // 네이버 지도 준비 완료
    }
}