package com.androidtraining.personalrutineapp.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(indices = [Index("name"), Index("age")])
data class Trainee(
        val name: String,
        val age: Int,
        val gender: Gender,
        @Embedded
        val routine: Routine,
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0)