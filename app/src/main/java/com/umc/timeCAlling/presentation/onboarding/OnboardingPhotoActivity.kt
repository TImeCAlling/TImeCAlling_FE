package com.umc.timeCAlling.presentation.onboarding

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ActivityOnboardingPhotoBinding
import com.umc.timeCAlling.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingPhotoActivity :
    BaseActivity<ActivityOnboardingPhotoBinding>(R.layout.activity_onboarding_photo) {

    override fun initView() {
        setClickListener()
    }

    override fun initObserver() {}

    private fun setClickListener() {
        binding.tvOnboardingPhotoNext.setOnClickListener {
            navigateToOnboardingNameActivity()
        }

        binding.clOnboardingPhotoCamera.setOnClickListener {
            openGallery()
        }
    }

    private fun navigateToOnboardingNameActivity() {
        val intent = Intent(this, OnboardingNameActivity::class.java)
        startActivity(intent)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImageUri: Uri? = result.data?.data
                if (selectedImageUri != null) {
                    updateProfileImage(selectedImageUri)
                } else {
                    Toast.makeText(this, "이미지를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun updateProfileImage(imageUri: Uri) {
        // CircleImageView에 URI로 이미지 설정
        binding.ivOnboardingPhotoOval1.setImageURI(imageUri)
        // 기존 얼굴 아이콘을 보이지 않게 설정
        binding.ivOnboardingPhotoFace.visibility = android.view.View.INVISIBLE
    }
}