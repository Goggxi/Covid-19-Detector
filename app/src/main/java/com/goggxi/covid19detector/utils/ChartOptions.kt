package com.goggxi.covid19detector.utils

enum class Metric {
    CONFIRMED, NEGATIVE, POSITIVE, DEATH
}

enum class TimeScale(val numDays: Int) {
    WEEK(7),
    MONTH(30),
    MAX(-1)
}
