package com.umc.timeCAlling.data.service

import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.checklist.UpdateChecklistRequestDto
import com.umc.timeCAlling.data.dto.response.checklist.UpdateChecklistResponseDto
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ChecklistService {

    @PATCH("/api/checklist/{schedule_id}")
    suspend fun updateChecklist(
        @Path("schedule_id") scheduleId: Int = 0,
        @Body request: UpdateChecklistRequestDto
    ) : BaseResponse<UpdateChecklistResponseDto>
}