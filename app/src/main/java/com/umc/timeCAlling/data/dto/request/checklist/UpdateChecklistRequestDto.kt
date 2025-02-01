package com.umc.timeCAlling.data.dto.request.checklist

import com.umc.timeCAlling.domain.model.request.checklist.UpdateChecklistRequestModel


data class UpdateChecklistRequestDto(
    val isSuccess: Boolean,
    val spare: String,
    val late: String,
    val reason: String,
    val externals: String,
    val isFit: Boolean,
    val date: String
) {
    fun toUpdateChecklistRequestModel() = UpdateChecklistRequestModel(isSuccess, spare, late, reason, externals, isFit, date)
}

