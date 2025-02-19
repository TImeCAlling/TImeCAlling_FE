package com.umc.timeCAlling.domain.model.request.checklist

import com.umc.timeCAlling.data.dto.request.checklist.UpdateChecklistRequestDto
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

data class UpdateChecklistRequestModel(
    var isSuccess: Boolean = false,
    var spare: String = "",
    var late: String = "",
    var reason: String = "",
    var externals: String = "",
    var isFit: Boolean =false,
    var date: String = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()).toString()
) {
    constructor() : this(false, "10분 이상", "5분 미만", "", "", false,  DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()).toString())
    fun toUpdateChecklistRequestDto() =  UpdateChecklistRequestDto(isSuccess, spare, late, reason, externals, isFit, date)
}

