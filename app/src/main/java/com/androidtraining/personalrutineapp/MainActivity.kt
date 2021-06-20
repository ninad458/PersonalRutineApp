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
            val routineDao = db.routineDao()
            val traineeDao = db.traineeDao()
            val exerciseDao = db.exerciseDao()


            val routine = Routine(
                dueDay = Date(),
//                exercises = listOf(exercise.copy(exerciseId = savedRoutineId.toInt())).map { it.exerciseId }
            )

            val addedRoutineId = routineDao.insertRoutine(routine)

            val exercise = Exercise(
                name = "Pull down lats",
                repetitions = 3,
                machineName = "Jerrai",
                liftedWeight = 70,
                routineId = addedRoutineId
            )
            traineeDao.insertTrainee(
                Trainee(
                    "Rocky",
                    32,
                    Gender.MALE,
                    routine.copy(routineId = addedRoutineId)
                )
            )
            val savedRoutineId = exerciseDao.insertDayRoutine(exercise)

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
