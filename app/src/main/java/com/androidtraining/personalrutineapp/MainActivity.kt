package com.androidtraining.personalrutineapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidtraining.personalrutineapp.dataBase.AppDatabase
import com.androidtraining.personalrutineapp.databinding.ActivityMainBinding
import com.androidtraining.personalrutineapp.entity.Exercise
import com.androidtraining.personalrutineapp.entity.Gender
import com.androidtraining.personalrutineapp.entity.Routine
import com.androidtraining.personalrutineapp.entity.Trainee
import com.androidtraining.personalrutineapp.util.viewBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class MainActivity : AppCompatActivity() {

    private val disposable = CompositeDisposable()

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        disposable.add(Observable.fromCallable {
            val db = AppDatabase.getAppDataBase(context = this)
            val genderDao = db.genderDao()
            val routineDao = db.routineDao()
            val traineeDao = db.traineeDao()
            val exerciseDao = db.exerciseDao()

            val gender1 = Gender(1, name = "Male")
            val gender2 = Gender(2, name = "Female")

            with(genderDao) {
                insertGender(gender1)
                insertGender(gender2)
            }

            val exercise = Exercise(
                name = "Pull down lats",
                repetitions = 3,
                machineName = "Jerrai",
                liftedWeight = 70
            )

            val savedRoutineId = exerciseDao.insertDayRoutine(exercise)

            val routine = Routine(
                dueDay = Date(),
                exercises = listOf(exercise.copy(exerciseId = savedRoutineId.toInt()))
            )

            val addedRoutineId = routineDao.insertRoutine(routine)

            traineeDao.insertTrainee(
                Trainee(
                    "Rocky",
                    32,
                    gender1.id,
                    routine.copy(routineId = addedRoutineId.toInt())
                )
            )

            return@fromCallable traineeDao.getAll()
        }.map { list -> list.joinToString("\n") { it.toString() } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(binding.tvMessage::setText)
        )
    }

    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
