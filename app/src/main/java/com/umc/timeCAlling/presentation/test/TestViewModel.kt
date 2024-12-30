package com.umc.timeCAlling.presentation.test

import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.timeCAlling.data.dto.request.TestRequest
import com.umc.timeCAlling.domain.repository.TestRepository
import com.umc.timeCAlling.util.network.onError
import com.umc.timeCAlling.util.network.onException
import com.umc.timeCAlling.util.network.onFail
import com.umc.timeCAlling.util.network.onSuccess
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TestViewModel @Inject constructor( // @Inject : 의존성 주입을 받겠다.
    private val repository: TestRepository
) : ViewModel() {
    private val _testState = MutableStateFlow<String>("test") // state flow는 초기값 필요
    val testState: StateFlow<String> get() = _testState

    private val _testShared = MutableSharedFlow<String>() // shared flow는 초기값 필요 X
    val testShared: SharedFlow<String> get() = _testShared

    fun test(){
        // api 호출은 coroutine scope 안에서만 가능
        viewModelScope.launch {
            // repository를 이용해 testService의 fetchTest api 호출
            repository.fetchTest(TestRequest("test")).onSuccess {
                // 성공했을 때 로직

                // state flow는 .value = 값 or .emit(값)으로 flow 방출 가능
                _testState.value = it.body
                _testState.emit(it.body)

                // shared flow는 .emit(값으로만) flow 방출 가능
                _testShared.emit(it.body)
            }.onError {
                // error 났을 때 로직
            }.onException {
                // 예외 발생 시 로직
            }.onFail {
                // 실패했을 떄 로직
            }
        }
    }
}