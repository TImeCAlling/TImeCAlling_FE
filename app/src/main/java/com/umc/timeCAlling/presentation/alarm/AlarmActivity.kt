package com.umc.timeCAlling.presentation.alarm

import android.app.KeyguardManager
import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ActivityAlarmBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class AlarmActivity : AppCompatActivity() , TextToSpeech.OnInitListener {
    private lateinit var binding: ActivityAlarmBinding
    private var mediaPlayer: MediaPlayer? = null
    private var alarmId: Int = 0
    private lateinit var spf: SharedPreferences
    private lateinit var tts: TextToSpeech
    private var isTtsInitialized = false
    private var ttsText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("AlarmActivity", "onCreate() called")
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        spf = getSharedPreferences("my_app_prefs", Context.MODE_PRIVATE)
        // TTS 초기화
        tts = TextToSpeech(this, this)
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
            // 알람 소리 음량 고정
            val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM)
            val fixedVolume = (maxVolume * 0.8).toInt() // 최대 음량의 80%로 설정
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, fixedVolume, 0)
            // 알람 소리 AudioAttributes 설정
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            mediaPlayer?.setAudioAttributes(audioAttributes)
        }
        mediaPlayer?.start()
        // TTS 출력 시작
        startTts()
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
        stopTts()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.KOREAN)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language not supported!")
            } else {
                isTtsInitialized = true
                // TTS 초기화 후 TTS 출력 시작
                startTts()
            }
        }else {
            Log.e("TTS", "Initialization Failed!")
        }
    }

    private fun startTts() {
        if (isTtsInitialized) {
            ttsText = "일어나세요!"
            speakOut()
        }
    }

    private fun speakOut() {
        if (isTtsInitialized) {
            // TTS 음량 고정
            val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM)
            val fixedVolume = (maxVolume * 0.8).toInt() // 최대 음량의 80%로 설정
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, fixedVolume, 0)
            // TTS AudioAttributes 설정
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
            tts.setAudioAttributes(audioAttributes)
            // TTS 출력
            tts.speak(ttsText, TextToSpeech.QUEUE_FLUSH, null, "tts1")
        }
    }

    private fun stopTts() {
        if (isTtsInitialized) {
            tts.stop()
            tts.shutdown()
            isTtsInitialized = false
        }
    }
}