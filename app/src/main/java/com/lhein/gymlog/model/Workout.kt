package com.lhein.gymlog.model

data class Workout (
    val idWorkout: Int,
    val exerciseName: String,
    val sets: Int,
    val reps: Int,
    val weight: Double,
    val date: String,
    val group: String,
    val dayOfWeek: String
)
