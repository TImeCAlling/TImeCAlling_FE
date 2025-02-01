package com.umc.timeCAlling.data.network

import android.content.SharedPreferences
import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
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

        // 강제로 Access Token을 잘못된 값으로 설정 (테스트용)
        val brokenAccessToken = "invalid_token"

        // 요청에 AccessToken 추가
        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $brokenAccessToken") // 테스트 중
            .build()

        // test
        Log.d("TokenInterceptor", "API 요청 전: Access Token = $brokenAccessToken")

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
        Log.d("TokenInterceptor", "Refresh Token: $refreshToken")

        val requestBodyJson = JSONObject().apply {
            put("accessToken", "")
            put("refreshToken", refreshToken)
        }.toString()

        val requestBody = requestBodyJson.toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("http://43.202.195.202:8080/swagger-ui/index.html#/user-controller/refreshToken") // 서버 URL
            .post(requestBody)
            .build()

        return try {
            val response = okHttpClient.newCall(request).execute()
            Log.d("TokenInterceptor", "Refresh Token 요청 응답 코드: ${response.code}")

            val responseBody = response.body?.string()
            Log.d("TokenInterceptor", "Refresh Token 응답 본문: $responseBody")

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
                Log.e("TokenInterceptor", "서버 응답 메시지: $responseBody")
                null
            }
        } catch (e: IOException) {
            Log.e("TokenInterceptor", "네트워크 오류로 인해 토큰 재발급 실패: ${e.message}")
            e.printStackTrace()
            null
        }
    }
}
