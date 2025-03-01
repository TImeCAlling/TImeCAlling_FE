package com.umc.timeCAlling

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.alarm.FcmTokenRequestDto
import com.umc.timeCAlling.data.dto.response.alarm.FcmTokenResponseDto
import com.umc.timeCAlling.data.service.AlarmService
import com.umc.timeCAlling.presentation.alarm.AlarmActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import kotlin.text.format

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var alarmService: AlarmService

    @Inject
    lateinit var spf: SharedPreferences

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("MyFirebaseMessagingService", "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("MyFirebaseMessagingService", "From: ${remoteMessage.from}")

        if (remoteMessage.data.isNotEmpty()) {
            Log.d("MyFirebaseMessagingService", "Message data payload: ${remoteMessage.data}")
            val title = remoteMessage.data["title"] ?: "기본 제목"
            val body = remoteMessage.data["body"] ?: "기본 내용"
            val scheduledDate = remoteMessage.data["scheduledDate"] ?: ""
            val senderNickname = remoteMessage.data["senderNickname"] ?: "알 수 없는 사용자"

            // 날짜 포맷 변경
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
            val date = dateFormat.parse(scheduledDate)
            val calendar = Calendar.getInstance()
            if (date != null) {
                calendar.time = date
            }
            val formattedDate = SimpleDateFormat("M월 d일 (E)", Locale.KOREAN).format(calendar.time)

            // AlarmActivity 실행
            val intent = Intent(this, AlarmActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra("alarmName", title) // alarmName에 title 설정
                putExtra("formattedDate", formattedDate) // formattedDate 설정
                putExtra("senderNickname", senderNickname) // senderNickname 설정
                putExtra("body", body) // body 설정
            }
            startActivity(intent)
        }
    }

    private fun sendRegistrationToServer(fcmToken: String) {

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: BaseResponse<FcmTokenResponseDto> = alarmService.fcmToken(FcmTokenRequestDto(fcmToken))
                Log.d("MyFirebaseMessagingService", "response: $response")
                if (response.isSuccess) {
                    val result = response.result
                    Log.d("MyFirebaseMessagingService", "FCM token sent to server successfully. User ID: ${result?.userId}")
                } else {
                    Log.e("MyFirebaseMessagingService", "Failed to send FCM token to server. Code: ${response.code}, Message: ${response.message}")
                    // Handle error, retry, or notify user
                }
            } catch (e: HttpException) {
                Log.e("MyFirebaseMessagingService", "HTTP error: ${e.code()}, ${e.message()}")
                // Handle HTTP error, retry, or notify user
            } catch (e: IOException) {
                Log.e("MyFirebaseMessagingService", "Network error: ${e.message}")
                // Handle network error, retry, or notify user
            } catch (e: Exception) {
                Log.e("MyFirebaseMessagingService", "Unexpected error: ${e.message}")
                // Handle unexpected error, retry, or notify user
            }
        }
    }
}