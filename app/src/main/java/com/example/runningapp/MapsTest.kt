package com.example.runningapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.naver.maps.map.*
import com.naver.maps.map.util.FusedLocationSource

class MapsTest : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap

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

        // 위치 소스 초기화
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        // 위치 소스 설정
        naverMap.locationSource = locationSource

        // 위치 권한 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
        }

        // 현재 위치를 나타내는 LocationOverlay 설정
        val locationOverlay = naverMap.locationOverlay
        locationOverlay.isVisible = true
        locationOverlay.circleColor = ContextCompat.getColor(this, R.color.blue) // 원하는 색상으로 변경
        locationOverlay.circleOutlineColor = ContextCompat.getColor(this, R.color.outline_blue) // 원하는 외곽선 색상으로 변경
        locationOverlay.circleOutlineWidth = 5 // 외곽선 두께 설정
        locationOverlay.circleRadius = 20 // 원의 반지름 설정

        // 현위치 버튼 활성화
        val uiSettings = naverMap.uiSettings
        uiSettings.isLocationButtonEnabled = true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                naverMap.locationTrackingMode = LocationTrackingMode.Follow
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}