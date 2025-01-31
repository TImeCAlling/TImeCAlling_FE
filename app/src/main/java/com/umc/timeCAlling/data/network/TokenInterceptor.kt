package com.umc.timeCAlling.data.network

import android.content.SharedPreferences
import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException

class TokenInterceptor(
    private val sharedPreferences: SharedPreferences,
    private val okHttpClient: OkHttpClient
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken = sharedPreferences.getString("jwt", null) ?: return chain.proceed(originalRequest)

        // 🔹 강제로 Access Token을 잘못된 값으로 설정 (테스트용)
        val brokenAccessToken = "invalid_token"

        // 요청에 AccessToken 추가
        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        // test
        Log.d("TokenInterceptor", "🚀 API 요청 전: Access Token = $brokenAccessToken")

        val response = chain.proceed(authenticatedRequest)

        // 401 Unauthorized 발생 시 AccessToken 재발급
        if (response.code == 401) {
            Log.w("TokenInterceptor", "401 Unauthorized 발생! 토큰 재발급 시도...")

            synchronized(this) { // 동기화 처리
                val newAccessToken = refreshToken()

                return if (newAccessToken != null) {
                    // 새로운 AccessToken으로 기존 요청 다시 시도
                    val newRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()

                    response.close() // 기존 응답 닫기
                    chain.proceed(newRequest) // 새 Access Token으로 요청 재시도
                } else {
                    Log.e("TokenInterceptor", "토큰 재발급 실패! API 요청 실패")
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

        Log.d("TokenInterceptor", "Refresh Token 사용하여 Access Token 재발급 요청...")

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

                Log.d("TokenInterceptor", "새 Access Token 저장 완료: $newAccessToken")
                Log.d("TokenInterceptor", "새 Refresh Token 저장 완료: $newRefreshToken")

                newAccessToken
            } else {
                Log.e("TokenInterceptor", "토큰 재발급 실패! 서버 응답: ${response.code}")
                null
            }
        } catch (e: IOException) {
            Log.e("TokenInterceptor", "네트워크 오류로 인해 토큰 재발급 실패: ${e.message}")
            e.printStackTrace()
            null
        }
    }
}
