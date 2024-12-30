package com.umc.timeCAlling.domain.repository

import com.umc.timeCAlling.data.dto.request.TestRequest
import com.umc.timeCAlling.domain.model.TestModel
import com.umc.timeCAlling.util.network.NetworkResult

interface TestRepository {
    suspend fun fetchTest(request: TestRequest): NetworkResult<TestModel>
}