package com.umc.timeCAlling.presentation.mypage.alarmlist

import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentAddAlarmBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAlarmFragment : BaseFragment<FragmentAddAlarmBinding>(R.layout.fragment_add_alarm) {
    private lateinit var navController: NavController
    private lateinit var timeBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var messageBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var musicBehavior: BottomSheetBehavior<ConstraintLayout>
    private var min1: Int = 1
    private var min2: Int = 5
    private var message: String = ""

    override fun initView() {
        bottomNavigationRemove()
        initSetOnClickListener()
        initMusicSwitch()
        initBottomSheet()
    }

    override fun initObserver() {
    }

    private fun initSetOnClickListener() {
        binding.apply {
            ivAddAlarmBack.setOnClickListener { findNavController().navigate(R.id.action_addAlarmFragment_to_alarmlistFragment) }
            layoutAddAlarmSelectTime.setOnClickListener { timeBehavior.state = BottomSheetBehavior.STATE_EXPANDED }
            tvAddAlarmMessage.setOnClickListener { messageBehavior.state = BottomSheetBehavior.STATE_EXPANDED }
            layoutAddAlarmMusicContainer.setOnClickListener { musicBehavior.state = BottomSheetBehavior.STATE_EXPANDED }
            btnAddAlarmSave.setOnClickListener { findNavController().navigate(R.id.action_addAlarmFragment_to_alarmlistFragment) }
        }
    }

    private fun initBottomSheet() {
        initAlarmTimeBottomSheet()
        initAlarmMessageBottomSheet()
        initAlarmMusicBottomSheet()
    }

    private fun initAlarmTimeBottomSheet() {
        timeBehavior = BottomSheetBehavior.from(binding.clAddAlarmBottomSheetTime)
        timeBehavior.apply {
            this.isHideable = true
            this.state = BottomSheetBehavior.STATE_HIDDEN
        }
        timeBehavior.addBottomSheetCallback(object: BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN, BottomSheetBehavior.STATE_COLLAPSED -> {
                        removeBottomSheetBackground()
                    }

                    BottomSheetBehavior.STATE_EXPANDED -> {
                        showBottomSheetBackground()
                    }
                    else -> showBottomSheetBackground()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("slideOffset", slideOffset.toString())
            }
        })

        binding.apply {
            clAddAlarmBottomSheetTimeClose.setOnClickListener { timeBehavior.state = BottomSheetBehavior.STATE_HIDDEN }
            ivAddAlarmMin1Up.setOnClickListener {
                if(min1 < 9) min1++
                else min1 = 0
                tvAddAlarmMin1.text = min1.toString()
            }
            ivAddAlarmMin1Down.setOnClickListener {
                if(min1 > 0) min1--
                else min1 = 9
                tvAddAlarmMin1.text = min1.toString()
            }
            ivAddAlarmMin2Up.setOnClickListener {
                if(min2 < 9) min2++
                else min2 = 0
                tvAddAlarmMin2.text = min2.toString()
            }
            ivAddAlarmMin2Down.setOnClickListener {
                if(min2 > 0) min2--
                else min2 = 9
                tvAddAlarmMin2.text = min2.toString()
            }

            btnAddAlarmTimeSave.setOnClickListener {
                binding.tvAddAlarmTimeSetted.text = "${min1*10 + min2}분 전"
                binding.tvAddAlarmTimeSelect.apply {
                    this.text = "알람시간"
                    this.setTextColor(getResources().getColor(R.color.gray_900))
                }

                timeBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    private fun initAlarmMessageBottomSheet() {
        messageBehavior = BottomSheetBehavior.from(binding.clAddAlarmBottomSheetMent)
        messageBehavior.apply {
            this.isHideable = true
            this.state = BottomSheetBehavior.STATE_HIDDEN
        }
        messageBehavior.addBottomSheetCallback(object: BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN, BottomSheetBehavior.STATE_COLLAPSED -> {
                        removeBottomSheetBackground()
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        showBottomSheetBackground()
                    }
                    else -> showBottomSheetBackground()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("slideOffset", slideOffset.toString())
            }
        })

        binding.apply {
            clAddAlarmBottomSheetMentClose.setOnClickListener { messageBehavior.state = BottomSheetBehavior.STATE_HIDDEN }
            etAddAlarmMent.addTextChangedListener {
                message = it.toString()
                tvAddAlarmMentLength.text = message.length.toString()
            }
            btnAddAlarmMentSave.setOnClickListener {
                binding.tvAddAlarmMessage.text = message
                binding.tvAddAlarmMessageLength.text = message.length.toString()
                messageBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    private fun initAlarmMusicBottomSheet() {
        musicBehavior = BottomSheetBehavior.from(binding.clAddAlarmBottomSheetMusic)
        musicBehavior.apply {
            this.isHideable = true
            this.state = BottomSheetBehavior.STATE_HIDDEN
        }
        musicBehavior.addBottomSheetCallback(object: BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN, BottomSheetBehavior.STATE_COLLAPSED -> {
                        removeBottomSheetBackground()
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        showBottomSheetBackground()
                    }
                    else -> showBottomSheetBackground()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("slideOffset", slideOffset.toString())
            }
        })
        binding.apply {
            clAddAlarmBottomSheetMusicClose.setOnClickListener { musicBehavior.state = BottomSheetBehavior.STATE_HIDDEN }
            btnAddAlarmMusicSave.setOnClickListener {
                musicBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    private fun initMusicSwitch() {
        binding.switchAddAlarmMusic.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                binding.switchAddAlarmMusic.trackTintList = getResources().getColorStateList(R.color.mint_main)
            } else {
                binding.switchAddAlarmMusic.trackTintList = getResources().getColorStateList(R.color.gray_400)
            }
        }
    }

    private fun showBottomSheetBackground() {
        binding.viewAddAlarmBottomSheetBackground.visibility = View.VISIBLE
    }

    private fun removeBottomSheetBackground() {
        binding.viewAddAlarmBottomSheetBackground.visibility = View.GONE
    }

    private fun bottomNavigationRemove() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
        bottomNavigationView?.visibility = View.GONE

        val addScheduleButton = requireActivity().findViewById<View>(R.id.iv_main_add_schedule_btn)
        addScheduleButton?.visibility = View.GONE

        val shadowImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_shadow)
        shadowImageView?.visibility = View.GONE

        val ovalImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_white_oval)
        ovalImageView?.visibility = View.GONE
    }
}