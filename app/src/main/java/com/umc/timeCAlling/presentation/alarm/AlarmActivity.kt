package com.umc.timeCAlling.presentation.alarm

import android.Manifest
import android.app.AlarmManager
import android.app.KeyguardManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.semantics.text
import androidx.core.content.ContextCompat
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ActivityAlarmBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.text.clear

@AndroidEntryPoint
class AlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmBinding
    private var mediaPlayer: MediaPlayer? = null
    private var alarmId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("AlarmActivity", "onCreate() called")
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 화면이 꺼져 있는지 확인
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        if (!powerManager.isInteractive) {
            // 화면이 꺼져 있다면 화면을 켭니다.
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }

        // 키가드 해제 및 화면 켜기
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }

        // Intent에서 알람 정보 가져오기
        alarmId = intent.getIntExtra("alarmId", 0)
        val alarmName = intent.getStringExtra("alarmName") ?: "Unknown Alarm"
        val year = intent.getIntExtra("year", 0)
        val month = intent.getIntExtra("month", 0)
        val dayOfMonth = intent.getIntExtra("dayOfMonth", 0)
        val hourOfDay = intent.getIntExtra("hourOfDay", 0)
        val minute = intent.getIntExtra("minute", 0)

        // Intent에서 FCM data payload 가져오기
        val fcmTitle = intent.getStringExtra("title")
        val fcmBody = intent.getStringExtra("body")
        val fcmScheduledDate = intent.getStringExtra("scheduledDate")
        val fcmSenderNickname = intent.getStringExtra("senderNickname")

        // 다른 앱 위에 윈도우 표시
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
            Log.d("AlarmActivity", "TYPE_APPLICATION_OVERLAY")
        } else {
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
            Log.d("AlarmActivity", "TYPE_SYSTEM_ALERT")
        }

        // 알람 이름과 날짜를 표시할 TextView
        val alarmNameTextView = findViewById<TextView>(R.id.tv_alarm_name)
        val alarmDateTextView = findViewById<TextView>(R.id.tv_alarm_date)
        val alarmTitleTextView = findViewById<TextView>(R.id.tv_alarm_title)
        val alarmContentTextView = findViewById<TextView>(R.id.tv_alarm_content)
        val alarmContent2TextView = findViewById<TextView>(R.id.tv_alarm_content_2)
        val alarmStopImageView = findViewById<ImageView>(R.id.iv_alarm_stop)

        // TextView에 알람 이름과 날짜 설정
        alarmNameTextView.text = alarmName
        alarmDateTextView.text = String.format("%04d-%02d-%02d %02d:%02d", year, month + 1, dayOfMonth, hourOfDay, minute)
        // TextView에 추가 텍스트 설정
        alarmTitleTextView.text = "'${alarmName}' 님의 알람" // 예시 텍스트
        alarmContentTextView.text = "김콜링님이 빨리 일어나라고 했어" // 예시 텍스트
        alarmContent2TextView.text = "준비 끝났지?" // 예시 텍스트

        // TextView에 추가 텍스트 설정 (FCM data payload)
        if (fcmTitle != null && fcmBody != null && fcmScheduledDate != null && fcmSenderNickname != null) {
            alarmTitleTextView.text = "'${fcmTitle}'" // 예시 텍스트
            alarmContentTextView.text = "${fcmSenderNickname}님이 빨리 일어나라고 했어" // 예시 텍스트
            alarmContent2TextView.text = "${fcmBody}" // 예시 텍스트
        } else {
            alarmTitleTextView.text = "'${alarmName}' 님의 알람" // 예시 텍스트
            alarmContentTextView.text = "김콜링님이 빨리 일어나라고 했어" // 예시 텍스트
            alarmContent2TextView.text = "준비 끝났지?" // 예시 텍스트
        }

        // 알람 끄기 버튼 클릭 리스너
        alarmStopImageView.setOnClickListener {
            stopAlarm()
            invalidateCache(alarmId) // "invalid cache" 수행
        }
        startAlarmSound()
    }

    private fun startAlarmSound() {
        if (mediaPlayer == null) {
            val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            mediaPlayer = MediaPlayer.create(this, ringtoneUri)
            mediaPlayer?.isLooping = true
        }
        mediaPlayer?.start()
    }

    private fun stopAlarm() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        val alarmHelper = AlarmHelper(this)
        alarmHelper.cancelAlarm(alarmId)

        finish()
    }

    private fun invalidateCache(alarmId: Int) {
        val alarmSettingsPreferences: SharedPreferences = getSharedPreferences("alarm_settings", Context.MODE_PRIVATE)
        alarmSettingsPreferences.edit().remove(alarmId.toString()).apply()

        Log.d("AlarmActivity", "Alarm setting for alarmId $alarmId invalidated.")
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAlarm()
    }
}