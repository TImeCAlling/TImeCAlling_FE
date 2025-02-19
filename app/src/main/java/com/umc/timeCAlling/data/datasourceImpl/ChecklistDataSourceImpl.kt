package com.umc.timeCAlling.data.datasourceImpl

import com.umc.timeCAlling.data.datasource.ChecklistDataSource
import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.checklist.UpdateChecklistRequestDto
import com.umc.timeCAlling.data.dto.response.checklist.UpdateChecklistResponseDto
import com.umc.timeCAlling.data.service.ChecklistService
import javax.inject.Inject

class ChecklistDataSourceImpl @Inject constructor(
    private val checklistService: ChecklistService
) : ChecklistDataSource {
    override suspend fun updateChecklist(scheduleId: Int, requestDto: UpdateChecklistRequestDto): BaseResponse<UpdateChecklistResponseDto> =
        checklistService.updateChecklist(scheduleId = scheduleId,request = requestDto)
}