package com.androidtraining.personalrutineapp.entity

import androidx.room.*


@Entity(indices = [Index("name"), Index("age")],
        foreignKeys = [ForeignKey(entity = Gender::class, parentColumns = ["id"], childColumns = ["gender"])])
data class Trainee(
        val name: String,
        val age: Int,
        val gender: Int?,
        @Embedded
        val routine: Routine,
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0)