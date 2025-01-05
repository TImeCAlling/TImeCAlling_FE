package com.umc.timeCAlling.presentation.addSchedule

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.skt.tmap.TMapView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentHomeBinding
import com.umc.timeCAlling.databinding.FragmentLocationSearchBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.util.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationSearchFragment: BaseFragment<FragmentLocationSearchBinding>(R.layout.fragment_location_search) {

    private lateinit var tMapView: TMapView

    override fun initView() {

        bottomNavigationRemove()
        initTMapView() // TMapView 초기화

    }

    override fun initObserver() {

    }

    private fun bottomNavigationRemove() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
        bottomNavigationView?.visibility = View.GONE
    }

    private fun initTMapView() {
        tMapView = TMapView(requireContext())
        tMapView.setSKTMapApiKey(getString(R.string.tmap_app_key)) // T map API 키로 변경
        binding.tmapView.addView(tMapView)
    }

}