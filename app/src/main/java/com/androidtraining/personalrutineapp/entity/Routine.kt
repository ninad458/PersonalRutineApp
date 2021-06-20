package com.androidtraining.personalrutineapp.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "trainee_routine")
data class Routine(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "pk_routine_id")
        val routineId: Long = 0,
        @ColumnInfo(name = "due_day")
        val dueDay: Date
)