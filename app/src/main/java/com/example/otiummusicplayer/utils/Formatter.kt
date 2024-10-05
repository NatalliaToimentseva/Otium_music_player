package com.example.otiummusicplayer.utils

private const val TIME_FORMAT = "%02d:%02d"
private const val FORMAT_ERROR = "0"
private const val FORMAT_STRING_ERROR = 0

fun formatTimeMls(millis: Int?): String {
    return if (millis != null) {
        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        TIME_FORMAT.format(minutes, seconds)
    } else FORMAT_ERROR
}

fun formatTimeToMls(duration: String?): Int {
    return if (duration != null) {
        val minutesToMls = (duration.subSequence(0, 2).toString().toInt() * 60000)
        val secondsToMls = (duration.subSequence(3, 5).toString().toInt() * 1000)
        minutesToMls + secondsToMls
    } else FORMAT_STRING_ERROR
}

fun formatTimeSec(sec: Int?): String {
    return if (sec != null) {
        val minutes = sec / 60
        val seconds = sec % 60
        TIME_FORMAT.format(minutes, seconds)
    } else FORMAT_ERROR
}