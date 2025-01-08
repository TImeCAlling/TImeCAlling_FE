package com.umc.timeCAlling.presentation.addSchedule.list

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.timeCAlling.R
import com.umc.timeCAlling.data.LocationResultType
import com.umc.timeCAlling.databinding.FragmentLocationResultTransportationListBinding
import com.umc.timeCAlling.presentation.addSchedule.AddScheduleViewModel
import com.umc.timeCAlling.presentation.addSchedule.adapter.LocationResultRVA
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.util.extension.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalkTransportationListFragment : BaseFragment<FragmentLocationResultTransportationListBinding>(R.layout.fragment_location_result_transportation_list) {

    private val viewModel: AddScheduleViewModel by activityViewModels()
    private var _locationResultRVA: LocationResultRVA? = null
    private val locationResultRVA get() = _locationResultRVA

    override fun initObserver() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.currentLocation.value?.let { currentLocation -> // let 블록 사용
                viewModel.searchLocation.value?.get(0)?.let { searchLocation -> // let 블록 사용
                    val startX = currentLocation.longitude
                    val startY = currentLocation.latitude
                    val endX = searchLocation.longitude
                    val endY = searchLocation.latitude
                    viewModel.getWalkTransportation(startX, startY, endX, endY)
                }
            }
        }
    }

    override fun initView() {
        viewModel.walkResult.observe(viewLifecycleOwner) { walkResult ->
            if (walkResult != null) { // API 호출 결과가 설정된 경우에만 초기화
                binding.rvLocationResult.layoutManager = LinearLayoutManager(requireContext())
                initLocationResultRVA()
                binding.rvLocationResult.adapter = locationResultRVA
            }
        }
    }

    private fun initLocationResultRVA() {
        _locationResultRVA = LocationResultRVA(
            viewModel = viewModel,
            lifecycleOwner = viewLifecycleOwner,
            viewLifecycleOwner = viewLifecycleOwner,
            type = LocationResultType.Walk
        )
    }

}