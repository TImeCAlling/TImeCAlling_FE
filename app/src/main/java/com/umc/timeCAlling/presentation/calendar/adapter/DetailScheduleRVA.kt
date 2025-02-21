package com.umc.timeCAlling.presentation.calendar.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ItemTodayScheduleDetailBinding
import com.umc.timeCAlling.domain.model.response.schedule.PastScheduleResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.ScheduleByDateResponseModel
import com.umc.timeCAlling.presentation.addSchedule.CategoryManager
import com.umc.timeCAlling.presentation.calendar.ScheduleViewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

class DetailScheduleRVA(
    private val context: Context,
    private val viewModel: ScheduleViewModel,
    private val viewLifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<DetailScheduleRVA.DetailScheduleViewHolder>() {
    lateinit var onItemClick: ((ScheduleByDateResponseModel) -> Unit)
    private var detailSchedules: List<ScheduleByDateResponseModel> = emptyList()
    private var checklistDone: List<PastScheduleResponseModel> = emptyList()

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private var selectedDate: String = LocalDate.now().format(formatter)

    fun setScheduleList(scheduleList: List<ScheduleByDateResponseModel>) {
        this.detailSchedules = ArrayList(scheduleList)
        Log.d("DetailScheduleRVA", "setScheduleList 호출됨")
        notifyDataSetChanged()
    }

    fun setDoneList(doneList: List<PastScheduleResponseModel>) {
        this.checklistDone = ArrayList(doneList)
        Log.d("DetailScheduleRVA", checklistDone.toString())
        Log.d("DetailScheduleRVA", "setDoneList 호출됨")
        notifyDataSetChanged()
    }

    fun setSelectedDate(date: String) {
        this.selectedDate = date
        Log.d("DetailScheduleRVA", "setSelectedDate 호출됨")
        notifyDataSetChanged()
    }

    init {
        viewModel.getUser()
    }

    inner class DetailScheduleViewHolder(private val binding: ItemTodayScheduleDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.tvDetailScheduleTitle
        val repeatInfo = binding.tvDetailScheduleRepeatInfo
        val category = binding.tvDetailScheduleCategory
        val categoryImage = binding.ivDetailScheduleCategory
        val time = binding.tvDetailScheduleTime
        val timeType = binding.tvDetailScheduleTimeType

        val memberContainer = binding.linearLayout

        val view = binding.viewDetailSchedule
        val bar = binding.viewDetailScheduleBar
        val container = binding.layoutDetailScheduleContainer
        val checkBtn = binding.imgDetailScheduleCheck

        fun bind(item: ScheduleByDateResponseModel) {

//            viewModel.user.observe(viewLifecycleOwner) { user ->
//                memberContainer.removeAllViews()
//                addProfileToContainer(user.profileImage)
//            }
            viewModel.loadScheduleUsers(item.scheduleId) // ViewModel에서 캐싱하도록 호출
            val users = viewModel.scheduleUserMap.value?.get(item.scheduleId) ?: emptyList()

            memberContainer.removeAllViews()
            if(users.isEmpty()) {
                viewModel.user.observe(viewLifecycleOwner) { user ->
                    memberContainer.removeAllViews()
                    addProfileToContainer(user.profileImage)
                }
            }
            for (user in users) {
                Log.d("DetailScheduleRVA", "user: $user")
                addProfileToContainer(user.profile)
            }
        }

        private fun addProfileToContainer(imageUrl: String) {
            val inflater = LayoutInflater.from(itemView.context)
            val profile: View = inflater.inflate(R.layout.item_schedule_member, memberContainer, false)
            val profileImage = profile.findViewById<ImageView>(R.id.img_detail_schedule_member)

            Glide.with(itemView.context).load(imageUrl).into(profileImage)
            memberContainer.addView(profile)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailScheduleViewHolder {
        val view = ItemTodayScheduleDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DetailScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailScheduleViewHolder, position: Int) {
        val meetTime = detailSchedules[position].meetTime
        val parsedTime = parseTimeString(meetTime)
        if (parsedTime != null) {
            val (hours, minutes, seconds) = parsedTime
            val formattedHours = if (hours == 0) 12 else if (hours > 12) hours - 12 else hours
            val formattedMinutes = String.format("%02d", minutes)

            holder.timeType.text = if (hours < 12) "오전" else "오후"
            holder.time.text = "${String.format("%02d", formattedHours)}:${formattedMinutes}"
        }

        holder.title.text = detailSchedules[position].name
        if (detailSchedules[position].repeatDays != null) {
            holder.repeatInfo.text = repeatDaysToKo(detailSchedules[position].repeatDays!!)
        } else {
            holder.repeatInfo.text = ""
        }
        holder.category.text = detailSchedules[position].categories[0].categoryName //카테고리 색 반영 나중에
        holder.category.setTextColor(holder.category.context.getColor(CategoryManager.getColor(detailSchedules[position].categories[0].categoryColor)))
        holder.categoryImage.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, CategoryManager.getColor(detailSchedules[position].categories[0].categoryColor)))

        val isChecked = checklistDone.any { it.scheduleId == detailSchedules[position].scheduleId } || !isTimeBeforeNow(meetTime)
        Log.d("DetailScheduleRVA ${position}", "isChecked: $isChecked")

        Log.d("DetailScheduleRVA selectedDate", "selectedDate: $selectedDate")
        Log.d("DetailScheduleRVA isDateAfterToday", isDateAfterToday(selectedDate).toString())

        if(!isChecked && !isDateAfterToday(selectedDate)) {
            holder.view.setBackgroundResource(R.drawable.shape_rect_999_mint_fill)
            holder.container.setBackgroundResource(R.drawable.shape_rect_28_mint200_fill_mint_line_1)
            holder.time.setTextColor(holder.time.context.getColor(R.color.mint_600))
            holder.checkBtn.setImageResource(R.drawable.ic_schedule_detail_check_mint)
        } else {
            holder.view.setBackgroundResource(R.drawable.shape_rect_999_gray300_fill)
            holder.container.setBackgroundResource(R.drawable.shape_rect_28_gray100_fill_gray300_line_1)
            holder.time.setTextColor(holder.time.context.getColor(R.color.gray_500))
            holder.checkBtn.setImageResource(R.drawable.ic_schedule_detail_check)
        }

        holder.bar.visibility =
            if (position == detailSchedules.size - 1) View.GONE else View.VISIBLE

        holder.itemView.setOnClickListener {
            onItemClick.invoke(detailSchedules[position])
        }
        holder.bind(detailSchedules[position])
    }

    override fun getItemCount(): Int = detailSchedules.size

    private fun dayConverter(day: String): String {
        when (day) {
            "MONDAY" -> return "월"
            "TUESDAY" -> return "화"
            "WEDNESDAY" -> return "수"
            "THURSDAY" -> return "목"
            "FRIDAY" -> return "금"
            "SATURDAY" -> return "토"
            "SUNDAY" -> return "일"
            else -> return ""
        }
    }

    private fun repeatDaysToKo(repeatDays: List<String>): String {
        var koDays = ""
        if (repeatDays.size == 7) koDays = "매일"
        else {
            koDays = repeatDays.map { dayConverter(it) }.joinToString(", ")
        }
        return koDays + " 반복"
    }

    private fun parseTimeString(timeString: String): Triple<Int, Int, Int>? {
        // 입력 문자열이 유효한 형식인지 확인
        if (!timeString.matches(Regex("\\d{2}:\\d{2}:\\d{2}"))) {
            return null
        }
        val parts = timeString.split(":")
        if (parts.size != 3) {
            return null
        }

        val hours = parts[0].toIntOrNull()
        val minutes = parts[1].toIntOrNull()
        val seconds = parts[2].toIntOrNull()

        // 변환 실패 시 null 반환
        if (hours == null || minutes == null || seconds == null) {
            return null
        }

        return Triple(hours, minutes, seconds)
    }

    private fun isTimeBeforeNow(timeString: String): Boolean {
        // 현재 시간 가져오기
        val now = LocalDateTime.now()

        // 시간 문자열 파싱
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val parsedTime = LocalTime.parse(timeString, timeFormatter)

        // 특정 시간의 LocalDateTime 생성
        val specificDateTime = LocalDateTime.of(now.toLocalDate(), parsedTime)

        // 시간 비교
        return specificDateTime.isBefore(now)
    }

    private fun isDateAfterToday(dateString: String): Boolean {
        // 현재 날짜 가져오기
        val today = LocalDate.now()

        // 날짜 문자열 파싱
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val parsedDate = LocalDate.parse(dateString, dateFormatter)

        // 날짜 비교
        return parsedDate.isAfter(today)
    }


    fun Context.toPx(dp: Int): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        resources.displayMetrics
    )
}
