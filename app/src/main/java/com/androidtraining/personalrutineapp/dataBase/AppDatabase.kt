package com.androidtraining.personalrutineapp.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androidtraining.personalrutineapp.converter.DateTypeConverter
import com.androidtraining.personalrutineapp.converter.ListConverter
import com.androidtraining.personalrutineapp.dao.ExerciseDao
import com.androidtraining.personalrutineapp.dao.GenderDao
import com.androidtraining.personalrutineapp.dao.RoutineDao
import com.androidtraining.personalrutineapp.dao.TraineeDao
import com.androidtraining.personalrutineapp.entity.Exercise
import com.androidtraining.personalrutineapp.entity.Gender
import com.androidtraining.personalrutineapp.entity.Routine
import com.androidtraining.personalrutineapp.entity.Trainee

@Database(
    entities = [Exercise::class, Gender::class, Routine::class, Trainee::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTypeConverter::class, ListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun genderDao(): GenderDao
    abstract fun routineDao(): RoutineDao
    abstract fun traineeDao(): TraineeDao

    companion object {
        var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase {
            if (INSTANCE == null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "myDB").build()
                }
            }
            return INSTANCE!!
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}