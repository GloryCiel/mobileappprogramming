package com.example.runningapp.ui.home

import android.app.AlertDialog
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.runningapp.BuildConfig
import com.example.runningapp.R
import com.example.runningapp.databinding.FragmentHomeBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PolylineOverlay
import com.naver.maps.map.util.FusedLocationSource
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import com.naver.maps.map.overlay.InfoWindow

class HomeFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    private var isRunning = false // 러닝 상태 여부 for runButton
    private var startTime: Long = 0
    private var totalDistance: Float = 0f
    private var lastLocation: Location? = null
    private var routePoints = mutableListOf<LatLng>()
    private var polyline: PolylineOverlay? = null

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                val elapsedTime = System.currentTimeMillis() - startTime
                val elapsedTimeInSeconds = elapsedTime / 1000
                val elapsedTimeInMinutes = elapsedTimeInSeconds / 60
                val elapsedTimeInHours = elapsedTimeInMinutes / 60
                val formattedTime = String.format("%02d:%02d:%02d", elapsedTimeInHours, elapsedTimeInMinutes % 60, elapsedTimeInSeconds % 60)

                binding.runInfo.text = "달린 시간: $formattedTime\n거리: ${totalDistance}m\n 페이스: ${String.format("%.1f", totalDistance/elapsedTimeInSeconds)} m/s"
                handler.postDelayed(this, 1000)
            }
        }
    }

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
        (activity as AppCompatActivity).supportActionBar?.hide()
        routePoints = mutableListOf()

        // Naver Map SDK 초기화 (클라이언트 ID 설정)
        NaverMapSdk.getInstance(requireContext()).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_MAPS_CLIENT_ID)

        // MapView 초기화
        mapView = MapView(requireContext())
        binding.mapContainer.addView(mapView) // fragment_home.xml의 FrameLayout에 추가
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        // FusedLocationSource 초기화
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        // 버튼 클릭 리스너 설정
        binding.runButton.setOnClickListener {
            if (isRunning) {
                // GPX 파일 이름 입력 다이얼로그 표시
                val input = EditText(requireContext())
                AlertDialog.Builder(requireContext())
                    .setTitle("경로 저장")
                    .setMessage("파일 이름을 입력하세요:")
                    .setView(input)
                    .setPositiveButton("저장") { _, _ ->
                        val fileName = input.text.toString()
                        if (fileName.isNotEmpty()) {
                            binding.runButton.text = "러닝 시작"
                            isRunning = false
                            binding.runInfo.isEnabled = false
                            handler.removeCallbacks(updateRunnable)
                            Toast.makeText(requireContext(), "러닝이 중지되었습니다.", Toast.LENGTH_SHORT).show()

                            // GPX 파일 저장
                            saveGpxFile("$fileName.gpx", routePoints, System.currentTimeMillis() - startTime, totalDistance)
                            Toast.makeText(requireContext(), "경로가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "파일 이름을 입력하세요.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("취소") { _, _ ->
                        // 일시정지된 업데이트 재개
                        handler.post(updateRunnable)
                    }
                    .show()

                // 업데이트 일시정지
                handler.removeCallbacks(updateRunnable)
            } else {
                binding.runButton.text = "러닝 중"
                naverMap.locationTrackingMode = LocationTrackingMode.Follow
                isRunning = true
                startTime = System.currentTimeMillis()
                totalDistance = 0f
                lastLocation = null
                routePoints.clear() // 경로 초기화
                binding.runInfo.isEnabled = true
                handler.post(updateRunnable)
                Toast.makeText(requireContext(), "러닝이 시작되었습니다.", Toast.LENGTH_SHORT).show()

                // 현재 위치를 경로에 추가
                val currentLocation = naverMap.locationOverlay.position
                routePoints.add(LatLng(currentLocation.latitude, currentLocation.longitude))
            }
        }

        binding.moreInfo.setOnClickListener {
            Toast.makeText(requireContext(), "More Info Clicked", Toast.LENGTH_SHORT).show()
        }

        binding.runInfo.isEnabled = false
    }

    private fun saveGpxFile(fileName: String, points: List<LatLng>, elapsedTime: Long, totalDistance: Float) {
        try {
            val gpxContent = buildString {
                append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                append("<gpx version=\"1.1\" creator=\"RunningApp\">\n")
                append("<trk>\n<trkseg>\n")
                for (point in points) {
                    append("<trkpt lat=\"${point.latitude}\" lon=\"${point.longitude}\"></trkpt>\n")
                }
                append("</trkseg>\n</trk>\n</gpx>")
            }

            val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val gpxFile = File(downloadDir, "$fileName.gpx")
            FileOutputStream(gpxFile).use {
                it.write(gpxContent.toByteArray())
            }

            val statsContent = buildString {
                val elapsedTimeInSeconds = elapsedTime / 1000
                val elapsedTimeInMinutes = elapsedTimeInSeconds / 60
                val elapsedTimeInHours = elapsedTimeInMinutes / 60
                val formattedTime = String.format("%02d:%02d:%02d", elapsedTimeInHours, elapsedTimeInMinutes % 60, elapsedTimeInSeconds % 60)
                val pace = totalDistance / elapsedTimeInSeconds

                append("달린 시간: $formattedTime\n")
                append("거리: ${totalDistance}m\n")
                append("페이스: ${String.format("%.1f", pace)} m/s\n")
            }

            val statsFile = File(downloadDir, "$fileName.txt")
            FileOutputStream(statsFile).use {
                it.write(statsContent.toByteArray())
            }

            Toast.makeText(requireContext(), "경로와 통계가 ${downloadDir.absolutePath}에 저장되었습니다.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "파일 저장에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        // 내 위치 표시 원의 크기 조정
        val locationOverlay = naverMap.locationOverlay
        locationOverlay.isVisible = true
        locationOverlay.circleColor = ContextCompat.getColor(requireContext(), R.color.blue)
        locationOverlay.circleOutlineColor = ContextCompat.getColor(requireContext(), R.color.outline_blue)
        locationOverlay.circleOutlineWidth = 5
        locationOverlay.circleRadius = 20

        // LocationButton 설정
        val locationButton = binding.root.findViewById<Button>(R.id.location_button)
        locationButton.setOnClickListener {
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
        }

        naverMap.addOnLocationChangeListener { location ->
            if (isRunning) {
                if (lastLocation != null) {
                    val distance = lastLocation!!.distanceTo(location).toInt()
                    if (distance > location.accuracy) {
                        totalDistance += distance
                        lastLocation = location
                        routePoints.add(LatLng(location.latitude, location.longitude)) // 현재 위치를 경로에 추가
                    }
                } else {
                    lastLocation = location
                    routePoints.add(LatLng(location.latitude, location.longitude)) // 첫 위치를 경로에 추가
                }
            }
        }

        // 여러 개의 GPX 파일을 불러와서 마커 추가
// 여러 개의 GPX 파일을 불러와서 마커 추가
        val gpxFiles = listOf("test.gpx", "school.gpx", "pretty_university_cross.gpx") // GPX 파일 목록
        for (fileName in gpxFiles) {
            val routePoints = parseGpxFile(fileName)
            if (routePoints.isNotEmpty()) {
                val startPoint = routePoints.first()
                val marker = Marker()
                marker.position = startPoint
                marker.map = naverMap

                // InfoWindow 설정
                val infoWindow = InfoWindow()
                infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(requireContext()) {
                    override fun getText(infoWindow: InfoWindow): CharSequence {
                        return fileName.removeSuffix(".gpx")
                    }
                }

                // 마커 클릭 리스너 설정
                marker.setOnClickListener {
                    if (polyline == null) {
                        // 전체 경로 표시
                        polyline = PolylineOverlay().apply {
                            coords = routePoints
                            color = Color.BLUE
                            map = naverMap
                        }
                        infoWindow.open(marker)
                    } else {
                        // 경로 제거
                        polyline?.map = null
                        polyline = null
                        infoWindow.close()
                    }
                    true
                }
            }
        }
    }

    private fun parseGpxFile(fileName: String): MutableList<LatLng> {
        val points = mutableListOf<LatLng>()
        try {
            val inputStream: InputStream = requireContext().assets.open(fileName)
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(inputStream, null)

            var eventType = parser.eventType
            var lat = 0.0
            var lon = 0.0

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val name = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (name == "rtept" || name == "trkpt") {
                            lat = parser.getAttributeValue(null, "lat").toDouble()
                            lon = parser.getAttributeValue(null, "lon").toDouble()
                            points.add(LatLng(lat, lon))
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return points
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
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

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}