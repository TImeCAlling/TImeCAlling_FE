package com.umc.timeCAlling.util

import android.content.SharedPreferences
import android.util.Log
import com.auth0.android.jwt.JWT
import com.umc.timeCAlling.data.datasource.LoginDataSource
import com.umc.timeCAlling.data.dto.request.login.TokenRefreshRequestDto
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val loginDataSource: LoginDataSource
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var accessToken = sharedPreferences.getString("jwt", "") ?: ""
        val refreshToken = sharedPreferences.getString("refreshToken", "") ?: ""

        Log.d("AuthInterceptor", "Current Access Token: $accessToken")

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

        var response = chain.proceed(request)

        // 401 응답이 온 경우 > Refresh Token 사용하여 Access Token 갱신
        if (response.code == 401 && refreshToken.isNotEmpty()) {
            val newAccessToken = runBlocking { refreshAccessToken(refreshToken) }
            if (newAccessToken != null) {
                Log.d("AuthInterceptor","401 오류 발생 → 새로운 Access Token 발급 완료: $newAccessToken")

                sharedPreferences.edit().putString("jwt", newAccessToken).apply()

                // 새로운 Access Token으로 다시 요청
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $newAccessToken")
                    .build()
                response = chain.proceed(newRequest)
            } else {
                Log.e("AuthInterceptor","401 오류 발생 → Refresh Token도 만료됨. 로그아웃 필요")
            }
        }

        return response
    }

    // JWT Access Token 만료되었는지 확인
    private fun isAccessTokenExpired(token: String?): Boolean {
        if (token.isNullOrEmpty()) return true

        return try {
            val jwt = JWT(token)
            jwt.isExpired(10) // 10초 여유 두고 체크
        } catch (e: Exception) {
            Log.e("AuthInterceptor","JWT 파싱 실패: ${e.message}")
            true // JWT 파싱 실패 시 만료된 것으로 간주
        }
    }

    // Refresh Token을 이용하여 새로운 Access Token 요청
    private suspend fun refreshAccessToken(refreshToken: String): String? {
        val requestDto = TokenRefreshRequestDto("", refreshToken)
        return try {
            val response = loginDataSource.tokenRefresh(requestDto)

            if (response.isSuccess && response.result != null) {
                val newAccessToken = response.result.accessToken
                sharedPreferences.edit().putString("jwt", newAccessToken).apply()
                Log.d("AuthInterceptor","새로운 Access Token 발급 완료: $newAccessToken")
                newAccessToken
            } else {
                Log.e("AuthInterceptor","토큰 갱신 실패: 서버 응답 실패 (${response.message})")
                null
            }
        } catch (e: Exception) {
            Log.e("AuthInterceptor","Access Token 갱신 실패: ${e.message}")
            null
        }
    }
}