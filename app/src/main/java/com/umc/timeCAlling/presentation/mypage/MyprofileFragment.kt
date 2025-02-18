package com.umc.timeCAlling.presentation.mypage

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.adapters.ViewBindingAdapter.setClickListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentMyprofileBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.presentation.login.adapter.SignupViewModel
import com.umc.timeCAlling.presentation.mypage.adapter.MyprofileTimeAdapter
import com.umc.timeCAlling.util.network.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class MyprofileFragment : BaseFragment<FragmentMyprofileBinding>(R.layout.fragment_myprofile) {

    private val myprofileViewModel: MyprofileViewModel by viewModels()
    private val signupViewModel: SignupViewModel by viewModels()

    private var isPhotoSelected = false
    private var isInputValid = false
    private var previousCenterPosition: Int? = null

    private var nickname: String = ""
    private var avgPrepTime: Int = 15
    private var freeTime: String = "PLENTY"
    private var imageFile: File? = null
    private var selectedImageUri: Uri? = null

    private val timeOptions = listOf(" ", " ", "15분", "30분", "45분", "60분", "90분+", " ", " ")
    private val spareOptions by lazy {
        listOf(
            binding.tvMyprofileSpareOption1,
            binding.tvMyprofileSpareOption2,
            binding.tvMyprofileSpareOption3
        )
    }

    private lateinit var nameBottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var timeBottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var spareBottomSheetBehavior: BottomSheetBehavior<View>

    override fun initView() {
        myprofileViewModel.getUser()
        observeViewModel()

        initBottomSheets()
        initBottomSheetActions()
        initSpareBottomSheetActions()
        initEditName()

        setClickListener()
        setupRecyclerView()
        setupTouchOutsideToClearFocus()

        hideViews(
            R.id.main_bnv,
            R.id.iv_main_add_schedule_btn,
            R.id.iv_main_bnv_shadow,
            R.id.iv_main_bnv_white_oval
        )
        binding.viewMyprofileBottomSheetBg.visibility = View.GONE
    }

    override fun initObserver() {
        /*
        // 카카오 닉네임 가져오기 예시
        signupViewModel.getKakaoUserInfo()
        signupViewModel.kakaoUserInfo.observe(viewLifecycleOwner) { userInfo ->
            Log.d("MypageFragment", "현재 로그인된 유저 닉네임: ${userInfo?.nickname ?: "정보 없음"}")
        }*/
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                myprofileViewModel.userInfo.collectLatest { state ->
                    when (state) {
                        is UiState.Loading -> {
                            Log.d("MyprofileFragment", "Loading user data")
                        }
                        is UiState.Success -> {
                            val user = state.data
                            Log.d("MyprofileFragment", "UI 업데이트: $user")

                            nickname = user.nickname
                            avgPrepTime = user.avgPrepTime
                            freeTime = user.freeTime
                            imageFile = null // 프로필 이미지는 초기에는 null

                            binding.tvMyprofileCurrentName.text = user.nickname
                            binding.tvMyprofileNameEdit.text = user.nickname
                            binding.tvMyprofileTimeEdit.text = user.avgPrepTime.toString()
                            binding.tvMyprofileSpareEdit.text = when (user.freeTime) {
                                "PLENTY" -> "여유"
                                "RELAXED" -> "넉넉"
                                "TIGHT" -> "딱딱"
                                else -> ""
                            }

                            if (user.profileImage.isNullOrEmpty()) {
                                binding.ivMyprofilePhoto.setImageResource(R.drawable.shape_rect_999_trans_fill)
                                binding.ivMyprofilePhoto.visibility = View.VISIBLE
                            } else {
                                Glide.with(this@MyprofileFragment)
                                    .load(user.profileImage)
                                    .placeholder(R.drawable.shape_rect_999_white_fill)
                                    .error(R.drawable.ic_profile_default)
                                    .into(binding.ivMyprofilePhoto)
                            }
                        }
                        is UiState.Error -> {
                            Toast.makeText(requireContext(), "사용자 정보 로드 실패.", Toast.LENGTH_SHORT).show()
                            Log.e("MyprofileFragment", "사용자 정보 로드 실패")
                        }
                        UiState.Empty -> Log.d("MyprofileFragment", "사용자 정보 없음")
                    }
                }
            }
        }
    }

    private fun setClickListener() {
        binding.clMyprofileCamera.setOnClickListener { openGallery() }
        binding.ivMyprofileBack.setOnClickListener { findNavController().popBackStack() }
        binding.ivMyprofileNameArrow.setOnClickListener { toggleBottomSheetState(nameBottomSheetBehavior) }
        binding.ivMyprofileTimeArrow.setOnClickListener { toggleBottomSheetState(timeBottomSheetBehavior) }
        binding.ivMyprofileSpareArrow.setOnClickListener { toggleBottomSheetState(spareBottomSheetBehavior) }
        binding.ivMyprofileLogoutArrow.setOnClickListener { showLogoutDialog() }
        binding.clMyprofileNameDelete.setOnClickListener { clearInputField() }
        binding.tvMyprofileWithdraw.setOnClickListener { showWithdrawDialog() }

        // 뒤로가기
        binding.ivMyprofileBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun handleBackgroundVisibility(newState: Int) {
        binding.viewMyprofileBottomSheetBg.visibility = if (newState == BottomSheetBehavior.STATE_HIDDEN) View.GONE else View.VISIBLE
    }

    private fun initBottomSheets() {
        nameBottomSheetBehavior = createBottomSheet(binding.clMyprofileBottomSheetName)
        timeBottomSheetBehavior = createBottomSheet(binding.clMyprofileBottomSheetTime)
        spareBottomSheetBehavior = createBottomSheet(binding.clMyprofileBottomSheetSpare)
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

    private fun initBottomSheetActions() {
        binding.tvMyprofileNameSave.setOnClickListener {
            nickname = binding.etMyprofileNameInput.text.toString().trim()
            binding.tvMyprofileCurrentName.text = nickname
            binding.tvMyprofileNameEdit.text = nickname
            nameBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            updateUserProfile()
        }

        binding.tvMyprofileTimeSave.setOnClickListener {
            avgPrepTime = timeOptions.getOrNull(previousCenterPosition ?: -1)
                ?.trim()?.takeWhile { it.isDigit() }?.toIntOrNull() ?: 15
            binding.tvMyprofileTimeEdit.text = avgPrepTime.toString()
            timeBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            updateUserProfile()
        }

        binding.tvMyprofileSpareSave.setOnClickListener {
            val selectedSpareOption = spareOptions.find { isOptionSelected(it) }
            freeTime = when ((selectedSpareOption as? TextView)?.text?.toString()?.trim().orEmpty()) {
                "여유" -> "PLENTY"
                "넉넉" -> "RELAXED"
                "딱딱" -> "TIGHT"
                else -> "PLENTY"
            }
            binding.tvMyprofileSpareEdit.text = (selectedSpareOption as? TextView)?.text?.toString().orEmpty()
            spareBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            updateUserProfile()
        }

        binding.ivMyprofileBottomSheetNameClose.setOnClickListener { nameBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN }
        binding.ivMyprofileBottomSheetTimeClose.setOnClickListener { timeBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN }
        binding.ivMyprofileBottomSheetSpareClose.setOnClickListener { spareBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN }
    }

    private fun isOptionSelected(option: View) = option.background.constantState == ContextCompat.getDrawable(requireContext(), R.drawable.shape_rect_999_mint200_fill_mint_line_1)?.constantState

    private fun initSpareBottomSheetActions() {
        setDefaultSelectedSpareOption()
        spareOptions.forEach { option ->
            option.setOnClickListener { updateSelectedSpareOption(option) }
        }
    }

    private fun setDefaultSelectedSpareOption() {
        val selectedOption = when (binding.tvMyprofileSpareEdit.text.toString()) {
            "여유" -> binding.tvMyprofileSpareOption1
            "넉넉" -> binding.tvMyprofileSpareOption2
            "딱딱" -> binding.tvMyprofileSpareOption3
            else -> binding.tvMyprofileSpareOption2 // 기본값 설정
        }
        updateSelectedSpareOption(selectedOption)
    }

    private fun updateSelectedSpareOption(selectedOption: View) {
        spareOptions.forEach { option ->
            if (option == selectedOption) applySelectedSpareStyle(option) else applyUnselectedSpareStyle(option)
        }
    }

    private fun applySelectedSpareStyle(option: View) {
        option.setBackgroundResource(R.drawable.shape_rect_999_mint200_fill_mint_line_1)
        (option as? TextView)?.setTextAppearance(R.style.TextAppearance_TimeCAlling_Button_Mint)
    }

    private fun applyUnselectedSpareStyle(option: View) {
        option.setBackgroundResource(R.drawable.shape_rect_999_gray200_fill)
        (option as? TextView)?.setTextAppearance(R.style.TextAppearance_TimeCAlling_Button_Gray)
    }

    private fun initEditName() {
        binding.etMyprofileNameInput.filters = arrayOf(InputFilter.LengthFilter(5))
        binding.etMyprofileNameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s?.toString()?.trim().orEmpty()
                binding.tvMyprofileNameCount.text = text.length.toString()
                isInputValid = text.isNotEmpty()
                updateInputFieldState(binding.etMyprofileNameInput.hasFocus(), text)
                if (text.isEmpty()) disableSaveButton() else enableSaveButton()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        binding.etMyprofileNameInput.setOnFocusChangeListener { _, hasFocus ->
            updateInputFieldState(hasFocus, binding.etMyprofileNameInput.text.toString())
        }
    }

    private fun updateInputFieldState(hasFocus: Boolean, text: String) {
        hideErrorState()
        when {
            !hasFocus && text.isEmpty() -> setInputFieldStyle(R.drawable.shape_rect_10_gray250_fill, R.color.gray_600)
            hasFocus && text.isEmpty() -> setInputFieldStyle(R.drawable.shape_rect_10_mint100_fill_mint_line_1, R.color.gray_900)
            hasFocus && text.isNotEmpty() -> {
                setInputFieldStyle(R.drawable.shape_rect_10_mint100_fill_mint_line_1, R.color.gray_900)
                binding.clMyprofileNameDelete.visibility = View.VISIBLE
            }
            !hasFocus && text.isNotEmpty() -> {
                setInputFieldStyle(R.drawable.shape_rect_10_white_fill_mint400_line_1, R.color.gray_900)
                binding.clMyprofileNameCheck.visibility = View.VISIBLE
            }
        }
    }

    private fun setInputFieldStyle(backgroundRes: Int, textColorRes: Int) {
        binding.etMyprofileNameInput.apply {
            background = ContextCompat.getDrawable(requireContext(), backgroundRes)
            setTextColor(ContextCompat.getColor(requireContext(), textColorRes))
        }
        binding.clMyprofileNameDelete.visibility = View.INVISIBLE
        binding.clMyprofileNameCheck.visibility = View.INVISIBLE
    }

    private fun clearInputField() {
        binding.etMyprofileNameInput.text = null
        binding.etMyprofileNameInput.requestFocus()
    }

    private fun showErrorState() {
        binding.etMyprofileNameInput.apply {
            text = null
            hint = "유효한 문자를 입력해주세요."
            background = ContextCompat.getDrawable(requireContext(), R.drawable.shape_rect_10_white_fill_error_line_1)
            setHintTextColor(ContextCompat.getColor(requireContext(), R.color.gray_500))
        }
        toggleErrorVisibility(true)
        disableSaveButton()
    }

    private fun hideErrorState() {
        binding.etMyprofileNameInput.hint = "EX) 김지각"
        toggleErrorVisibility(false)
    }

    private fun toggleErrorVisibility(isError: Boolean) {
        binding.clMyprofileNameError.visibility = if (isError) View.VISIBLE else View.INVISIBLE
        binding.clMyprofileNameCheck.visibility = if (!isError) View.VISIBLE else View.INVISIBLE
        binding.clMyprofileNameDelete.visibility = if (!isError) View.VISIBLE else View.INVISIBLE
    }

    private fun enableSaveButton() {
        setSaveButtonState(true, R.drawable.shape_rect_999_mint_fill, R.color.white)
    }

    private fun disableSaveButton() {
        setSaveButtonState(false, R.drawable.shape_rect_999_gray200_fill, R.color.gray_500)
    }

    private fun setSaveButtonState(isEnabled: Boolean, backgroundRes: Int, textColorRes: Int) {
        binding.tvMyprofileNameSave.apply {
            this.isEnabled = isEnabled
            background = ContextCompat.getDrawable(requireContext(), backgroundRes)
            setTextColor(ContextCompat.getColor(requireContext(), textColorRes))
        }
    }

    private fun setupTouchOutsideToClearFocus() {
        binding.clMyprofileName.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) hideKeyboardIfNeeded()
            false
        }
    }

    private fun hideKeyboardIfNeeded() {
        requireActivity().currentFocus?.let {
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
            it.clearFocus()
        }
    }

    private fun hideViews(vararg viewIds: Int) {
        viewIds.forEach { id ->
            requireActivity().findViewById<View>(id)?.visibility = View.GONE
        }
    }

    private fun toggleBottomSheetState(bottomSheetBehavior: BottomSheetBehavior<View>) {
        bottomSheetBehavior.state = if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_HIDDEN
    }

    // updateUser() 호출
    private fun updateUserProfile() {
        val updatedNickname = if (nickname != binding.tvMyprofileCurrentName.text.toString()) {
            binding.tvMyprofileCurrentName.text.toString()
        } else null

        val updatedAvgPrepTime = if (avgPrepTime != binding.tvMyprofileTimeEdit.text.toString().toInt()) {
            binding.tvMyprofileTimeEdit.text.toString().toInt()
        } else null

        val updatedFreeTime = if (freeTime != binding.tvMyprofileSpareEdit.text.toString()) {
            when (binding.tvMyprofileSpareEdit.text.toString()) {
                "여유" -> "PLENTY"
                "넉넉" -> "RELAXED"
                "딱딱" -> "TIGHT"
                else -> null
            }
        } else null

        myprofileViewModel.updateUser(
            updatedNickname,
            updatedAvgPrepTime,
            updatedFreeTime,
            imageFile
        )
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == androidx.fragment.app.FragmentActivity.RESULT_OK) {
            selectedImageUri = result.data?.data
            if (selectedImageUri != null) {
                binding.ivMyprofilePhoto.setImageURI(selectedImageUri)
                binding.ivMyprofilePhoto.visibility = View.INVISIBLE
                isPhotoSelected = true

                imageFile = uriToFile(selectedImageUri!!, requireContext()) // 전역 변수 업데이트
                updateUserProfile() // 이미지 선택 후 updateUserProfile() 호출
            } else {
                showToast("이미지를 선택하지 않았습니다.")
            }
        }
    }

    private fun uriToFile(uri: Uri, context: Context): File {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri) ?: throw IllegalArgumentException("Cannot open input stream from URI")

        val tempFile = File.createTempFile("profile_image", ".jpg", context.cacheDir)
        tempFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }

        return tempFile
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val adapter = MyprofileTimeAdapter(timeOptions)

        val avgPrepTime = binding.tvMyprofileTimeEdit.text.toString().toIntOrNull() ?: 15
        val initialPosition = when (avgPrepTime) {
            15 -> 2
            30 -> 3
            45 -> 4
            60 -> 5
            90 -> 6
            else -> 2 // 기본값 설정
        }

        binding.rvMyprofileTimeRecyclerView.apply {
            this.layoutManager = layoutManager
            this.adapter = adapter

            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(this)

            post {
                adapter.updateItemColor(initialPosition, true)
                previousCenterPosition = initialPosition
                layoutManager.scrollToPosition(initialPosition)
            }

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val centerView = snapHelper.findSnapView(layoutManager)
                        val centerPosition = centerView?.let { layoutManager.getPosition(it) }
                        if (centerPosition != null && centerPosition != previousCenterPosition) {
                            adapter.updateItemColor(centerPosition, true)
                            previousCenterPosition?.let { adapter.updateItemColor(it, false) }
                            previousCenterPosition = centerPosition
                        }
                    }
                }
            })
        }
    }

    private fun showWithdrawDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_withdraw_account_dialog)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.8).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        val btn = dialog.findViewById<TextView>(R.id.btn_dialog)
        btn.setOnClickListener {
            dialog.dismiss()
            deleteUser()
        }

        dialog.show()
    }

    private fun showLogoutDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_logout_account_dialog)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.8).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        val logoutBtn = dialog.findViewById<TextView>(R.id.btn_dialog_logout)
        logoutBtn.setOnClickListener {
            dialog.dismiss()
            logoutUser()
        }

        val cancelBtn = dialog.findViewById<TextView>(R.id.btn_dialog_cancel)
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun deleteUser(){
        myprofileViewModel.deleteUser()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                myprofileViewModel.deleteState.collectLatest { state ->
                    when (state) {
                        is UiState.Success -> {
                            Log.d("MyprofileFragment", "회원탈퇴 성공, 저장된 토큰 삭제 후 로그인 화면으로 이동")

                            signupViewModel.clearAuthToken()

                            findNavController().navigate(R.id.action_myprofileFragment_to_loginFragment) // 로그인 화면으로 이동
                        }
                        is UiState.Error -> {
                            Toast.makeText(requireContext(), "회원탈퇴 실패: ${state.error?.message}", Toast.LENGTH_SHORT).show()
                            Log.e("MyprofileFragment", "회원탈퇴 실패")
                        }
                        else -> {} // 로딩 상태 처리 가능
                    }
                }
            }
        }
    }

    private fun logoutUser(){
        myprofileViewModel.logoutUser()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                myprofileViewModel.logoutState.collectLatest { state ->
                    when (state) {
                        is UiState.Success -> {
                            Log.d("MyprofileFragment", "로그아웃 성공, 로그인 화면으로 이동")

                            signupViewModel.clearAuthToken()
                            findNavController().navigate(R.id.action_myprofileFragment_to_loginFragment) // 로그인 화면으로 이동
                        }
                        is UiState.Error -> {
                            Toast.makeText(requireContext(), "로그아웃 실패: ${state.error?.message}", Toast.LENGTH_SHORT).show()
                            Log.e("MyprofileFragment", "로그아웃 실패")
                        }
                        else -> {} // 로딩 상태 처리 가능
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

