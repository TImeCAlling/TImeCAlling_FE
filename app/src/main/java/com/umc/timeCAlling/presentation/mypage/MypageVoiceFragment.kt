package com.umc.timeCAlling.presentation.mypage

import android.view.View
import android.widget.SeekBar
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentMypageVoiceBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MypageVoiceFragment: BaseFragment<FragmentMypageVoiceBinding>(R.layout.fragment_mypage_voice) {

    private lateinit var genderBottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var languageBottomSheetBehavior: BottomSheetBehavior<View>

    private var selectedGender: String = "여성"
    private var selectedLanguage: String = "한국어"

    override fun initObserver() {

    }

    override fun initView() {
        setClickListener()
        initBottomSheets()
        initBottomSheetActions()
        initVolumeSeekBar()

        hideViews(
            R.id.main_bnv,
            R.id.iv_main_add_schedule_btn,
            R.id.iv_main_bnv_shadow,
            R.id.iv_main_bnv_white_oval
        )
    }

    private fun setClickListener() {
        binding.ivMyprofileBack.setOnClickListener { findNavController().popBackStack() }
        binding.ivMyprofileGenderArrow.setOnClickListener { toggleBottomSheetState(genderBottomSheetBehavior) }
        binding.ivMyprofileLanguageArrow.setOnClickListener { toggleBottomSheetState(languageBottomSheetBehavior) }
    }

    private fun hideViews(vararg viewIds: Int) {
        viewIds.forEach { id ->
            requireActivity().findViewById<View>(id)?.visibility = View.GONE
        }
    }

    private fun initBottomSheets() {
        genderBottomSheetBehavior = createBottomSheet(binding.clMypageVoiceBottomSheetGender)
        languageBottomSheetBehavior = createBottomSheet(binding.clMypageVoiceBottomSheetLanguage)
    }

    private fun createBottomSheet(sheet: View): BottomSheetBehavior<View> {
        return BottomSheetBehavior.from(sheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            peekHeight = 0
            isHideable = true
            skipCollapsed = true
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    handleBackgroundVisibility(newState)
                }
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }
    }

    private fun handleBackgroundVisibility(newState: Int) {
        binding.viewMyprofileBottomSheetBg.visibility = if (newState == BottomSheetBehavior.STATE_HIDDEN) View.GONE else View.VISIBLE
    }

    private fun toggleBottomSheetState(bottomSheetBehavior: BottomSheetBehavior<View>) {
        bottomSheetBehavior.state = if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_HIDDEN
    }

    private fun initBottomSheetActions() {
        binding.clMypageVoiceGenderBtnWomen.setOnClickListener {
            selectGender(true)
        }
        binding.clMypageVoiceGenderBtnMen.setOnClickListener {
            selectGender(false)
        }

        binding.clMypageVoiceLanguageBtnKorean.setOnClickListener {
            selectLanguage(true)
        }
        binding.clMypageVoiceLanguageBtnEnglish.setOnClickListener {
            selectLanguage(false)
        }

        binding.tvMypageVoiceLanguageSave.setOnClickListener {
            binding.tvMyprofileLanguageSet.text = selectedLanguage
            languageBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.tvMypageVoiceGenderSave.setOnClickListener {
            binding.tvMyprofileGenderSet.text = selectedGender
            genderBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }


        binding.ivMypageVoiceBottomSheetLanguageClose.setOnClickListener { languageBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN }
        binding.ivMypageVoiceBottomSheetGenderClose.setOnClickListener { genderBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN }
    }

    private fun selectGender(isFemale: Boolean) {
        val selectedOval = R.drawable.shape_rect_999_mint300_fill_mint_line_1
        val unselectedOval = R.drawable.shape_rect_999_gray300_fill
        val selectedCheck = R.drawable.ic_check_mint
        val unselectedCheck = R.drawable.ic_check_white

        // 선택된 값 업데이트
        selectedGender = if (isFemale) "여성" else "남성"

        // 여성 선택 시 UI 변경
        binding.ivMypageVoiceGenderOvalWomen.setBackgroundResource(if (isFemale) selectedOval else unselectedOval)
        binding.ivMypageVoiceGenderCheckWomen.setBackgroundResource(if (isFemale) selectedCheck else unselectedCheck)

        // 남성 선택 시 UI 변경
        binding.ivMypageVoiceGenderOvalMen.setBackgroundResource(if (isFemale) unselectedOval else selectedOval)
        binding.ivMypageVoiceGenderCheckMen.setBackgroundResource(if (isFemale) unselectedCheck else selectedCheck)
    }

    private fun selectLanguage(isKorean: Boolean) {
        val selectedOval = R.drawable.shape_rect_999_mint300_fill_mint_line_1
        val unselectedOval = R.drawable.shape_rect_999_gray300_fill
        val selectedCheck = R.drawable.ic_check_mint
        val unselectedCheck = R.drawable.ic_check_white

        // 선택된 값 업데이트
        selectedLanguage = if (isKorean) "한국어" else "영어"

        // 한국어 선택 시 UI 변경
        binding.ivMypageVoiceLanguageOvalKorean.setBackgroundResource(if (isKorean) selectedOval else unselectedOval)
        binding.ivMypageVoiceLanguageCheckKorean.setBackgroundResource(if (isKorean) selectedCheck else unselectedCheck)

        // 영어 선택 시 UI 변경
        binding.ivMypageVoiceLanguageOvalEnglish.setBackgroundResource(if (isKorean) unselectedOval else selectedOval)
        binding.ivMypageVoiceLanguageCheckEnglish.setBackgroundResource(if (isKorean) unselectedCheck else selectedCheck)
    }

    private fun initVolumeSeekBar() {
        binding.seekbarMyprofileVolume.max = 5 // 0~5 단계
        binding.seekbarMyprofileVolume.progress = 0 // 초기값 설정

        binding.seekbarMyprofileSpeed.max = 5 // 0~5 단계
        binding.seekbarMyprofileSpeed.progress = 0 // 초기값 설정

        binding.seekbarMyprofileVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val closestStep = progress
                    seekBar?.progress = closestStep // 가장 가까운 단계로 이동
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let {
                    val closestStep = it.progress // 멈췄을 때 단계 유지
                    it.progress = closestStep
                }
            }
        })

        binding.seekbarMyprofileSpeed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val closestStep = progress
                    seekBar?.progress = closestStep // 가장 가까운 단계로 이동
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let {
                    val closestStep = it.progress // 멈췄을 때 단계 유지
                    it.progress = closestStep
                }
            }
        })
    }



}