package com.lhein.gymlog.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import com.google.android.material.shape.ShapeAppearanceModel
import com.lhein.gymlog.R
import com.lhein.gymlog.databinding.ItemWorkoutBinding
import com.lhein.gymlog.model.Workout

class WorkoutAdapter(
    private val onClick: (Workout) -> Unit,
    private val onCompleteClick: (Workout) -> Unit
) : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

    private var listWorkouts = emptyList<Workout>()

    fun updateList(newList: List<Workout>) {
        listWorkouts = newList
        notifyDataSetChanged()
    }

    inner class WorkoutViewHolder(val binding: ItemWorkoutBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(workout: Workout, position: Int) {
            val context = itemView.context

            // 1. Dados Básicos
            binding.txtExerciseName.text = workout.exerciseName
            
            val details = mutableListOf<String>()
            if (workout.sets > 0 && workout.reps > 0) {
                details.add("${workout.sets} x ${workout.reps}")
            } else if (workout.sets > 0) {
                details.add("${workout.sets} séries")
            } else if (workout.reps > 0) {
                details.add("${workout.reps} reps")
            }
            
            if (workout.weight > 0) {
                details.add("${workout.weight}kg")
            }
            
            if (workout.duration > 0) {
                details.add("${workout.duration}min")
            }
            
            if (details.isEmpty()) {
                binding.txtDetails.text = ""
                binding.imgWorkoutShape.visibility = android.view.View.GONE
            } else {
                binding.txtDetails.text = details.joinToString(" • ")
                binding.imgWorkoutShape.visibility = android.view.View.VISIBLE
            }

            // 2. Estado de Conclusão (Checkbox)
            binding.cbComplete.setOnCheckedChangeListener(null) // Evita trigger ao reciclar
            binding.cbComplete.isChecked = workout.isComplete
            
            // Visual feedback
            if (workout.isComplete) {
                binding.txtExerciseName.alpha = 0.5f
                binding.txtDetails.alpha = 0.5f
                binding.imgWorkoutShape.alpha = 0.5f
            } else {
                binding.txtExerciseName.alpha = 1.0f
                binding.txtDetails.alpha = 1.0f
                binding.imgWorkoutShape.alpha = 1.0f
            }

            binding.cbComplete.setOnCheckedChangeListener { _, isChecked ->
                onCompleteClick(workout.copy(isComplete = isChecked))
            }

            // 3. Badge Dinâmica por Grupo
            val group = workout.group
            binding.txtGroupBadge.text = group
            binding.txtGroupBadge.backgroundTintList = ColorStateList.valueOf(getBadgeColor(group))
            binding.txtGroupBadge.setTextColor(getBadgeTextColor(group))

            // 3. Lista de Shapes (Material Design 3)
            val shapes = listOf(
                R.style.ShapeAppearance_M3_Clamshell,
                R.style.ShapeAppearance_M3_Arch,
                R.style.ShapeAppearance_M3_Gem,
                R.style.ShapeAppearance_M3_Sunny,
                R.style.ShapeAppearance_M3_Cookie,
                R.style.ShapeAppearance_M3_Oval,
                R.style.ShapeAppearance_M3_Slanted,
                R.style.ShapeAppearance_M3_Arrow,
                R.style.ShapeAppearance_M3_Bun
            )

            // 4. Aplicação do Shape e Cor de Fundo Adaptável
            val selectedShape = shapes[position % shapes.size]
            binding.imgWorkoutShape.shapeAppearanceModel = ShapeAppearanceModel.builder(
                context,
                selectedShape,
                0
            ).build()

            // Pega a cor do tema (PrimaryContainer) para o shape ser Dark Mode friendly
            val shapeBgColor = MaterialColors.getColor(context, com.google.android.material.R.attr.colorPrimaryContainer, Color.LTGRAY)
            binding.imgWorkoutShape.backgroundTintList = ColorStateList.valueOf(shapeBgColor)

            // 5. Clique
            binding.root.setOnClickListener {
                onClick(workout)
            }
        }

        // Lógica de cores das badges (Container Colors)
        private fun getBadgeColor(group: String): Int {
            return when (group) {
                "Peito" -> Color.parseColor("#FFDAD6")   // Red Container
                "Costas" -> Color.parseColor("#D7E3FF")  // Blue Container
                "Pernas" -> Color.parseColor("#E4E199")  // Olive Container
                "Bíceps" -> Color.parseColor("#F2DAFF")  // Purple Container
                "Tríceps" -> Color.parseColor("#F2DAFF") // Purple Container
                "Ombros" -> Color.parseColor("#FFDCC0")  // Orange Container
                "Abdomem" -> Color.parseColor("#C6F0D2") // Green Container
                "Cardio" -> Color.parseColor("#B9F0FF")  // Cyan Container
                else -> Color.parseColor("#E1E2EC")      // Neutral Container
            }
        }

        // Lógica de cores do texto das badges (On-Container Colors)
        private fun getBadgeTextColor(group: String): Int {
            return when (group) {
                "Peito" -> Color.parseColor("#410002")
                "Costas" -> Color.parseColor("#001B3D")
                "Pernas" -> Color.parseColor("#1D1D00")
                "Bíceps" -> Color.parseColor("#250059")
                "Tríceps" -> Color.parseColor("#250059")
                "Ombros" -> Color.parseColor("#2F1500")
                "Abdomem" -> Color.parseColor("#00210A")
                "Cardio" -> Color.parseColor("#001F24")
                else -> Color.parseColor("#1A1C1E")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemWorkoutBinding.inflate(layoutInflater, parent, false)
        return WorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(listWorkouts[position], position)
    }

    override fun getItemCount(): Int = listWorkouts.size
}