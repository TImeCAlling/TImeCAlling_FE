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
import com.umc.timeCAlling.databinding.FragmentLocationSearchBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LocationSearchFragment : BaseFragment<FragmentLocationSearchBinding>(com.umc.timeCAlling.R.layout.fragment_location_search) {

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
        initRecentSearchRVA()
        initRecentBottomSheet()
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
            val keyword = binding.etLocationSearchLocation.text.toString()
            viewModel.addRecentSearch(keyword) // 검색어 추가
            Log.d("LocationSearchFragment", "눌렀잖아!!")
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
        searchResults.clear()
        val keyword = binding.etLocationSearchLocation.text.toString()
        Log.d("LocationSearchFragment", "찾기")
        if (keyword.isNotBlank()) {
            tmapData.findAllPOI(keyword) { poiItems ->
                if (poiItems != null && poiItems.isNotEmpty()) {
                    // 도로명 주소가 있는 POI만 필터링하고 최대 5개로 제한
                    val filteredPoiItems = poiItems.filter { it.newAddressList.isNotEmpty()}.take(5)

                    // 필터링된 검색 결과 출력
                    for ((index,poiItem) in filteredPoiItems.withIndex()) {
                        var i = 0
                        val latitude = poiItem.poiPoint.latitude
                        val longitude = poiItem.poiPoint.longitude

                        val roadNameAddress = poiItem.getPOIAddress()
                        Log.d("LocationSearchFragment", "장소명: ${poiItem.name}, 도로명 주소: $roadNameAddress")
                        val searchResult = SearchResult(poiItem.name, roadNameAddress, latitude, longitude)
                        searchResults.add(searchResult) // searchResults 리스트에 직접

                        val poiItem = filteredPoiItems[i]

                        val markerItem = TMapMarkerItem()
                        val point = TMapPoint(latitude, longitude)

                        // 마커 아이콘
                        try {
                            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.img_marker_test)
                            markerItem.icon = bitmap // 마커 아이콘 지정
                        } catch (e: Exception) {
                            Log.e("LocationSearchFragment", "마커 아이콘 로드 실패", e)
                            // 기본 마커 아이콘 사용 또는 오류 메시지 표시
                        }
                        markerItem.setPosition(0.5f, 1.0f)
                        markerItem.tMapPoint = point
                        markerItem.name = poiItem.name // poiItem.name을 마커 이름으로 설정

                        markerItem.id = "markerItem${index}" // index 사용

                        tMapView.addTMapMarkerItem(markerItem)


                        if (filteredPoiItems.isNotEmpty()) {
                            val firstPoiItem = filteredPoiItems[0] // 첫 번째 POIItem 가져오기
                            val firstPoiPoint = firstPoiItem.poiPoint

                            // 첫 번째 검색 결과의 위도와 경도를 사용하여 지도 중심점 설정
                            tMapView.setCenterPoint(firstPoiPoint.latitude, firstPoiPoint.longitude, true)
                            tMapView.setZoomLevel(15)
                        }

                        i++
                        Log.d("LocationSearchFragment", "마커 추가중~~")
                    }
                    viewModel.updateSearchResults(searchResults)
                    requireActivity().runOnUiThread {
                        initSearchResultRVA()
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

    private fun initRecentSearchRVA() {
        binding.bottomSheetRecentSearch.visibility = View.VISIBLE
        val recentSearchRVA = RecentSearchRVA(viewModel, viewLifecycleOwner)
        binding.rvRecentSearch.adapter = recentSearchRVA
        binding.rvRecentSearch.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initSearchResultRVA() {
        initResultBottomSheet()
        binding.bottomSheetSearchResult.visibility = View.VISIBLE
        binding.bottomSheetRecentSearch.visibility = View.GONE
        val searchResultRVA = SearchResultRVA(viewModel, viewLifecycleOwner) { searchResult ->
            val poiPoint = TMapPoint(searchResult.latitude, searchResult.longitude)
            val topMargin = 400 * resources.displayMetrics.density
            tMapView.setCenterPoint(poiPoint.latitude, poiPoint.longitude, true) // animate 매개변수 추가
            tMapView.setPadding(0, 0, 0, topMargin.toInt())
            tMapView.setZoomLevel(15)
        }
        binding.rvSearchResult.adapter = searchResultRVA
        binding.rvSearchResult.layoutManager = LinearLayoutManager(requireContext())
        Log.d("LocationSearchFragment", "결과")

    }

    private fun initRecentBottomSheet() {
        val bottomSheet = binding.bottomSheetRecentSearch // BottomSheet 레이아웃 ID
        recentBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        recentBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN // 초기 상태 숨김
        recentBottomSheetBehavior.peekHeight = 200
        recentBottomSheetBehavior.isHideable = false // 드래그하여 숨기기 설정
        recentBottomSheetBehavior.skipCollapsed = true // 숨김 상태로 바로 전환
    }

    private fun initResultBottomSheet() {
        val bottomSheet = binding.bottomSheetSearchResult// BottomSheet 레이아웃 ID
        resultBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        resultBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        resultBottomSheetBehavior.peekHeight = 200
        resultBottomSheetBehavior.isHideable = false // 드래그하여 숨기기 설정
        resultBottomSheetBehavior.skipCollapsed = true // 숨김 상태로 바로 전환
        Log.d("LocationSearchFragment", "결과 바텀")
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}
