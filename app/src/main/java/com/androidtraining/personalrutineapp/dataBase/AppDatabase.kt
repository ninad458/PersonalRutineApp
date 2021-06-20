package com.androidtraining.personalrutineapp.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.androidtraining.personalrutineapp.converter.Converters
import com.androidtraining.personalrutineapp.converter.DateTypeConverter
import com.androidtraining.personalrutineapp.converter.ListConverter
import com.androidtraining.personalrutineapp.dao.ExerciseDao
import com.androidtraining.personalrutineapp.dao.RoutineDao
import com.androidtraining.personalrutineapp.dao.TraineeDao
import com.androidtraining.personalrutineapp.entity.Exercise
import com.androidtraining.personalrutineapp.entity.Gender
import com.androidtraining.personalrutineapp.entity.Routine
import com.androidtraining.personalrutineapp.entity.Trainee

@Database(
    entities = [Exercise::class, Routine::class, Trainee::class],
    version = 3,
    exportSchema = true
)
@TypeConverters(DateTypeConverter::class, ListConverter::class, Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun routineDao(): RoutineDao
    abstract fun traineeDao(): TraineeDao

    companion object {
        var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase {
            if (INSTANCE == null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "myDB"
                    ).addMigrations(MIGRATION_1_2)
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE!!
        }

        fun destroyDataBase(){
            INSTANCE = null
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `temp_trainee` (`name` TEXT NOT NULL, `age` INTEGER NOT NULL, `gender` TEXT NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `routineId` INTEGER NOT NULL, `due_day` INTEGER NOT NULL, `exercises` TEXT NOT NULL)")
                val tempTraineeQueries = database.query("SELECT * FROM trainee").use { cursor ->
                    val trainees = mutableListOf<String>()
                    if (!cursor.moveToFirst()) return@use trainees
                    do {
                        val cnName = "name"
                        val cnAge = "age"
                        val cnGender = "gender"
                        val cnId = "id"
                        val cnRoutineId = "routineId"
                        val cnRoutineDueDay = "due_day"
                        val cnRoutineExercises = "exercises"
                        val name = cursor.getString(cursor.getColumnIndex(cnName))
                        val age = cursor.getInt(cursor.getColumnIndex(cnAge))
                        val gender = if (cursor.getInt(cursor.getColumnIndex(cnGender)) == 2) Gender.FEMALE else Gender.MALE
                        val id = cursor.getInt(cursor.getColumnIndex(cnId))
                        val routineId = cursor.getInt(cursor.getColumnIndex(cnRoutineId))
                        val routineDueDay = cursor.getLong(cursor.getColumnIndex(cnRoutineDueDay))
                        val routineExercises = cursor.getString(cursor.getColumnIndex(cnRoutineExercises))

                        trainees.add(
                            """INSERT INTO temp_trainee 
                            |($cnName, $cnAge, $cnGender, $cnId, $cnRoutineId, $cnRoutineDueDay, $cnRoutineExercises) 
                            |VALUES ('$name', $age, '${gender.name}', $id, $routineId, $routineDueDay, '$routineExercises');""".trimMargin()
                        )
                    } while (cursor.moveToNext())
                    return@use trainees
                }

                for (query in tempTraineeQueries) {
                    database.execSQL(query)
                }

                database.execSQL("DROP TABLE trainee;")
                database.execSQL("ALTER TABLE temp_trainee RENAME TO trainee;")

                database.execSQL("CREATE INDEX IF NOT EXISTS" + ("" +
                        " `index_Trainee_age` ON  'Trainee' (`age`)")
                )
                database.execSQL("CREATE INDEX IF NOT EXISTS" + ("" +
                        " `index_Trainee_name` ON  'Trainee' (`name`)")
                )
            }
        }
    }
}