package  com.umc.timeCAlling.data.dto.response

import com.umc.timeCAlling.domain.model.TestModel

data class TestResponse (
    val body: String
){
    fun toTestModel() = TestModel(body)
}