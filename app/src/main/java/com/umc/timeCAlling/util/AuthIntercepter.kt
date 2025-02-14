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

        Log.d("AuthInterceptor", "Current Access Token: $accessToken")

        // JWT 만료 여부 확인 후 만료된 경우 요청 차단 (Authenticator가 처리)
        if (isAccessTokenExpired(accessToken)) {
            Log.d("AuthInterceptor", "Access Token이 만료됨. 요청 차단.")
            return chain.proceed(chain.request())
        }

        // 요청에 최신 Access Token 추가
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(request)
    }

    // JWT Access Token 만료 확인
    private fun isAccessTokenExpired(token: String?): Boolean {
        if (token.isNullOrEmpty()) return true

        return try {
            val jwt = JWT(token)
            jwt.isExpired(10) // 10초 여유 두고 체크
        } catch (e: Exception) {
            Log.e("AuthInterceptor", "JWT 파싱 실패: ${e.message}")
            true
        }
    }
}
