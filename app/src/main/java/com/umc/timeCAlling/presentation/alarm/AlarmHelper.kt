package com.umc.timeCAlling.presentation.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.fragment.app.add
import java.util.Calendar
import java.util.Locale
import kotlin.text.equals
import kotlin.text.set

class AlarmHelper(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setAlarm(alarmName: String, year: Int, month: Int, dayOfMonth: Int, hourOfDay: Int, minute: Int, scheduleId: Int) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("alarmName", alarmName)
            putExtra("year", year)
            putExtra("month", month)
            putExtra("dayOfMonth", dayOfMonth)
            putExtra("hourOfDay", hourOfDay)
            putExtra("minute", minute)
            putExtra("scheduleId", scheduleId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            scheduleId, // 요청 코드 (알람마다 고유한 값 사용)
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // 알람 설정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                Log.e("AlarmHelper", "Cannot schedule exact alarms on this device.")
                // 정확한 알람을 설정할 수 없는 경우에 대한 처리 (예: 토스트 메시지 표시)를 추가할 수 있습니다.
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }

        // 알람 정보 저장 (SharedPreferences 사용)
        saveAlarmInfo(alarmName, year, month, dayOfMonth, hourOfDay, minute, scheduleId)
        Log.d("AlarmHelper", "Alarm set for $alarmName at ${calendar.time}, Schedule ID: $scheduleId")
    }

    fun setRepeatingAlarm(
        alarmName: String,
        startDate: String,
        endDate: String,
        repeatDays: List<String>,
        hourOfDay: Int,
        minute: Int,
        scheduleId: Int
    ) {
        val startCalendar = parseDateToCalendar(startDate)
        val endCalendar = parseDateToCalendar(endDate)
        val now = Calendar.getInstance()

        if (startCalendar.before(now)) {
            startCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        while (startCalendar.before(endCalendar) || startCalendar.equals(endCalendar)) {
            val dayOfWeek = getDayOfWeek(startCalendar)
            if (repeatDays.contains(dayOfWeek)) {
                startCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                startCalendar.set(Calendar.MINUTE, minute)
                startCalendar.set(Calendar.SECOND, 0)

                val intent = Intent(context, AlarmReceiver::class.java).apply {
                    putExtra("alarmName", alarmName)
                    putExtra("year", startCalendar.get(Calendar.YEAR))
                    putExtra("month", startCalendar.get(Calendar.MONTH))
                    putExtra("dayOfMonth", startCalendar.get(Calendar.DAY_OF_MONTH))
                    putExtra("hourOfDay", hourOfDay)
                    putExtra("minute", minute)
                    putExtra("scheduleId", scheduleId)
                    putExtra("isRepeating", true)
                    putExtra("startDate", startDate)
                    putExtra("endDate", endDate)
                    putExtra("repeatDays", repeatDays.toTypedArray())
                }

                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    scheduleId,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (alarmManager.canScheduleExactAlarms()) {
                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            startCalendar.timeInMillis,
                            pendingIntent
                        )
                    } else {
                        Log.e("AlarmHelper", "Cannot schedule exact alarms on this device.")
                    }
                } else {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        startCalendar.timeInMillis,
                        pendingIntent
                    )
                }
                Log.d("AlarmHelper", "Repeating alarm set for $alarmName at ${startCalendar.time}, Schedule ID: $scheduleId")
            }
            startCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
    }

    private fun saveAlarmInfo(alarmName: String, year: Int, month: Int, dayOfMonth: Int, hourOfDay: Int, minute: Int, scheduleId: Int) {
        // SharedPreferences를 사용하여 알람 정보 저장
        val sharedPreferences = context.getSharedPreferences("alarm_prefs", Context.MODE_PRIVATE)
        val alarmDateTime = String.format(Locale.getDefault(), "%04d-%02d-%02d %02d:%02d", year, month + 1, dayOfMonth, hourOfDay, minute)
        with(sharedPreferences.edit()) {
            putString("alarmName", alarmName)
            putString("alarmDateTime", alarmDateTime)
            putInt("year", year)
            putInt("month", month)
            putInt("dayOfMonth", dayOfMonth)
            putInt("hourOfDay", hourOfDay)
            putInt("minute", minute)
            putInt("scheduleId", scheduleId)
            apply()
        }
    }

    private fun parseDateToCalendar(dateString: String): Calendar {
        val parts = dateString.split("-")
        val year = parts[0].toInt()
        val month = parts[1].toInt() - 1
        val dayOfMonth = parts[2].toInt()
        return Calendar.getInstance().apply {
            set(year, month, dayOfMonth)
        }
    }

    private fun getDayOfWeek(calendar: Calendar): String {
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "MONDAY"
            Calendar.TUESDAY -> "TUESDAY"
            Calendar.WEDNESDAY -> "WEDNESDAY"
            Calendar.THURSDAY -> "THURSDAY"
            Calendar.FRIDAY -> "FRIDAY"
            Calendar.SATURDAY -> "SATURDAY"
            Calendar.SUNDAY -> "SUNDAY"
            else -> ""
        }
    }
}