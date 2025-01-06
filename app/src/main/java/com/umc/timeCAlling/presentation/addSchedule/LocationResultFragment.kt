package com.umc.timeCAlling.presentation.addSchedule

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.skt.tmap.TMapData
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapTapi
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.umc.timeCAlling.R
import com.umc.timeCAlling.data.SearchResult
import com.umc.timeCAlling.databinding.FragmentLocationResultBinding
import com.umc.timeCAlling.databinding.FragmentLocationSearchBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LocationResultFragment : BaseFragment<FragmentLocationResultBinding>(R.layout.fragment_location_result) {

    private val viewModel: AddScheduleViewModel by activityViewModels() // ViewModel 초기화
    private val searchResults = mutableListOf<SearchResult>()
    private lateinit var recentBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var resultBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
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
    }

    private fun bottomNavigationRemove() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
        bottomNavigationView?.visibility = View.GONE
    }

    private fun initTMapTapi() {
        tmapTapi = TMapTapi(requireContext())
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
