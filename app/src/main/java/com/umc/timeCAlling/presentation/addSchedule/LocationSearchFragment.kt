package com.umc.timeCAlling.presentation.addSchedule

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
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
import com.umc.timeCAlling.presentation.addSchedule.adapter.RecentSearchRVA
import com.umc.timeCAlling.presentation.addSchedule.adapter.SearchResultRVA
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.util.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LocationSearchFragment : BaseFragment<FragmentLocationSearchBinding>(com.umc.timeCAlling.R.layout.fragment_location_search) {

    private val viewModel: AddScheduleViewModel by activityViewModels() // ViewModel 초기화
    private val searchResults = mutableListOf<SearchResult>()
    private lateinit var recentBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var resultBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var tMapView: TMapView
    private lateinit var tmapData: TMapData
    private var mode : String = ""
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun initObserver() {

    }

    override fun initView() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        mode = viewModel.getMode()
        viewModel.setLocation(true)
        if(mode == "shared"){
            val locationName = viewModel.selectedLocationName.value ?: ""
            val longitude = viewModel.locationLongitude.value
            val latitude = viewModel.locationLatitude.value

            val location = SearchResult(locationName, "", latitude!!.toDouble(), longitude!!.toDouble())
            viewModel.updateSearchResults(listOf(location))

            findNavController().navigate(R.id.action_locationSearchFragment_to_locationResultFragment)
        }
        bottomNavigationRemove()
        initTMapView() // TMapView 초기화
        initRecentSearchRVA()
        initRecentBottomSheet()
        tmapData = TMapData()

        checkLocationPermission()


        binding.etLocationSearchLocation.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchLocation()
                true
            } else {
                false
            }
        }

        binding.ivLocationSearch.setOnClickListener {
            hideKeyboard(view ?: binding.root)
            searchLocation()
            val keyword = binding.etLocationSearchLocation.text.toString()
            viewModel.addRecentSearch(keyword) // 검색어 추가
        }

        binding.tvRecentSearchDelete.setOnClickListener {
            viewModel.clearRecentSearches()
        }

        binding.ivLocationSearchDelete.setOnClickListener {
            binding.etLocationSearchLocation.text.clear()
            binding.bottomSheetSearchResult.visibility = View.GONE
            binding.bottomSheetRecentSearch.visibility = View.VISIBLE
        }
        binding.ivLocationSearchBack.setOnSingleClickListener {
            findNavController().popBackStack()
        }
    }
    private fun hideKeyboard(view: View) {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // 권한이 이미 있는 경우
            Log.d("LocationSearchFragment", "Location permission already granted")
            getCurrentLocation()
        } else {
            // 권한이 없는 경우 권한 요청
            Log.d("LocationSearchFragment", "Requesting location permission")
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용된 경우
                Log.d("LocationSearchFragment", "Location permission granted by user")
                getCurrentLocation()
            } else {
                // 권한이 거부된 경우
                Log.d("LocationSearchFragment", "Location permission denied by user")
                // 권한이 거부되었을 때의 처리 (예: 사용자에게 권한이 필요한 이유를 설명)
            }
        }
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    Log.d("LocationSearchFragment", "Current Location in getCurrentLocation(): ${location.latitude}, ${location.longitude}")
                    viewModel.updateCurrentLocation(location)
                    tMapView.setOnMapReadyListener {
                        tMapView.setLocationPoint(location.latitude, location.longitude)
                        tMapView.setCenterPoint(location.latitude, location.longitude)
                        tMapView.setZoomLevel(15)
                    }
                }else {
                    Log.d("LocationSearchFragment", "Location is null")
                }
            }
        }
    }

    private fun bottomNavigationRemove() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
        bottomNavigationView?.visibility = View.GONE
    }

    private fun searchLocation() {
        searchResults.clear()
        val keyword = binding.etLocationSearchLocation.text.toString()
        Log.d("LocationSearchFragment", "찾기")
        if (keyword.isNotBlank()) {
            tmapData.findAllPOI(keyword) { poiItems ->
                if (poiItems != null && poiItems.isNotEmpty()) {
                    val filteredPoiItems = poiItems.filter { it.newAddressList.isNotEmpty()}.take(5)

                    for ((index,poiItem) in filteredPoiItems.withIndex()) {
                        var i = 0
                        val latitude = poiItem.poiPoint.latitude
                        val longitude = poiItem.poiPoint.longitude
                        val roadNameAddress = poiItem.getPOIAddress()
                        val searchResult = SearchResult(poiItem.name, roadNameAddress, latitude, longitude)
                        searchResults.add(searchResult)

                        val poiItem = filteredPoiItems[i]

                        val markerItem = TMapMarkerItem()
                        val point = TMapPoint(latitude, longitude)

                        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.img_marker_test)
                        markerItem.icon = bitmap

                        markerItem.setPosition(0.5f, 1.0f)
                        markerItem.tMapPoint = point
                        markerItem.name = poiItem.name

                        markerItem.id = "markerItem${index}" // index 사용

                        tMapView.addTMapMarkerItem(markerItem)


                        val firstPoiItem = filteredPoiItems[0] // 첫 번째 POIItem 가져오기
                        val firstPoiPoint = firstPoiItem.poiPoint
                        tMapView.setCenterPoint(firstPoiPoint.latitude-0.004, firstPoiPoint.longitude, true)
                        tMapView.setZoomLevel(15)

                        i++
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
        tMapView.setOnMapReadyListener {
            Log.d("LocationSearchFragment", "TMapView is ready")
            getCurrentLocation()
        }
    }

    private fun initRecentSearchRVA() {
        binding.bottomSheetRecentSearch.visibility = View.VISIBLE
        val recentSearchRVA = RecentSearchRVA(viewModel, viewLifecycleOwner){
            recentSearch ->
            binding.etLocationSearchLocation.setText(recentSearch)
            searchLocation()
        }
        binding.rvRecentSearch.adapter = recentSearchRVA
        binding.rvRecentSearch.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initSearchResultRVA() {
        initResultBottomSheet()
        binding.bottomSheetSearchResult.visibility = View.VISIBLE
        binding.bottomSheetRecentSearch.visibility = View.GONE

        val searchResultRVA = SearchResultRVA(
            viewModel,
            viewLifecycleOwner,
            ::moveToSearchResult, // Assuming this function handles the click,
            findNavController()
        )
        binding.rvSearchResult.adapter = searchResultRVA
        binding.rvSearchResult.layoutManager = LinearLayoutManager(requireContext())
        Log.d("LocationSearchFragment", "결과")
    }

    private fun initRecentBottomSheet() {
        val bottomSheet = binding.bottomSheetRecentSearch // BottomSheet 레이아웃 ID
        recentBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        recentBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN // 초기 상태 숨김
        recentBottomSheetBehavior.peekHeight = 400
        recentBottomSheetBehavior.isHideable = false // 드래그하여 숨기기 설정
        recentBottomSheetBehavior.skipCollapsed = true // 숨김 상태로 바로 전환
    }

    private fun initResultBottomSheet() {
        val bottomSheet = binding.bottomSheetSearchResult// BottomSheet 레이아웃 ID
        resultBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        resultBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        resultBottomSheetBehavior.peekHeight = 400
        resultBottomSheetBehavior.isHideable = false // 드래그하여 숨기기 설정
        resultBottomSheetBehavior.skipCollapsed = true // 숨김 상태로 바로 전환
        Log.d("LocationSearchFragment", "결과 바텀")
    }

    private fun moveToSearchResult(searchResult: SearchResult) {
        val poiPoint = TMapPoint(searchResult.latitude, searchResult.longitude)
        tMapView.setCenterPoint(poiPoint.latitude-0.004, poiPoint.longitude, true)
        tMapView.setZoomLevel(15)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}
