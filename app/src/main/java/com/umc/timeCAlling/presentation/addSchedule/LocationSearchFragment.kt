package com.umc.timeCAlling.presentation.addSchedule

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.skt.tmap.TMapData
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapTapi
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentLocationSearchBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LocationSearchFragment : BaseFragment<FragmentLocationSearchBinding>(com.umc.timeCAlling.R.layout.fragment_location_search) {

    private lateinit var tMapView: TMapView
    private lateinit var tmapTapi: TMapTapi
    private lateinit var tmapData: TMapData

    override fun initObserver() {
        // 위치 권한 요청
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
        }
    }

    override fun initView() {
        bottomNavigationRemove()
        initTMapView() // TMapView 초기화
        initTMapTapi()
        tmapData = TMapData()

        // initView()에서 현재 위치 로그에 출력
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    Log.d("LocationSearchFragment", "Current Location in initView(): ${location.latitude}, ${location.longitude}")

                    tMapView.setOnMapReadyListener {
                        tMapView.setLocationPoint(location.latitude, location.longitude)
                        tMapView.setCenterPoint(location.latitude, location.longitude)
                        tMapView.setZoomLevel(15)
                    }
                }
            }
        }

        binding.etLocationSearchLocation.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchLocation()
                true
            } else {
                false
            }
        }

        binding.ivLocationSearch.setOnClickListener {
            searchLocation()
        }
    }

    private fun bottomNavigationRemove() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(com.umc.timeCAlling.R.id.main_bnv)
        bottomNavigationView?.visibility = View.GONE
    }

    private fun initTMapTapi() {
        tmapTapi = TMapTapi(requireContext())
    }

    private fun searchLocation() {
        val keyword = binding.etLocationSearchLocation.text.toString()
        Log.d("LocationSearchFragment", "찾기")
        if (keyword.isNotBlank()) {
            tmapData.findAllPOI(keyword) { poiItems ->
                if (poiItems != null && poiItems.isNotEmpty()) {
                    // 도로명 주소가 있는 POI만 필터링하고 최대 5개로 제한
                    val filteredPoiItems = poiItems.filter { it.newAddressList.isNotEmpty()}.take(5)

                    // 필터링된 검색 결과 출력
                    for (poiItem in filteredPoiItems) {
                        val roadNameAddress = poiItem.getPOIAddress()
                        Log.d("LocationSearchFragment", "장소명: ${poiItem.name}, 도로명 주소: $roadNameAddress")
                    }

                    // 첫 번째 검색 결과에 마커 표시 및 중심점 이동 (필터링된 결과 사용)
                    if (filteredPoiItems.isNotEmpty()) {
                        val poiItem = filteredPoiItems[0]
                        val poiPoint = poiItem.poiPoint

                        val markerItem1 = TMapMarkerItem()
                        val tMapPoint1 = TMapPoint(poiPoint.latitude, poiPoint.longitude)

                        // 마커 아이콘
                        try {
                            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.img_marker_test)
                            markerItem1.icon = bitmap // 마커 아이콘 지정
                        } catch (e: Exception) {
                            Log.e("LocationSearchFragment", "마커 아이콘 로드 실패", e)
                            // 기본 마커 아이콘 사용 또는 오류 메시지 표시
                        }
                        markerItem1.setPosition(0.5f, 1.0f)
                        markerItem1.tMapPoint = tMapPoint1
                        markerItem1.name = poiItem.name // poiItem.name을 마커 이름으로 설정

                        markerItem1.id = "markerItem1"

                        tMapView.addTMapMarkerItem(markerItem1)

                        tMapView.setLocationPoint(poiPoint.latitude, poiPoint.longitude)
                        tMapView.setCenterPoint(poiPoint.latitude, poiPoint.longitude)
                        tMapView.setZoomLevel(15)

                    } else {
                        Log.d("LocationSearchFragment", "검색 결과 없음")
                    }
                } else {
                    Log.d("LocationSearchFragment", "검색 결과 없음")
                }
            }
        }
    }

    private fun initTMapView() {
        tMapView = TMapView(requireContext())
        tMapView.setSKTMapApiKey(getString(R.string.tmap_app_key)) // T map API 키로 변경
        binding.tmapView.addView(tMapView)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}
