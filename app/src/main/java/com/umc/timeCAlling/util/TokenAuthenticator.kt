package com.umc.timeCAlling.util

import android.content.SharedPreferences
import android.util.Log
import com.umc.timeCAlling.data.datasource.LoginDataSource
import com.umc.timeCAlling.data.dto.request.login.TokenRefreshRequestDto
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val loginDataSourceProvider: Lazy<LoginDataSource>  // ✅ Lazy로 변경
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = sharedPreferences.getString("refreshToken", "") ?: ""
        val accessToken = sharedPreferences.getString("jwt", "") ?: ""

        if (refreshToken.isEmpty()) {
            Log.e("TokenAuthenticator", "401 발생 → Refresh Token 없음 → 로그아웃 처리")
            forceLogout()
            return null
        }

        val newAccessToken = runBlocking {
            refreshAccessToken(accessToken, refreshToken)
        }

        return if (newAccessToken != null) {
            sharedPreferences.edit().putString("jwt", newAccessToken).apply()
            response.request.newBuilder()
                .header("Authorization", "Bearer $newAccessToken")
                .build()
        } else {
            Log.e("TokenAuthenticator", "401 발생 → Access Token 갱신 실패 → 로그아웃 처리")
            forceLogout()
            return null
        }
    }

    private suspend fun refreshAccessToken(accessToken: String, refreshToken: String): String? {
        val requestDto = TokenRefreshRequestDto(accessToken, refreshToken)  // ✅ Access Token도 함께 전달

        return try {
            val loginDataSource = loginDataSourceProvider.get()  // ✅ Lazy로 가져오기
            val response = loginDataSource.tokenRefresh(requestDto)
            if (response.isSuccess && response.result != null) {
                val newAccessToken = response.result.accessToken
                Log.d("TokenAuthenticator", "새로운 Access Token 발급 완료: $newAccessToken")
                newAccessToken
            } else {
                Log.e("TokenAuthenticator", "토큰 갱신 실패: ${response.message}")
                null
            }
        } catch (e: Exception) {
            Log.e("TokenAuthenticator", "Access Token 갱신 중 예외 발생: ${e.message}")
            null
        }
    }

    private fun forceLogout() {
        sharedPreferences.edit().clear().apply()
        Log.e("TokenAuthenticator", "모든 토큰 삭제 → 강제 로그아웃 완료")
    }
}
