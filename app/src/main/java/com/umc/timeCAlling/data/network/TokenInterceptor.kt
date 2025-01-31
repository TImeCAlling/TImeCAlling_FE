package com.umc.timeCAlling.data.network

import android.content.SharedPreferences
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class TokenInterceptor(
    private val sharedPreferences: SharedPreferences,
    private val okHttpClient: OkHttpClient
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken = sharedPreferences.getString("jwt", null) ?: return chain.proceed(originalRequest)

        // 요청에 AccessToken 추가
        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        val response = chain.proceed(authenticatedRequest)

        // 401 Unauthorized 발생 시 AccessToken 재발급
        if (response.code == 401) {
            synchronized(this) { // 동기화 처리
                val newAccessToken = refreshToken()

                return if (newAccessToken != null) {
                    // 새로운 AccessToken으로 기존 요청 다시 시도
                    val newRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()

                    response.close() // 기존 응답 닫기
                    chain.proceed(newRequest) // 기존 요청 재시도
                } else {
                    response // 토큰 갱신 실패 시 기존 응답 반환
                }
            }
        }

        return response
    }

    /**
     * RefreshToken을 사용하여 새로운 AccessToken 요청
     */
    private fun refreshToken(): String? {
        val refreshToken = sharedPreferences.getString("refreshToken", null) ?: return null

        val requestBody = RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            JSONObject().apply {
                put("accessToken", "")
                put("refreshToken", refreshToken)
            }.toString()
        )

        val request = Request.Builder()
            .url("https://your.api.url/api/users/token/refresh") // 실제 서버 URL 사용
            .post(requestBody)
            .build()

        return try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val jsonResponse = JSONObject(responseBody ?: "{}")

                val newAccessToken = jsonResponse.getString("accessToken")
                val newRefreshToken = jsonResponse.getString("refreshToken")

                // 새로운 토큰을 SharedPreferences에 저장
                sharedPreferences.edit().apply {
                    putString("jwt", newAccessToken)
                    putString("refreshToken", newRefreshToken)
                    apply()
                }

                newAccessToken
            } else {
                null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
