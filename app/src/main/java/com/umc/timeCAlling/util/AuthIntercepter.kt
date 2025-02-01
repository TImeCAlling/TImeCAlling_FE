package com.umc.timeCAlling.util

import android.content.SharedPreferences
import android.util.Log
import com.auth0.jwt.JWT
import com.umc.timeCAlling.data.datasource.LoginDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import retrofit2.Retrofit
import timber.log.Timber
import java.util.Date
import javax.inject.Inject
import javax.inject.Named

class AuthInterceptor @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = sharedPreferences.getString("jwt", "") ?: ""

        Log.d(",", "AuthInterceptor - Access Token: $accessToken") // 액세스 토큰 로그 출력

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        Log.d("", "AuthInterceptor - Request Headers: ${request.headers}") // 요청 헤더 로그 출력

        var response = chain.proceed(request)

        if (response.code == 401) {
            val kakaoUserId = sharedPreferences.getString("kakaoUserId", null)
            val refreshToken = sharedPreferences.getString("refreshToken", "") ?: ""

            if (kakaoUserId.isNullOrEmpty()) {
                Timber.e("Kakao User ID가 없습니다. 리프레시 요청을 수행하지 않습니다.")
                return response
            }
            /*if (isRefreshTokenExpired(refreshToken)) {
                Timber.e("리프레시 토큰이 만료되었습니다.")
                return response
            }*/

            //   val refreshRequest = RefreshAuthUserRequestDto(kakaoUserId, refreshToken)
            //   val newAccessToken = runBlocking { refreshAccessToken(refreshRequest) }

            /*if (newAccessToken != null) {
                Timber.d("기존 액세스 토큰: $accessToken")
                Timber.d("새로운 액세스 토큰: $newAccessToken")

                sharedPreferences.edit().putString("jwt", newAccessToken).apply()

                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $newAccessToken")
                    .build()
                response = chain.proceed(newRequest)
            } else {
                Timber.e("리프레시 토큰 요청 실패")
            }*/
        }
        return response
    }
}