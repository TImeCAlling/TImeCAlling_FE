package com.umc.timeCAlling.data.datasource

import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.checklist.UpdateChecklistRequestDto
import com.umc.timeCAlling.data.dto.response.checklist.UpdateChecklistResponseDto

interface ChecklistDataSource {
    suspend fun updateChecklist(requestDto: UpdateChecklistRequestDto): BaseResponse<UpdateChecklistResponseDto>
}