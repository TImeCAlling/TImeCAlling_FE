package com.umc.timeCAlling

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.alarm.FcmTokenRequestDto
import com.umc.timeCAlling.data.dto.response.alarm.FcmTokenResponseDto
import com.umc.timeCAlling.data.service.AlarmService
import com.umc.timeCAlling.domain.model.request.alarm.FcmTokenRequestModel
import com.umc.timeCAlling.domain.model.response.alarm.FcmTokenResponseModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

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

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("MyFirebaseMessagingService", "Message data payload: ${remoteMessage.data}")
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("MyFirebaseMessagingService", "Message Notification Body: ${it.body}")
        }
    }

    private fun sendRegistrationToServer(fcmToken: String) {
        val kakaoAccessToken = spf.getString("kakaoAccessToken", null)
        Log.d("MyFirebaseMessagingService", "kakaoAccessToken: $kakaoAccessToken")
        if (kakaoAccessToken == null) {
            Log.e("MyFirebaseMessagingService", "kakaoAccessToken is null")
            return
        }
        val authorization = "Bearer $kakaoAccessToken"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: BaseResponse<FcmTokenResponseDto> = alarmService.fcmToken(authorization, FcmTokenRequestDto(fcmToken))
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