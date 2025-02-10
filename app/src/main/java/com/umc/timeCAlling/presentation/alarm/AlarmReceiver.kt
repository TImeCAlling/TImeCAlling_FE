package com.umc.timeCAlling.presentation.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.util.Log
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.text.format

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmReceiver", "Alarm received")

        val alarmName = intent.getStringExtra("alarmName") ?: "Unknown Alarm"
        val year = intent.getIntExtra("year", 0)
        val month = intent.getIntExtra("month", 0)
        val dayOfMonth = intent.getIntExtra("dayOfMonth", 0)
        val hourOfDay = intent.getIntExtra("hourOfDay", 0)
        val minute = intent.getIntExtra("minute", 0)
        val scheduleId = intent.getIntExtra("scheduleId", -1)
        val isRepeating = intent.getBooleanExtra("isRepeating", false)
        val startDate = intent.getStringExtra("startDate") ?: ""
        val endDate = intent.getStringExtra("endDate") ?: ""
        val repeatDays = intent.getStringArrayExtra("repeatDays") ?: emptyArray()

        // AlarmActivity를 시작하는 Intent 생성
        val alarmIntent = Intent(context, AlarmActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("alarmName", alarmName)
            putExtra("year", year)
            putExtra("month", month)
            putExtra("dayOfMonth", dayOfMonth)
            putExtra("hourOfDay", hourOfDay)
            putExtra("minute", minute)
            putExtra("scheduleId", scheduleId)
            putExtra("isRepeating", isRepeating)
            putExtra("startDate", startDate)
            putExtra("endDate", endDate)
            putExtra("repeatDays", repeatDays)
        }

        // AlarmActivity 시작
        context.startActivity(alarmIntent)

        // 알람 정보 로그 출력
        Log.d("AlarmReceiver", "Alarm received for $alarmName at $year-$month-$dayOfMonth $hourOfDay:$minute, Schedule ID: $scheduleId")

        // 반복 알람인 경우 다음 알람 설정
        if (isRepeating) {
            val startDate = intent.getStringExtra("startDate") ?: ""
            val endDate = intent.getStringExtra("endDate") ?: ""
            val repeatDays = intent.getStringArrayExtra("repeatDays")?.toList() ?: emptyList()

            val alarmHelper = AlarmHelper(context)
            //alarmHelper.setRepeatingAlarm(alarmName, startDate, endDate, repeatDays, alarmHourOfDay, alarmMinute, scheduleId)
            Log.d("AlarmReceiver", "Repeating alarm: Start Date: $startDate, End Date: $endDate, Repeat Days: ${repeatDays.joinToString()}")        }
    }
}
