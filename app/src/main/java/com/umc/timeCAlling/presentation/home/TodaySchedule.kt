package com.umc.timeCAlling.presentation.home

data class TodaySchedule(
    val title: String,
    val description: String,
    val isMorning: Boolean,
    val time: String,
    val timeLeft: String
)
