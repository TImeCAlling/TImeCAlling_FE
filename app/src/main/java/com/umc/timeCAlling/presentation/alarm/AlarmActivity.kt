package com.umc.timeCAlling.presentation.alarm

import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.semantics.text
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ActivityAlarmBinding
import com.umc.timeCAlling.databinding.ActivitySplashBinding
import com.umc.timeCAlling.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmActivity : AppCompatActivity() {
    private var ringtone: Ringtone? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        playAlarmSound()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        // 알람 이름과 날짜를 표시할 TextView
        val alarmNameTextView = findViewById<TextView>(R.id.tv_alarm_name)
        val alarmDateTextView = findViewById<TextView>(R.id.tv_alarm_date)
        val alarmTitleTextView = findViewById<TextView>(R.id.tv_alarm_title)
        val alarmContentTextView = findViewById<TextView>(R.id.tv_alarm_content)
        val alarmContent2TextView = findViewById<TextView>(R.id.tv_alarm_content_2)
        val alarmStopImageView = findViewById<ImageView>(R.id.iv_alarm_stop)

        // Intent에서 알람 이름과 날짜 가져오기
        val alarmName = intent.getStringExtra("alarmName") ?: "Unknown Alarm"
        val alarmDateTime = intent.getStringExtra("alarmDateTime") ?: "Unknown Date and Time"

        // TextView에 알람 이름과 날짜 설정
        alarmNameTextView.text = alarmName
        alarmDateTextView.text = alarmDateTime

        // TextView에 추가 텍스트 설정
        alarmTitleTextView.text = "'홍길동' 님의 알람" // 예시 텍스트
        alarmContentTextView.text = "김콜링님이 빨리 일어나라고 했어" // 예시 텍스트
        alarmContent2TextView.text = "준비 끝났지?" // 예시 텍스트

        // 알람 끄기 버튼 클릭 리스너
        alarmStopImageView.setOnClickListener {
            finishAffinity() // 앱 전체 종료
            System.exit(0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Activity가 종료될 때 알람음 중지
        stopAlarmSound()
    }

    private fun stopAlarmSound() {
        // 알람음 중지
        ringtone?.stop()
        ringtone = null
    }

    private fun playAlarmSound() {
        try {
            // 기본 알람음 URI 가져오기
            val alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

            // Ringtone 객체 생성
            ringtone = RingtoneManager.getRingtone(this, alarmUri)

            // 알람음 재생
            ringtone?.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}