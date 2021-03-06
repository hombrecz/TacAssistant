package cz.hombre.militaryReportsAssistant.services

import java.util.*

interface DateTimeService {

    fun getMilitaryDateTimeGroup(): String

    fun getMilitaryDateTimeGroup(date: Date): String

    fun getLocalTime(): String

    fun getLocalTime(date: Date): String

    fun getLocalDate(): String

    fun getLocalDateTime() : String

    fun getTimeDifference(from: Date): String
}