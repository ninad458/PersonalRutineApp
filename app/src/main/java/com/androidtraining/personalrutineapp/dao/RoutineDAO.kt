package com.androidtraining.personalrutineapp.dao

import androidx.room.*
import com.androidtraining.personalrutineapp.entity.Routine
import java.util.*

@Dao
interface RoutineDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRoutine(routine: Routine): Long

    @Update
    fun updateRoutine(routine: Routine)

    @Delete
    fun deleteRoutine(routine: Routine)

    @Query("SELECT * FROM trainee_routine WHERE due_Day >= :due")
    fun getRoutineByDueDate(due: Date): List<Routine>

    @Query("SELECT * FROM trainee_routine")
    fun getAllRoutines(): List<Routine>

}