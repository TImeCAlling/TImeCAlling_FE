package com.umc.timeCAlling.presentation.checkList

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.timeCAlling.domain.model.request.checklist.UpdateChecklistRequestModel
import com.umc.timeCAlling.domain.repository.ChecklistRepository
import com.umc.timeCAlling.presentation.home.helper.ChecklistSharedPreferencesHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckListViewModel @Inject constructor(
    private val checklistRepository: ChecklistRepository
) : ViewModel() {
    private val _checkListId = MutableLiveData<Int>()
    val checkListId: LiveData<Int> get() = _checkListId

    fun updateChecklist(scheduleId: Int, request: UpdateChecklistRequestModel) {
        viewModelScope.launch {
            checklistRepository.updateChecklist(scheduleId, request).onSuccess { response ->
                _checkListId.value = response.checklistId
                Log.d("CheckListViewModel" , response.toString())
            }.onFailure { error ->
                // 에러 처리
                Log.d("CheckListViewModel" , error.toString())
            }
        }
    }
}