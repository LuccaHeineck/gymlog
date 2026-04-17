package com.lhein.gymlog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lhein.gymlog.databinding.ItemWorkoutBinding
import com.lhein.gymlog.model.Workout

class WorkoutAdapter(
    private val onClick: (Workout) -> Unit // Função que será chamada no clique
) : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

    private var listWorkouts = emptyList<Workout>()

    fun updateList(newList: List<Workout>) {
        listWorkouts = newList
        notifyDataSetChanged()
    }

    inner class WorkoutViewHolder(val binding: ItemWorkoutBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(workout: Workout) {
            binding.txtExerciseName.text = workout.exerciseName
            binding.txtGroupBadge.text = workout.group

            // Configura o clique no card
            binding.root.setOnClickListener {
                onClick(workout)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemWorkoutBinding.inflate(layoutInflater, parent, false)
        return WorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = listWorkouts[position]
        holder.bind(workout)
    }

    override fun getItemCount(): Int = listWorkouts.size
}