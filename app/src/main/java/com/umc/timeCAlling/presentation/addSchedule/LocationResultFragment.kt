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
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.skt.tmap.TMapData
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapTapi
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.umc.timeCAlling.R
import com.umc.timeCAlling.data.SearchResult
import com.umc.timeCAlling.databinding.FragmentLocationResultBinding
import com.umc.timeCAlling.databinding.FragmentLocationSearchBinding
import com.umc.timeCAlling.presentation.addSchedule.adapter.LocationResultTransportationListVPA
import com.umc.timeCAlling.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LocationResultFragment : BaseFragment<FragmentLocationResultBinding>(R.layout.fragment_location_result) {

    private val viewModel: AddScheduleViewModel by activityViewModels() // ViewModel 초기화

    private var _transportationListVPA: LocationResultTransportationListVPA? = null
    private val transportationListVPA get() = _transportationListVPA

    private lateinit var tMapView: TMapView
    private lateinit var tmapTapi: TMapTapi
    private lateinit var tmapData: TMapData

    override fun initObserver() {

    }

    override fun initView() {
        bottomNavigationRemove()
        initLocationResultTransportationListVPAdapter()
        initTMapView()
    }

    private fun bottomNavigationRemove() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
        bottomNavigationView?.visibility = View.GONE
    }

    private fun initCarTime() {
        viewModel.currentLocation.observe(viewLifecycleOwner) {location ->
            if(location != null) {
                tmapData.findAroundNamePOI(TMapPoint(location.latitude, location.longitude), "내 위치",1000, 10){poiItems ->

                }
            }
        }
    }

    private fun initTMapView() {
        tMapView = TMapView(requireContext())
        tMapView.setSKTMapApiKey(getString(R.string.tmap_app_key)) // T map API 키로 변경
    }

    private fun initLocationResultTransportationListVPAdapter(){
        _transportationListVPA = LocationResultTransportationListVPA(this)
        with(binding){
            vpLocationResultTransportationList.adapter = transportationListVPA

            TabLayoutMediator(tabLocationResultTransportation, vpLocationResultTransportationList) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()

            tabLocationResultTransportation.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    viewModel.refreshCategoryPage(tab?.text?.toString() ?: "자동차")
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })
        }
    }

    companion object {
        private val tabTitles = listOf("자동차", "대중교통", "걷기")
    }
}
