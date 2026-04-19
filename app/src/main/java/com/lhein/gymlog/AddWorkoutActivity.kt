package com.lhein.gymlog

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.lhein.gymlog.database.WorkoutDAO
import com.lhein.gymlog.databinding.ActivityAddWorkoutBinding
import com.lhein.gymlog.model.Workout

class AddWorkoutActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAddWorkoutBinding.inflate(layoutInflater) }
    private var workoutExistente: Workout? = null
    private val workoutDAO by lazy { WorkoutDAO(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupSpinners()
        setupListeners()

        val bundle = intent.extras
        if (bundle != null) {
            workoutExistente = bundle.getSerializable("workout") as? Workout
            workoutExistente?.let { preencherCampos(it) }
        } else {
            binding.btnExcluir.visibility = View.GONE
        }

        binding.btnSalvar.setOnClickListener {
            if (validarCampos()) {
                if (workoutExistente != null) editar(workoutExistente!!) else salvar()
            }
        }

        binding.btnBack.setOnClickListener { finish() }

        binding.btnExcluir.setOnClickListener {
            workoutExistente?.let { excluir(it.idWorkout) }
        }
    }

    private fun setupListeners() {
        binding.editNomeExercicio.addTextChangedListener { binding.layoutNome.error = null }
        binding.editSeries.addTextChangedListener { binding.layoutSeries.error = null }
        binding.editReps.addTextChangedListener { binding.layoutReps.error = null }
    }

    private fun validarCampos(): Boolean {
        var isValid = true

        if (binding.editNomeExercicio.text.toString().isEmpty()) {
            binding.layoutNome.error = "Campo obrigatório"
            isValid = false
        }

        return isValid
    }

    private fun preencherCampos(workout: Workout) {
        binding.textTitulo.text = "Editar Treino"
        binding.btnSalvar.text = "Atualizar"
        binding.btnExcluir.visibility = View.VISIBLE

        binding.editNomeExercicio.setText(workout.exerciseName)
        binding.editSeries.setText(workout.sets.toString())
        binding.editReps.setText(workout.reps.toString())
        binding.editPeso.setText(workout.weight.toString())
        binding.editDuracao.setText(workout.duration.toString())

        setSpinnerValue(binding.spinnerDia, workout.dayOfWeek)
        setSpinnerValue(binding.spinnerGrupo, workout.group)
    }

    private fun excluir(id: Int) {
        if (workoutDAO.remove(id)) {
            Toast.makeText(this, "Removido com sucesso", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setSpinnerValue(spinner: android.widget.Spinner, value: String) {
        val adapter = spinner.adapter as? ArrayAdapter<String>
        val position = adapter?.getPosition(value) ?: -1
        if (position >= 0) spinner.setSelection(position)
    }

    private fun salvar() {
        val workout = capturarDadosFormulario(-1)
        if (workoutDAO.create(workout)) {
            Toast.makeText(this, "Treino salvo!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun editar(workout: Workout) {
        val workoutAtualizado = capturarDadosFormulario(workout.idWorkout)
        if (workoutDAO.update(workoutAtualizado)) {
            Toast.makeText(this, "Treino atualizado!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun capturarDadosFormulario(id: Int): Workout {
        return Workout(
            idWorkout = id,
            exerciseName = binding.editNomeExercicio.text.toString(),
            sets = binding.editSeries.text.toString().toIntOrNull() ?: 0,
            reps = binding.editReps.text.toString().toIntOrNull() ?: 0,
            weight = binding.editPeso.text.toString().toDoubleOrNull() ?: 0.0,
            date = "",
            group = binding.spinnerGrupo.selectedItem.toString(),
            dayOfWeek = binding.spinnerDia.selectedItem.toString(),
            duration = binding.editDuracao.text.toString().toIntOrNull() ?: 0,
            isComplete = workoutExistente?.isComplete ?: false
        )
    }

    private fun setupSpinners() {
        val dias = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")
        val grupos = listOf("Peito", "Costas", "Pernas", "Bíceps", "Tríceps", "Ombros", "Abdomem", "Cardio")

        binding.spinnerDia.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dias)
        binding.spinnerGrupo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, grupos)
    }
}