package  com.umc.timeCAlling.data.dto

data class BaseResponse<T>(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: T
)