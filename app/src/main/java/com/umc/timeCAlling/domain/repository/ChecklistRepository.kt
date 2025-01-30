package com.umc.timeCAlling.domain.repository

import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.domain.model.request.checklist.UpdateChecklistRequestModel
import com.umc.timeCAlling.domain.model.response.UpdateChecklistResponseModel

interface ChecklistRepository {
    suspend fun updateChecklist(requestModel: UpdateChecklistRequestModel): Result<UpdateChecklistResponseModel>
}