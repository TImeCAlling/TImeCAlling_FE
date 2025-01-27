package com.umc.timeCAlling.presentation.login

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentSignupPhotoBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupPhotoFragment :
    BaseFragment<FragmentSignupPhotoBinding>(R.layout.fragment_signup_photo) {

    private var isPhotoSelected = false

    override fun initView() {
        setClickListener()
        updateNextButtonState()
    }

    override fun initObserver() {}

    private fun setClickListener() {
        // 다음 버튼 클릭 시
        binding.tvSignupPhotoNext.setOnClickListener {
            if (isPhotoSelected) {
                navigateToSignupNameFragment()
            }
        }

        // 기본 이미지로 넘어가기
        binding.tvSignupPhotoDefault.setOnClickListener {
            navigateToSignupNameFragment()
        }

        // 갤러리 열기 버튼
        binding.clSignupPhotoCamera.setOnClickListener {
            openGallery()
        }

        // 뒤로가기 버튼
        binding.ivSignupPhotoBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun openGallery() {
        // 갤러리를 여는 Intent 실행
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == androidx.fragment.app.FragmentActivity.RESULT_OK) {
                val selectedImageUri: Uri? = result.data?.data
                if (selectedImageUri != null) {
                    updateProfileImage(selectedImageUri)
                    isPhotoSelected = true
                    updateNextButtonState()
                } else {
                    Toast.makeText(requireContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun updateProfileImage(imageUri: Uri) {
        // CircleImageView에 URI로 이미지 설정
        binding.ivSignupPhotoOval1.setImageURI(imageUri)
        // 기본 얼굴 아이콘 숨기기
        binding.ivSignupPhotoFace.visibility = android.view.View.INVISIBLE
    }

    private fun updateNextButtonState() {
        binding.tvSignupPhotoNext.apply {
            isClickable = isPhotoSelected
            background = ContextCompat.getDrawable(
                requireContext(),
                if (isPhotoSelected) R.drawable.shape_rect_999_mint_fill else R.drawable.shape_rect_999_gray300_fill
            )
            setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (isPhotoSelected) R.color.white else R.color.gray_500
                )
            )
        }
    }

    private fun navigateToSignupNameFragment() {
        findNavController().navigate(R.id.action_signupPhotoFragment_to_signupNameFragment)
    }
}