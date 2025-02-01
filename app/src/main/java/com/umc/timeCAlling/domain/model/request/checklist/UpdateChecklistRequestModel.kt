package com.umc.timeCAlling.domain.model.request.checklist

import com.umc.timeCAlling.data.dto.request.checklist.UpdateChecklistRequestDto

data class UpdateChecklistRequestModel(
    val isSuccess: Boolean,
    val spare: String,
    val late: String,
    val reason: String,
    val externals: String,
    val isFit: Boolean,
    val date: String
) {
    fun toUpdateChecklistRequestDto() =  UpdateChecklistRequestDto(isSuccess, spare, late, reason, externals, isFit, date)
}
