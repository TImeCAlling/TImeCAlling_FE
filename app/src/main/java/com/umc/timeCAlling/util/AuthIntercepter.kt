package com.umc.timeCAlling.util

import android.content.SharedPreferences
import android.util.Log
import com.auth0.android.jwt.JWT
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var accessToken = sharedPreferences.getString("jwt", "") ?: ""

        /*accessToken = "스웨거에서 받은 accessToken"
        //refreshToken = "스웨거에서 받은 refreshToken"
        sharedPreferences.edit().apply{
            putString("jwt", accessToken)
            //putString("refreshToken", refreshToken)
        }*/

        Log.d("AuthInterceptor", "Current Access Token: $accessToken")
        Log.d("AuthInterceptor", "Current Refresh Token: $refreshToken")
        // JWT 디코딩 후 만료 여부 확인
        if (isAccessTokenExpired(accessToken)) {
            Log.d("AuthInterceptor","Access Token이 만료되었습니다. Refresh Token을 사용하여 새 토큰을 요청합니다.")

            if (refreshToken.isNotEmpty()) {
                val newAccessToken = runBlocking { refreshAccessToken(refreshToken) }
                if (newAccessToken != null) {
                    accessToken = newAccessToken
                    Log.d("AuthInterceptor","새로운 Access Token 발급 완료: $accessToken")
                } else {
                    Log.e("AuthInterceptor","토큰 갱신 실패")
                }
            }else{
                Log.e("AuthInterceptor","Refresh Token이 없습니다.")
            }
        }

        // 요청에 최신 Access Token 추가
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(request)
    }

    // Access Token 만료 시간 로그, 만료 시 재발급 요청
    private fun logTokenExpiration(token: String?, chain: Interceptor.Chain): Response? {
        if (token.isNullOrEmpty()) {
            Log.d("AuthInterceptor", "Access Token 없음. 만료 여부 확인 생략.")
            return null
        }

        return try {
            val jwt = JWT(token)
            val expirationTime = jwt.expiresAt?.time ?: 0
            val currentTime = System.currentTimeMillis()
            val remainingTimeMs = expirationTime - currentTime

            if (remainingTimeMs > 0) {
                val remainingMinutes = remainingTimeMs / (1000 * 60)  // 분 단위
                val remainingSeconds = (remainingTimeMs / 1000) % 60  // 초 단위
                Log.d("AuthInterceptor", "Access Token 유효 시간: ${remainingMinutes}분 ${remainingSeconds}초 남음")
                null  // 만료되지 않음 → 기존 흐름 유지
            } else {
                Log.e("AuthInterceptor", "Access Token이 만료됨. 요청 차단 및 재발급 처리.")
                chain.proceed(chain.request())  // 만료된 경우 요청을 그대로 진행 (재발급 로직이 처리)
            }
        } catch (e: Exception) {
            Log.e("AuthInterceptor", "JWT 파싱 실패: ${e.message}")
            null  // 파싱 실패 시 만료 여부 확인하지 않고 기존 흐름 유지
        }
    }
}
