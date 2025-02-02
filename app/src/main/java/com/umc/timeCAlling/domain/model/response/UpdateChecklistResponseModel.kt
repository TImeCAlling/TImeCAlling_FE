package com.umc.timeCAlling.domain.model.response

import com.umc.timeCAlling.data.dto.response.checklist.UpdateChecklistResponseDto

data class UpdateChecklistResponseModel(
    val checklistId: Int
) {
    fun toUpdateChecklistResponseDto() = UpdateChecklistResponseDto(checklistId)
}
