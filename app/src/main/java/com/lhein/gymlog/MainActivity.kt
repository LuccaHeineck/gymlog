package com.lhein.gymlog

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayout
import com.lhein.gymlog.adapter.WorkoutAdapter
import com.lhein.gymlog.databinding.ActivityMainBinding
import com.lhein.gymlog.model.Workout

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private var listWorkouts = emptyList<Workout>()
    private val workoutAdapter: WorkoutAdapter? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //TODO filtrar apenas dias que ja tem treinos
        val diasSemana = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")
        diasSemana.forEach { dia ->
            binding.tabLayoutDays.addTab(binding.tabLayoutDays.newTab().setText(dia))
        }

        binding.tabLayoutDays.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val diaSelecionado = tab?.text.toString()
                // Chame sua função de filtro aqui:
                // viewModel.filtrarTreinosPorDia(diaSelecionado)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}