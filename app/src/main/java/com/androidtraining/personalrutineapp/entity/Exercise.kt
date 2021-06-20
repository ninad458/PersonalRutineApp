package com.androidtraining.personalrutineapp.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
        foreignKeys = [ForeignKey(
                entity = Routine::class,
                childColumns = arrayOf("fk_routine_id"),
                parentColumns = arrayOf("pk_routine_id"),
                onUpdate = ForeignKey.CASCADE,
                onDelete = ForeignKey.CASCADE
        )]
)
data class Exercise(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "pk_exercise_id")
        val exerciseId: Int = 0,
        val name: String,
        val repetitions: Int,
        @ColumnInfo(name = "machine_name")
        val machineName: String,
        val liftedWeight: Int,
        @ColumnInfo(name = "fk_routine_id")
        val routineId: Long
)