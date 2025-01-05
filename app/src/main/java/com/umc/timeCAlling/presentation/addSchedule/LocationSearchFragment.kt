package com.umc.timeCAlling.presentation.addSchedule

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.skt.tmap.TMapData
import com.skt.tmap.TMapTapi
import com.skt.tmap.TMapView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentLocationSearchBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationSearchFragment : BaseFragment<FragmentLocationSearchBinding>(R.layout.fragment_location_search) {

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
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
        bottomNavigationView?.visibility = View.GONE
    }

    private fun initTMapTapi() {
        tmapTapi = TMapTapi(requireContext())
    }

    private fun searchLocation() {
        val keyword = binding.etLocationSearchLocation.text.toString()
        if (keyword.isNotBlank()) {
            tmapData.findAllPOI(keyword) { poiItems ->
                if (poiItems != null && poiItems.size > 0) {
                    val poiItem = poiItems[0]
                    val poiPoint = poiItem.poiPoint
                    tMapView.setCenterPoint(poiPoint.longitude, poiPoint.latitude)
                    tMapView.setZoomLevel(15)
                }
            }
        }
    }

    private fun initTMapView() {
        tMapView = TMapView(requireContext())
        tMapView.setSKTMapApiKey(getString(R.string.tmap_app_key)) // T map API 키로 변경
        binding.tmapView.addView(tMapView)

        tMapView.setOnMapReadyListener { // MapEngine 초기화 완료 시 호출되는 콜백
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}
