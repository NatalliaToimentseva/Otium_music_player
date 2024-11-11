package com.example.otiummusicplayer.utils

private const val TIME_FORMAT = "%02d:%02d"
private const val FORMAT_ERROR = "0"
private const val FORMAT_STRING_ERROR = 0
private const val MLS_IN_SEC = 1000
private const val MLS_IN_MIN = 60000
private const val SEC_IN_MIN = 60
private const val MIN_REGEX_LOWER_BORDER = 0
private const val MIN_REGEX_UPPER_BORDER = 2
private const val SEC_REGEX_LOWER_BORDER = 3
private const val SEC_REGEX_UPPER_BORDER = 5

fun formatTimeMls(millis: Int?): String {
    return if (millis != null) {
        val totalSeconds = millis / MLS_IN_SEC
        val minutes = totalSeconds / SEC_IN_MIN
        val seconds = totalSeconds % SEC_IN_MIN
        TIME_FORMAT.format(minutes, seconds)
    } else FORMAT_ERROR
}

fun formatTimeToMls(duration: String?): Int {
    return if (duration != null) {
        val minutesToMls = (duration.subSequence(MIN_REGEX_LOWER_BORDER, MIN_REGEX_UPPER_BORDER).toString().toInt() * MLS_IN_MIN )
        val secondsToMls = (duration.subSequence(SEC_REGEX_LOWER_BORDER, SEC_REGEX_UPPER_BORDER).toString().toInt() * MLS_IN_SEC)
        minutesToMls + secondsToMls
    } else FORMAT_STRING_ERROR
}

fun formatTimeSec(sec: Int?): String {
    return if (sec != null) {
        val minutes = sec / SEC_IN_MIN
        val seconds = sec % SEC_IN_MIN
        TIME_FORMAT.format(minutes, seconds)
    } else FORMAT_ERROR
}