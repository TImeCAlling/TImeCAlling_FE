package com.umc.timeCAlling.data.repositoryImpl

import com.umc.timeCAlling.data.datasource.ChecklistDataSource
import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.domain.model.request.checklist.UpdateChecklistRequestModel
import com.umc.timeCAlling.domain.model.response.UpdateChecklistResponseModel
import com.umc.timeCAlling.domain.repository.ChecklistRepository
import javax.inject.Inject

class ChecklistRepositoryImpl @Inject constructor(
    private val checklistDataSource: ChecklistDataSource
) : ChecklistRepository {
    override suspend fun updateChecklist(scheduleId: Int, requestModel: UpdateChecklistRequestModel): Result<UpdateChecklistResponseModel> =
        runCatching {checklistDataSource.updateChecklist(scheduleId, requestModel.toUpdateChecklistRequestDto()).result.toUpdateChecklistResponseModel()}
}