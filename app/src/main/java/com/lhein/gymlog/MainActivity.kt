package com.lhein.gymlog

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.lhein.gymlog.adapter.WorkoutAdapter
import com.lhein.gymlog.database.WorkoutDAO
import com.lhein.gymlog.databinding.ActivityMainBinding
import com.lhein.gymlog.model.Workout

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var workoutAdapter: WorkoutAdapter
    private val workoutDAO by lazy { WorkoutDAO(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Inicializa o Adapter com a nova lógica: clicar no card abre a tela de edição
        workoutAdapter = WorkoutAdapter(
            onClick = { workout ->
                val intent = Intent(this, AddWorkoutActivity::class.java)
                intent.putExtra("workout", workout)
                startActivity(intent)
            },
            onCompleteClick = { workout ->
                // Atualiza no banco de dados
                workoutDAO.update(workout)
                // Atualiza a lista visualmente (re-filtra)
                val diaAtual = binding.tabLayoutDays.getTabAt(binding.tabLayoutDays.selectedTabPosition)?.text.toString()
                executarFiltro(diaAtual)
            }
        )

        binding.rvwWorkouts.adapter = workoutAdapter
        binding.rvwWorkouts.layoutManager = LinearLayoutManager(this)

        // Botão para adicionar novo treino (abre a tela vazia)
        binding.fabAdicionar.setOnClickListener {
            val intent = Intent(this, AddWorkoutActivity::class.java)
            startActivity(intent)
        }

        setupTabLayout()
    }

    override fun onStart() {
        super.onStart()
        // Sempre que voltar para a tela, atualiza o filtro da aba selecionada
        val diaAtual = binding.tabLayoutDays.getTabAt(binding.tabLayoutDays.selectedTabPosition)?.text.toString()
        executarFiltro(diaAtual)
    }

    private fun setupTabLayout() {
        val diasSemana = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")
        binding.tabLayoutDays.removeAllTabs()

        diasSemana.forEach { dia ->
            binding.tabLayoutDays.addTab(binding.tabLayoutDays.newTab().setText(dia))
        }

        binding.tabLayoutDays.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                executarFiltro(tab?.text.toString())
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // Inicializa com Segunda-feira
        executarFiltro("Seg")
    }

    private fun executarFiltro(dia: String) {
        // Busca a lista completa do banco e filtra pelo dia da aba
        val todosOsWorkouts = workoutDAO.list()
        val treinosFiltrados = todosOsWorkouts.filter { it.dayOfWeek == dia }

        // Atualiza o RecyclerView através do Adapter
        workoutAdapter.updateList(treinosFiltrados)

        println("Filtrando treinos para: $dia - Encontrados: ${treinosFiltrados.size}")
    }
}