package com.umc.timeCAlling.util

import android.content.Context
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.net.http.HttpException
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.auth0.android.jwt.JWT
import com.google.firebase.messaging.FirebaseMessaging
import com.umc.timeCAlling.data.datasource.LoginDataSource
import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.alarm.FcmTokenRequestDto
import com.umc.timeCAlling.data.dto.request.login.TokenRefreshRequestDto
import com.umc.timeCAlling.data.dto.response.alarm.FcmTokenResponseDto
import com.umc.timeCAlling.data.service.AlarmService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

@HiltWorker
class TokenRefreshWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val sharedPreferences: SharedPreferences,
    private val loginDataSourceProvider: Lazy<LoginDataSource>,
    private val alarmService: AlarmService
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        try {
            Log.d("TokenRefreshWorker", "doWork: TokenRefreshWorker 시작")
            // 액세스 토큰 만료 여부 확인 및 재발급 로직
            val isTokenExpired = checkAccessTokenExpiration()
            if (isTokenExpired) {
                refreshToken()
            }

            // FCM 토큰 업데이트 로직
            updateFCMToken()
            Log.d("TokenRefreshWorker", "doWork: TokenRefreshWorker 종료")
            return Result.success()
        } catch (e: Exception) {
            Log.e("TokenRefreshWorker", "doWork: TokenRefreshWorker 실패", e)
            return Result.failure()
        }
    }

    private fun checkAccessTokenExpiration(): Boolean {
        val accessToken = sharedPreferences.getString("jwt", "") ?: ""
        val expirationTime = getAccessTokenExpirationTime(accessToken) // 저장된 만료 시간 가져오기
        val currentTime = Calendar.getInstance().timeInMillis
        Log.d("TokenRefreshWorker", "checkAccessTokenExpiration: currentTime: $currentTime, expirationTime: $expirationTime")
        return currentTime >= expirationTime
    }

    private fun getAccessTokenExpirationTime(accessToken: String): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, 1)
        return calendar.timeInMillis
    }

    private suspend fun refreshToken() {
        val accessToken = sharedPreferences.getString("jwt", "") ?: ""
        val refreshToken = sharedPreferences.getString("refreshToken", "") ?: ""
        val requestDto = TokenRefreshRequestDto(accessToken, refreshToken)

        try {
            val loginDataSource = loginDataSourceProvider.value
            val response = loginDataSource.tokenRefresh(requestDto)
            if (response.isSuccess && response.result != null) {
                val newAccessToken = response.result.accessToken
                Log.d("TokenRefreshWorker", "새로운 Access Token 발급 완료: $newAccessToken")
                saveAccessToken(newAccessToken)
            } else {
                Log.e("TokenRefreshWorker", "토큰 갱신 실패: ${response.message}")
            }
        } catch (e: Exception) {
            Log.e("TokenRefreshWorker", "Access Token 갱신 중 예외 발생: ${e.message}")
        }
    }

    private fun saveAccessToken(accessToken: String) {
        // 새로운 액세스 토큰 저장
        // 예: SharedPreferences에 토큰 저장
        sharedPreferences.edit().putString("jwt", accessToken).apply()
        Log.d("TokenRefreshWorker", "saveAccessToken: accessToken: $accessToken")
    }

    private suspend fun updateFCMToken() {
        val fcmToken = FirebaseMessaging.getInstance().token.await()
        val response: BaseResponse<FcmTokenResponseDto> = alarmService.fcmToken(
            FcmTokenRequestDto(fcmToken)
        )
        Log.d("TokenRefreshWorker", "response: $response")
        if (response.isSuccess) {
            val result = response.result
            Log.d(
                "TokenRefreshWorker",
                "FCM token sent to server successfully. User ID: ${result?.userId}"
            )
            saveFCMToken(fcmToken)
        } else {
            Log.e(
                "TokenRefreshWorker",
                "Failed to send FCM token to server. Code: ${response.code}, Message: ${response.message}"
            )
            // Handle error, retry, or notify user
        }
    }

    private fun saveFCMToken(token: String) {
        // FCM 토큰 저장 로직
        // 예: SharedPreferences에 토큰 저장
        sharedPreferences.edit().putString("fcmToken", token).apply()
        Log.d("TokenRefreshWorker", "saveFCMToken: token: $token")
    }
}