package com.androidtraining.personalrutineapp.entity

import androidx.room.*

@Entity
data class Exercise(
        @PrimaryKey(autoGenerate = true)
        val exerciseId: Int = 0,
        val name: String,
        val repetitions:Int,
        @ColumnInfo(name = "machine_name")
        val machineName: String,
        val liftedWeight: Int)