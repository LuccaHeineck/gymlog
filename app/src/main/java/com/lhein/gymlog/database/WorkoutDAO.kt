package com.lhein.gymlog.database

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.lhein.gymlog.model.Workout

class WorkoutDAO(context: Context) : IWorkoutDAO {

    private val write = DatabaseHelper(context).writableDatabase
    private val read = DatabaseHelper(context).readableDatabase

    override fun create(workout: Workout): Boolean {
        val content = ContentValues()
        content.put(DatabaseHelper.COLUMN_EXERCISE_NAME, workout.exerciseName)
        content.put(DatabaseHelper.COLUMN_SETS, workout.sets)
        content.put(DatabaseHelper.COLUMN_REPS, workout.reps)
        content.put(DatabaseHelper.COLUMN_WEIGHT, workout.weight)
        content.put(DatabaseHelper.COLUMN_GROUP, workout.group)
        content.put(DatabaseHelper.COLUMN_DAY_OF_WEEK, workout.dayOfWeek)

        try {
            write.insert(
                DatabaseHelper.TABLE_WORKOUTS,
                null,
                content
            )
            Log.i("info_db", "Treino inserido com sucesso")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("info_db", "Erro ao inserir treino: ${e.message}")
            return false
        }

        return true

    }

    override fun update(workout: Workout): Boolean {
        val args = arrayOf(workout.idWorkout.toString())
        val content = ContentValues()
        content.put(DatabaseHelper.COLUMN_EXERCISE_NAME, workout.exerciseName)
        content.put(DatabaseHelper.COLUMN_SETS, workout.sets)
        content.put(DatabaseHelper.COLUMN_REPS, workout.reps)
        content.put(DatabaseHelper.COLUMN_WEIGHT, workout.weight)
        content.put(DatabaseHelper.COLUMN_GROUP, workout.group)
        content.put(DatabaseHelper.COLUMN_DAY_OF_WEEK, workout.dayOfWeek)

        try {
            write.update(
                DatabaseHelper.TABLE_WORKOUTS,
                content,
                "${DatabaseHelper.COLUMN_ID} = ?",
                args
            )
            Log.i("info_db", "Treino atualizado com sucesso")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("info_db", "Erro ao atualizar treino: ${e.message}")
            return false
        }

        return true
    }

    override fun remove(idWorkout: Int): Boolean {
        val args = arrayOf(idWorkout.toString())

        try {
            write.delete(
                DatabaseHelper.TABLE_WORKOUTS,
                "${DatabaseHelper.COLUMN_ID} = ?",
                args
            )
            Log.i("info_db", "Treino removido com sucesso")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("info_db", "Erro ao remover treino: ${e.message}")
            return false
        }

        return true
    }

    override fun list(): List<Workout> {
        val listWorkouts = mutableListOf<Workout>()

        val sql = "SELECT ${DatabaseHelper.COLUMN_ID}, " +
                "${DatabaseHelper.COLUMN_EXERCISE_NAME}, " +
                "${DatabaseHelper.COLUMN_SETS}, " +
                "${DatabaseHelper.COLUMN_REPS}, " +
                "${DatabaseHelper.COLUMN_WEIGHT}, " +
                "${DatabaseHelper.COLUMN_GROUP}, " +
                "${DatabaseHelper.COLUMN_DAY_OF_WEEK}, " +
                "    strftime('%d/%m/%Y %H:%M', ${DatabaseHelper.COLUMN_DATE}) ${DatabaseHelper.COLUMN_DATE} " +
                "FROM ${DatabaseHelper.TABLE_WORKOUTS}"

        val cursor = read.rawQuery(sql, null)

        val indexId = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)
        val indexExerciseName = cursor.getColumnIndex(DatabaseHelper.COLUMN_EXERCISE_NAME)
        val indexSets = cursor.getColumnIndex(DatabaseHelper.COLUMN_SETS)
        val indexReps = cursor.getColumnIndex(DatabaseHelper.COLUMN_REPS)
        val indexWeight = cursor.getColumnIndex(DatabaseHelper.COLUMN_WEIGHT)
        val indexGroup = cursor.getColumnIndex(DatabaseHelper.COLUMN_GROUP)
        val indexDayOfWeek = cursor.getColumnIndex(DatabaseHelper.COLUMN_DAY_OF_WEEK)
        val indexDate = cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE)

        while ( cursor.moveToNext() ) {
            val id = cursor.getInt(indexId)
            val exerciseName = cursor.getString(indexExerciseName)
            val sets = cursor.getInt(indexSets)
            val reps = cursor.getInt(indexReps)
            val weight = cursor.getDouble(indexWeight)
            val group = cursor.getString(indexGroup)
            val dayOfWeek = cursor.getString(indexDayOfWeek)
            val date = cursor.getString(indexDate)

            listWorkouts.add(Workout(id, exerciseName, sets, reps, weight, date, group, dayOfWeek))
        }
        return listWorkouts
    }
}