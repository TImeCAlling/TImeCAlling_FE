package com.umc.timeCAlling.presentation.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.media.MediaPlayer
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        private var mediaPlayer: MediaPlayer? = null
    }

    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getIntExtra("alarmId", 0)
        val alarmName = intent.getStringExtra("alarmName") ?: ""
        val year = intent.getIntExtra("year", -1)
        val month = intent.getIntExtra("month", -1)
        val dayOfMonth = intent.getIntExtra("dayOfMonth", -1)
        val hourOfDay = intent.getIntExtra("hourOfDay", -1)
        val minute = intent.getIntExtra("minute", -1)

        val currentCalendar = Calendar.getInstance()
        val currentYear = currentCalendar.get(Calendar.YEAR)
        val currentMonth = currentCalendar.get(Calendar.MONTH)
        val currentDayOfMonth = currentCalendar.get(Calendar.DAY_OF_MONTH)
        val currentHourOfDay = currentCalendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = currentCalendar.get(Calendar.MINUTE)

        Log.d("AlarmReceiver", "Alarm received for: $alarmName, Date: $year-${month + 1}-$dayOfMonth, Time: $hourOfDay:$minute, alarmId: $alarmId")
        Log.d("AlarmReceiver", "Current Date: $currentYear-${currentMonth + 1}-$currentDayOfMonth")


        if (year == currentYear && month == currentMonth && dayOfMonth == currentDayOfMonth && hourOfDay == currentHourOfDay && minute == currentMinute) {            Log.d("AlarmReceiver", "Date matches. Starting AlarmActivity.")
            val alarmActivityIntent = Intent(context, AlarmActivity::class.java).apply {
                Log.d("AlarmReceiver", "Date and Time match. Starting AlarmActivity.")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("alarmId", alarmId)
                putExtra("alarmName", alarmName)
                putExtra("year", year)
                putExtra("month", month)
                putExtra("dayOfMonth", dayOfMonth)
                putExtra("hourOfDay", hourOfDay)
                putExtra("minute", minute)
            }
            context.startActivity(alarmActivityIntent)
        } else {
            Log.d("AlarmReceiver", "Date does not match. Canceling alarm.")
            val alarmHelper = AlarmHelper(context)
            alarmHelper.cancelAlarm(alarmId)
        }
    }
}