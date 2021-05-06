package com.androidtraining.personalrutineapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.reactivex.annotations.NonNull

@Entity
data class Gender(
        @NonNull
        @PrimaryKey
        val id: Int = 0,
        val name: String)