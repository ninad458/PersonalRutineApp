package com.androidtraining.personalrutineapp.dao

import androidx.room.*
import com.androidtraining.personalrutineapp.entity.Exercise

@Dao
interface ExerciseDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDayRoutine(exercise: Exercise): Long

    @Update
    fun updateDayRoutine(exercise: Exercise)

    @Delete
    fun deleteDayRoutine(exercise: Exercise)

    @Query("SELECT * FROM Exercise WHERE name == :exerciseName")
    fun getExerciseByName(exerciseName: String): List<Exercise>
}