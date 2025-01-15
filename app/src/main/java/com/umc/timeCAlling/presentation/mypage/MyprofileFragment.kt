package com.umc.timeCAlling.presentation.mypage

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentMyprofileBinding
import com.umc.timeCAlling.presentation.base.BaseFragment

class MyprofileFragment : BaseFragment<FragmentMyprofileBinding>(R.layout.fragment_myprofile) {

    private var isPhotoSelected = false

    override fun initView() {
        setClickListener()
        bottomNavigationRemove()
    }

    override fun initObserver() {

    }

    private fun setClickListener() {
        // 갤러리 열기 버튼
        binding.clMyprofileCamera.setOnClickListener {
            openGallery()
        }

        // 뒤로가기 버튼
        binding.ivMyprofileBack.setOnClickListener {
            findNavController().popBackStack()
        }
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
                } else {
                    Toast.makeText(requireContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun updateProfileImage(imageUri: Uri) {
        // CircleImageView에 URI로 이미지 설정
        binding.ivMyprofileOval1.setImageURI(imageUri)
        // 기본 얼굴 아이콘 숨기기
        binding.ivMyprofileFace.visibility = android.view.View.INVISIBLE
    }

}