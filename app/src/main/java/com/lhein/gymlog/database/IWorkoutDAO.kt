package com.lhein.gymlog.database

import com.lhein.gymlog.model.Workout

interface IWorkoutDAO {
    fun create( workout: Workout): Boolean
    fun update( workout: Workout ): Boolean
    fun remove( idWorkout: Int ): Boolean
    fun list(): List<Workout>
}