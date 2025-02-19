package com.umc.timeCAlling.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.auth0.android.jwt.JWT
import com.umc.timeCAlling.R
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url.toString()

        val requestBuilder = originalRequest.newBuilder()

        if (originalUrl.contains("apis.openapi.sk.com")) {
            // ✅ SK OpenAPI 요청이면 appKey 추가 (Authorization 변경 X)
            val tmapAppKey = context.getString(R.string.tmap_app_key)
            requestBuilder.addHeader("appKey", tmapAppKey)
        } else {
            // ✅ 기존 로직 유지 (Access Token 추가)
            val accessToken = sharedPreferences.getString("jwt", "") ?: ""

            logTokenExpiration(accessToken, chain)?.let { return it }

            requestBuilder.addHeader("Authorization", "Bearer $accessToken")
        }

        return chain.proceed(requestBuilder.build())
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
