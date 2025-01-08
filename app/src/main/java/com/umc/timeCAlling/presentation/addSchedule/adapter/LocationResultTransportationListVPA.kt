package com.umc.timeCAlling.presentation.addSchedule.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.umc.timeCAlling.presentation.addSchedule.list.CarTransportationListFragment
import com.umc.timeCAlling.presentation.addSchedule.list.PublicTransportationListFragment
import com.umc.timeCAlling.presentation.addSchedule.list.WalkTransportationListFragment

class LocationResultTransportationListVPA(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> CarTransportationListFragment()
            1 -> PublicTransportationListFragment()
            2 -> WalkTransportationListFragment()
            else -> throw IllegalStateException("Invalid position")
        }
    }
}