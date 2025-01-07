package com.umc.timeCAlling.presentation.addSchedule.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.umc.timeCAlling.presentation.addSchedule.list.CarTransportationListFragment
import com.umc.timeCAlling.presentation.addSchedule.list.PublicTransportationListFragment
import com.umc.timeCAlling.presentation.addSchedule.list.WalkTransportationListFragment

class LocationResultTransportationListVPA(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 6
    }

    // API 연결하면서 내부 Fragment 바꿀 예정
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> PublicTransportationListFragment()
            1 -> WalkTransportationListFragment()
            2 -> CarTransportationListFragment()
            else -> throw IllegalStateException("Invalid position")
        }
    }
}