package com.androidtraining.personalrutineapp.converter

import androidx.room.TypeConverter
import com.androidtraining.personalrutineapp.entity.Gender

object Converters {

    @TypeConverter
    fun stringToGender(value: String) = /*try {*/
        Gender.valueOf(value)
    /*} catch (e: Exception) {
        Gender.MALE
    }*/

    @TypeConverter
    fun genderToString(gender: Gender) = gender.name
}