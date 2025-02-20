package com.umc.timeCAlling.presentation.alarm

import android.Manifest
import android.app.AlarmManager
import android.app.KeyguardManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.icu.util.Calendar
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
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.semantics.text
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ActivityAlarmBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import kotlin.text.clear
import kotlin.text.format
import java.util.Locale

@AndroidEntryPoint
class AlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmBinding
    private var mediaPlayer: MediaPlayer? = null
    private var alarmId: Int = 0
    private lateinit var spf: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("AlarmActivity", "onCreate() called")
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        spf = getSharedPreferences("my_app_prefs", Context.MODE_PRIVATE)
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
        val nickName = spf.getString("nickName", "") ?: "사용자"
        // Intent에서 알람 정보 가져오기
        alarmId = intent.getIntExtra("alarmId", 0)
        val alarmName = intent.getStringExtra("alarmName") ?: "Unknown Alarm"
        val formattedDate = intent.getStringExtra("formattedDate") ?: ""
        val fcmSenderNickname = intent.getStringExtra("senderNickname") ?: null

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

        // TextView에 알람 이름 설정
        alarmNameTextView.text = alarmName
        alarmDateTextView.text = formattedDate

        // TextView에 추가 텍스트 설정 (FCM data payload)
        if (alarmName != null && formattedDate != null && fcmSenderNickname != null) {
            alarmTitleTextView.text = "'${alarmName}" // 예시 텍스트
            alarmContentTextView.text = "${nickName}님, 벌써 약속 시간 다가와요!" // 예시 텍스트
            alarmContent2TextView.text = "${fcmSenderNickname}님이 깨우라고 하셨어요!" // 예시 텍스트
        } else {
            alarmTitleTextView.text = "${nickName}님의 알람" // 예시 텍스트
            alarmContentTextView.text = "약속 잘 지키셨겠죠?" // 예시 텍스트
            alarmContent2TextView.text = "오늘 일정도 좋은 시간 보내세요!" // 예시 텍스트
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