package com.umc.timeCAlling.presentation.calendar

import android.graphics.Color

data class DetailSchedule (
    val title: String,
    val repeatInfo: String,
    val category: String,
    val isMorning: Boolean,
    val time: String,
    val memberCount: Int
)