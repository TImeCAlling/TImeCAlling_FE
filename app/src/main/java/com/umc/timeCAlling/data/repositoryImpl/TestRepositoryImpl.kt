package com.umc.timeCAlling.data.repositoryImpl

import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.TestRequest
import com.umc.timeCAlling.data.dto.response.TestResponse
import com.umc.timeCAlling.data.service.TestService
import com.umc.timeCAlling.domain.model.TestModel
import com.umc.timeCAlling.domain.repository.TestRepository
import com.umc.timeCAlling.util.network.NetworkResult
import com.umc.timeCAlling.util.network.handleApi
import javax.inject.Inject

class TestRepositoryImpl @Inject constructor(
    private val testService: TestService
) : TestRepository {
    override suspend fun fetchTest(request: TestRequest): NetworkResult<TestModel> {
        return handleApi({testService.fetchTest(request)}) {response: BaseResponse<TestResponse> -> response.data.toTestModel()} // mapper를 통해 api 결과를 TestModel로 매핑
    }

}