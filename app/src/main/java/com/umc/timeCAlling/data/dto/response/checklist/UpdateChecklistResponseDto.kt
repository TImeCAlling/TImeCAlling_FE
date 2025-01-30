package com.umc.timeCAlling.data.dto.response.checklist

import com.umc.timeCAlling.domain.model.response.UpdateChecklistResponseModel

data class UpdateChecklistResponseDto(
    val checklistId: Int
) {
    fun toUpdateChecklistResponseModel() = UpdateChecklistResponseModel(checklistId)
}
