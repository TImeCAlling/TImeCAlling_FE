package com.umc.timeCAlling.presentation.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.add
import java.util.Calendar
import java.util.Locale
import kotlin.text.equals
import kotlin.text.set

class AlarmHelper(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setAlarm(alarmName: String, year: Int, month: Int, dayOfMonth: Int, hourOfDay: Int, minute: Int, alarmId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Log.e("AlarmHelper", "Cannot schedule exact alarms: permission denied")
                // 권한이 없는 경우 사용자에게 권한을 요청하는 로직을 추가해야 합니다.
                // 예를 들어, 사용자에게 권한을 요청하는 Intent를 시작할 수 있습니다.
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                context.startActivity(intent)
                return
            }
        }
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("alarmName", alarmName)
            putExtra("year", year)
            putExtra("month", month)
            putExtra("dayOfMonth", dayOfMonth)
            putExtra("hourOfDay", hourOfDay)
            putExtra("minute", minute)
            putExtra("alarmId", alarmId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // 알람 설정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }

        Log.d("AlarmHelper", "Alarm set for $alarmName at ${calendar.time}, alarmId: $alarmId")
    }

    fun cancelAlarm(alarmId: Int) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
        Log.d("AlarmHelper", "Alarm canceled for alarmId: $alarmId")
    }
}