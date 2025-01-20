package com.umc.timeCAlling.presentation.mypage

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.provider.MediaStore
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentMyprofileBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.presentation.mypage.adapter.MyprofileTimeAdapter

class MyprofileFragment : BaseFragment<FragmentMyprofileBinding>(R.layout.fragment_myprofile) {

    private var isPhotoSelected = false
    private var isInputValid = false

    private val timeOptions = listOf("  ", "  ", "15분", "30분", "45분", "60분", "90분+", "  ", "  ")
    private var previousCenterPosition: Int? = null

    private lateinit var nameBottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var timeBottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var spareBottomSheetBehavior: BottomSheetBehavior<View>

    override fun initView() {
        setClickListener()
        initBottomSheets()
        initNameBottomSheetActions()
        initEditName()
        setupRecyclerView()
        observeKeyboardAndClearFocus()
        setupTouchOutsideToClearFocus()
        hideViews(
            R.id.main_bnv,
            R.id.iv_main_add_schedule_btn,
            R.id.iv_main_bnv_shadow,
            R.id.iv_main_bnv_white_oval
        )
    }

    override fun initObserver() {}

    /** 클릭 리스너 설정 */
    private fun setClickListener() {
        binding.clMyprofileCamera.setOnClickListener { openGallery() }
        binding.ivMyprofileBack.setOnClickListener { findNavController().popBackStack() }

        binding.ivMyprofileNameArrow.setOnClickListener { toggleBottomSheetState(nameBottomSheetBehavior) }
        binding.ivMyprofileTimeArrow.setOnClickListener { toggleBottomSheetState(timeBottomSheetBehavior) }
        binding.ivMyprofileSpareArrow.setOnClickListener { toggleBottomSheetState(spareBottomSheetBehavior) }

        binding.clMyprofileNameDelete.setOnClickListener { clearInputField() }
    }

    // Bottom Sheet 초기화 함수 수정
    private fun initBottomSheets() {
        // 기존 name Bottom Sheet 초기화
        val nameBottomSheet = binding.clMyprofileBottomSheetName
        nameBottomSheetBehavior = BottomSheetBehavior.from(nameBottomSheet)
        nameBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        nameBottomSheetBehavior.peekHeight = 0
        nameBottomSheetBehavior.isHideable = true
        nameBottomSheetBehavior.skipCollapsed = true

        // 새로운 time Bottom Sheet 초기화
        val timeBottomSheet = binding.clMyprofileBottomSheetTime
        timeBottomSheetBehavior = BottomSheetBehavior.from(timeBottomSheet)
        timeBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        timeBottomSheetBehavior.peekHeight = 0
        timeBottomSheetBehavior.isHideable = true
        timeBottomSheetBehavior.skipCollapsed = true

        // 새로운 spare Bottom Sheet 초기화
        val spareBottomSheet = binding.clMyprofileBottomSheetSpare
        spareBottomSheetBehavior = BottomSheetBehavior.from(spareBottomSheet)
        spareBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        spareBottomSheetBehavior.peekHeight = 0
        spareBottomSheetBehavior.isHideable = true
        spareBottomSheetBehavior.skipCollapsed = true
    }


    /** 이름 Bottom Sheet 동작 설정 */
    private fun initNameBottomSheetActions() {
        binding.tvMyprofileNameSave.setOnClickListener {
            val newName = binding.etMyprofileNameInput.text.toString().trim()
            if (newName.isNotEmpty()) {
                binding.tvMyprofileCurrentName.text = newName
                binding.tvMyprofileNameEdit.text = newName
                nameBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                Toast.makeText(requireContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.ivMyprofileBottomSheetNameClose.setOnClickListener {
            nameBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.ivMyprofileBottomSheetTimeClose.setOnClickListener {
            timeBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.ivMyprofileBottomSheetSpareClose.setOnClickListener {
            spareBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    /** 입력 필드 초기화 */
    private fun initEditName() {
        binding.etMyprofileNameInput.filters = arrayOf(InputFilter.LengthFilter(20))

        binding.etMyprofileNameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s?.toString()?.trim() ?: ""
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

    /** 입력 필드 상태 업데이트 */
    private fun updateInputFieldState(hasFocus: Boolean, text: String) {
        hideErrorState() // 에러 상태 숨기기
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

    /** 입력 필드 스타일 설정 */
    private fun setInputFieldStyle(backgroundRes: Int, textColorRes: Int) {
        binding.etMyprofileNameInput.apply {
            background = ContextCompat.getDrawable(requireContext(), backgroundRes)
            setTextColor(ContextCompat.getColor(requireContext(), textColorRes))
        }
        binding.clMyprofileNameDelete.visibility = View.INVISIBLE
        binding.clMyprofileNameCheck.visibility = View.INVISIBLE
    }

    // 입력 필드 초기화
    private fun clearInputField() {
        binding.etMyprofileNameInput.text = null
        binding.etMyprofileNameInput.requestFocus()
    }

    // 에러 상태 표시
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

    // 에러 상태 숨기기
    private fun hideErrorState() {
        binding.etMyprofileNameInput.hint = "EX) 김지각"
        toggleErrorVisibility(false)
    }

    // 에러, 체크, 삭제 아이콘의 표시 상태를 토글
    private fun toggleErrorVisibility(isError: Boolean) {
        binding.clMyprofileNameError.visibility = if (isError) View.VISIBLE else View.INVISIBLE
        binding.clMyprofileNameCheck.visibility = if (!isError) View.VISIBLE else View.INVISIBLE
        binding.clMyprofileNameDelete.visibility = if (!isError) View.VISIBLE else View.INVISIBLE
    }

    /** 저장 버튼 활성화 */
    private fun enableSaveButton() {
        setSaveButtonState(
            isEnabled = true,
            backgroundRes = R.drawable.shape_rect_999_mint_fill,
            textColorRes = R.color.white
        )
    }

    /** 저장 버튼 비활성화 */
    private fun disableSaveButton() {
        setSaveButtonState(
            isEnabled = false,
            backgroundRes = R.drawable.shape_rect_999_gray200_fill,
            textColorRes = R.color.gray_500
        )
    }

    /** 저장 버튼 상태 설정 */
    private fun setSaveButtonState(isEnabled: Boolean, backgroundRes: Int, textColorRes: Int) {
        binding.tvMyprofileNameSave.apply {
            this.isEnabled = isEnabled
            background = ContextCompat.getDrawable(requireContext(), backgroundRes)
            setTextColor(ContextCompat.getColor(requireContext(), textColorRes))
        }
    }

    /** 키보드 상태 관찰 */
    private fun observeKeyboardAndClearFocus() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            binding.root.getWindowVisibleDisplayFrame(rect)
            val screenHeight = binding.root.rootView.height
            val keyboardHeight = screenHeight - rect.bottom
            if (keyboardHeight == 0) {
                binding.etMyprofileNameInput.clearFocus()
            }
        }
    }

    /** 외부 터치 시 포커스 해제 */
    private fun setupTouchOutsideToClearFocus() {
        binding.clMyprofileName.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboardIfNeeded()
            }
            false
        }
    }

    /** 키보드 숨기기 및 포커스 해제 */
    private fun hideKeyboardIfNeeded() {
        requireActivity().currentFocus?.let {
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
            it.clearFocus()
        }
    }

    /** 네비게이션 바와 기타 뷰 숨기기 */
    private fun hideViews(vararg viewIds: Int) {
        viewIds.forEach { id ->
            requireActivity().findViewById<View>(id)?.visibility = View.GONE
        }
    }

    /** Bottom Sheet 상태 토글 */
    private fun toggleBottomSheetState(bottomSheetBehavior: BottomSheetBehavior<View>) {
        bottomSheetBehavior.state = if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            BottomSheetBehavior.STATE_EXPANDED
        } else {
            BottomSheetBehavior.STATE_HIDDEN
        }
    }

    /** 갤러리 열기 */
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    /** 갤러리 실행 결과 처리 */
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == androidx.fragment.app.FragmentActivity.RESULT_OK) {
            val selectedImageUri: Uri? = result.data?.data
            if (selectedImageUri != null) {
                binding.ivMyprofileOval1.setImageURI(selectedImageUri)
                binding.ivMyprofileFace.visibility = View.INVISIBLE
                isPhotoSelected = true
            } else {
                Toast.makeText(requireContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /** 리사이클러뷰 */
    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val adapter = MyprofileTimeAdapter(timeOptions)

        binding.rvMyprofileTimeRecyclerView.apply {
            this.layoutManager = layoutManager
            this.adapter = adapter

            // LinearSnapHelper 설정
            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(this)

            // 초기화 시 첫 번째 아이템 선택 처리
            post {
                val initialPosition = 2
                adapter.updateItemColor(initialPosition, true)
                previousCenterPosition = initialPosition
            }

            // 중앙 아이템 감지 및 색상 변경
            this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val centerView = snapHelper.findSnapView(layoutManager)
                        val centerPosition = centerView?.let { layoutManager.getPosition(it) }
                        if (centerPosition != null && centerPosition != previousCenterPosition) {
                            // 중앙 아이템 색상 변경
                            adapter.updateItemColor(centerPosition, true)
                            previousCenterPosition?.let { adapter.updateItemColor(it, false) }
                            previousCenterPosition = centerPosition
                        }
                    }
                }
            })
        }
    }
}