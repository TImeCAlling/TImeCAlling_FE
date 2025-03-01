package com.umc.timeCAlling.presentation.addSchedule

import android.graphics.PorterDuff
import android.util.Log
import android.view.View
import androidx.compose.ui.semantics.text
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentLocationResultBinding
import com.umc.timeCAlling.presentation.addSchedule.adapter.LocationResultTransportationListVPA
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.util.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LocationResultFragment : BaseFragment<FragmentLocationResultBinding>(R.layout.fragment_location_result) {

    private val viewModel: AddScheduleViewModel by activityViewModels() // ViewModel 초기화

    private var _transportationListVPA: LocationResultTransportationListVPA? = null
    private val transportationListVPA get() = _transportationListVPA

    override fun initObserver() {
    }

    override fun initView() {
        viewModel.scheduleTime.observe(viewLifecycleOwner) { scheduleTime ->
            binding.tvLocationResultTime.text = scheduleTime
        }

        bottomNavigationRemove()
        initLocationResultTransportationListVPAdapter()
        initSearchText()
        moveToLocationSearch()

        binding.ivLocationResultBack.setOnSingleClickListener {
            findNavController().popBackStack()
        }
    }

    private fun bottomNavigationRemove() {
        // BottomNavigationView 숨기기
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
        bottomNavigationView?.visibility = View.GONE

        // + 버튼 숨기기
        val addScheduleButton = requireActivity().findViewById<View>(R.id.iv_main_add_schedule_btn)
        addScheduleButton?.visibility = View.GONE

        val shadowImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_shadow)
        shadowImageView?.visibility = View.GONE

        val ovalImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_white_oval)
        ovalImageView?.visibility = View.GONE
    }

    private fun initLocationResultTransportationListVPAdapter(){
        _transportationListVPA = LocationResultTransportationListVPA(this)
        with(binding){
            vpLocationResultTransportationList.adapter = transportationListVPA

            TabLayoutMediator(tabLocationResultTransportation, vpLocationResultTransportationList) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()

            tabLocationResultTransportation.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewModel.refreshCategoryPage(tab.text?.toString() ?: "자동차")
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                }

                override fun onTabReselected(tab: TabLayout.Tab) {

                }


            })
        }
    }

    private fun initSearchText(){
        Log.d("selectedLocationName", "selectedLocationName: ${viewModel.selectedLocationName.value}")
        binding.tvLocationSearchEnd.text = viewModel.selectedLocationName.value
    }

    private fun moveToLocationSearch() {
        binding.tvLocationResultLocationSave.setOnClickListener {
            findNavController().navigate(R.id.action_locationResultFragment_to_addScheduleFragment)
            viewModel.setSelectedLocationName(binding.tvLocationSearchEnd.text.toString())
        }
    }

    companion object {
        private val tabTitles = listOf("자동차", "대중교통", "걷기")
    }
}
