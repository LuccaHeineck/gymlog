package com.lhein.gymlog.model

import java.io.Serializable

data class Workout (
    val idWorkout: Int,
    val exerciseName: String,
    val sets: Int,
    val reps: Int,
    val weight: Double,
    val date: String,
    val group: String,
    val dayOfWeek: String,
    val duration: Int,
    val isComplete: Boolean
) : Serializable
