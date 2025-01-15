package com.umc.timeCAlling.presentation.checkList

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentCheckListBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckListFragment: BaseFragment<FragmentCheckListBinding>(R.layout.fragment_check_list) {
    private lateinit var args : CheckListFragmentArgs

    private var questionIndex = 0
    private var selectedIndex: Int? = null
    private var userAnswers: String = ""
    private val questions = listOf(
        QuestionItem(
            question = "오늘 일정에 맞춰 제시간에 도착했나요?",
            answers = listOf("네, 제시간에 도착했습니다.", "아니요, 지각했습니다.")
        ),
        QuestionItem(
            question = "일정 시작 시간보다 얼마나 여유 있게 도착했나요?",
            answers = listOf("10분 이상 여유롭게 도착", "5-10분 여유롭게 도착", "거의 정시에 도착")
        ),
        QuestionItem(
            question = "얼마나 지각했나요?",
            answers = listOf("5분 미만 지각", "5-10분 지각", "10분 이상 지각")
        ),
        QuestionItem(
            question = "지각하거나 여유롭게 도착한 이유는 무엇인가요?",
            answers = listOf("교통 체증/대중교통 지연", "늦게 출발", "길을 잘못 찾음" ,"일찍 출발", "교통 상황 원활", "목적지와 가까움")
        ),
        QuestionItem(
            question = "오늘 일정에 영향을 준 외부 요인이 있었나요?",
            answers = listOf("날씨", "교통 상황", "예상치 못한 개인 사정", "기타")
        ),
        QuestionItem(
            question = "출발 시간이 적절했다고 생각하시나요?",
            answers = listOf("네", "아니요")
        ),
    )

    override fun initView() {
        args = CheckListFragmentArgs.fromBundle(requireArguments())
        bottomNavigationRemove()
        initCheckList()

        binding.apply{
            ivChecklistBack.setOnClickListener {
                findNavController().navigate(R.id.action_global_homeFragment)
                bottomNavigationShow()
            }
        }
    }

    override fun initObserver() {

    }

    private fun bottomNavigationRemove() {
        // BottomNavigationView 숨기기
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
        bottomNavigationView?.visibility = View.GONE

        // + 버튼 숨기기
        val addScheduleButton = requireActivity().findViewById<View>(R.id.iv_main_add_schedule_btn)
        addScheduleButton?.visibility = View.GONE

        val shadowImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_shadow)
        shadowImageView?.visibility = View.GONE

        val ovalImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_white_oval)
        ovalImageView?.visibility = View.GONE
    }

    private fun bottomNavigationShow() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
        bottomNavigationView?.visibility = View.VISIBLE

        val addScheduleButton = requireActivity().findViewById<View>(R.id.iv_main_add_schedule_btn)
        addScheduleButton?.visibility = View.VISIBLE

        val shadowImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_shadow)
        shadowImageView?.visibility = View.VISIBLE

        val ovalImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_white_oval)
        ovalImageView?.visibility = View.VISIBLE
    }

    private fun initCheckList() {
        showQuestion(questionIndex)

        binding.btnChecklistNext.setOnClickListener {
            if(selectedIndex == null) {
                Toast.makeText(requireContext(), "답변을 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                if(questionIndex == 0 && selectedIndex == 1) {
                    questionIndex++
                }
                if(questionIndex == 1) {questionIndex++}
                binding.progressindicatorCheckList.progress += 20
                userAnswers += questions[questionIndex].answers[selectedIndex!!] + ", "
                if(questionIndex < questions.size - 1) {
                    questionIndex++
                    showQuestion(questionIndex)
                } else {
                    Log.d("userAnswers", userAnswers)
                    val idx = args.scheduleIndex
                    val action = CheckListFragmentDirections.actionCheckListFragmentToCheckListResultFragment(idx)
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun showQuestion(index : Int) {
        selectedIndex = null
        val questionItem = questions[index]
        binding.tvChecklistQuestion.text = questionItem.question
        binding.layoutChecklistAnswers.removeAllViews()

        questionItem.answers.forEachIndexed {index, text ->
            val itemView = layoutInflater.inflate(R.layout.item_checklist_answer, binding.layoutChecklistAnswers, false)

            val tvAnswer = itemView.findViewById<TextView>(R.id.tv_answer)
            val ivAnswer = itemView.findViewById<ImageView>(R.id.iv_answer)

            tvAnswer.text = text
            itemView.setOnClickListener {
                selectedIndex = index
                updateSelectionUI()
            }
            binding.layoutChecklistAnswers.addView(itemView)
        }
    }

    private fun updateSelectionUI() {
        for(i in 0 until binding.layoutChecklistAnswers.childCount) {
            val child = binding.layoutChecklistAnswers.getChildAt(i)
            val tvAnswer = child.findViewById<TextView>(R.id.tv_answer)
            val ivAnswer = child.findViewById<ImageView>(R.id.iv_answer)
            if(i == selectedIndex) {
                child.setBackgroundResource(R.drawable.shape_rect_26_mint300_fill)
                ivAnswer.setImageResource(R.drawable.ic_icon_mint_fill)
                tvAnswer.setTextColor(resources.getColor(R.color.mint_600))
            } else {
                child.setBackgroundResource(R.drawable.shape_rect_26_gray200_fill)
                ivAnswer.setImageResource(R.drawable.ic_icon_gray500_fill)
                tvAnswer.setTextColor(resources.getColor(R.color.gray_500))
            }
        }
    }
}