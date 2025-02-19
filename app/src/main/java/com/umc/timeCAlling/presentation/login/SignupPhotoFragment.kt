package com.umc.timeCAlling.presentation.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentSignupPhotoBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.presentation.login.adapter.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@AndroidEntryPoint
class SignupPhotoFragment : BaseFragment<FragmentSignupPhotoBinding>(R.layout.fragment_signup_photo) {

    private val viewModel: SignupViewModel by activityViewModels()
    private var profileImageUri: Uri? = null
    private var isPhotoSelected = false

    override fun initView() {
        setClickListener()
        updateNextButtonState()
    }

    override fun initObserver() {
        viewModel.profileImage.observe(viewLifecycleOwner) { uri ->
            Timber.d("Observed Profile Image URI: $uri")
            uri?.let {
                binding.ivSignupPhoto.setImageURI(it)
                isPhotoSelected = true
                updateNextButtonState()
            }
        }
    }

    private fun setClickListener() {
        // 다음 버튼 클릭 시
        binding.tvSignupPhotoNext.setOnClickListener {
            if (isPhotoSelected) {
                navigateToSignupNameFragment()
            }
        }

        binding.tvSignupPhotoDefault.setOnClickListener {
            val defaultImageFile = getFileFromResource(requireContext(), R.drawable.ic_profile_default_default, "default_profile.png")
            val defaultImageUri = Uri.fromFile(defaultImageFile) // 변환된 파일을 URI로 가져옴

            defaultImageUri?.let {
                viewModel.setProfileImage(it) // ViewModel에 URI 저장
                binding.ivSignupPhoto.setImageURI(it) // 선택한 이미지 미리보기
                isPhotoSelected = true
                updateNextButtonState()
            }
            navigateToSignupNameFragment()
        }

        binding.clSignupPhotoCamera.setOnClickListener {
            openPhotoPicker()
        }

        binding.ivSignupPhotoBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    fun getFileFromResource(context: Context, resourceId: Int, fileName: String): File {
        val file = File(context.cacheDir, fileName)

        val inputStream: InputStream = context.resources.openRawResource(resourceId)
        val outputStream = FileOutputStream(file)

        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return file
    }

    private fun openPhotoPicker() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*" // 모든 이미지 타입 선택
        }
        photoPickerLauncher.launch(intent) // 갤러리 열기
    }

    // PhotoPicker Launcher
    private val photoPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            result.data?.data?.let { uri ->
                profileImageUri = uri
                binding.ivSignupPhoto.setImageURI(uri) // 선택한 이미지 미리보기
                viewModel.setProfileImage(uri) // ViewModel에 URI 저장
            }
        }
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
