package com.example.atividadescomplementares

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
// MODIFICADO: O import do SearchView agora pode ser o do widget padrão
import android.widget.SearchView
import com.google.android.material.bottomnavigation.BottomNavigationView

class CategoriasActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var adapter: CategoriaAdapter
    // MODIFICADO: Não precisa mais da variável searchView a nível de classe, mas pode manter se preferir
    // private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categorias)

        // Configuração da Toolbar (continua igual)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Categorias"

        // Inicializa SharedPreferences
        sharedPref = getSharedPreferences(CadastroActivity.PREFS_NAME, Context.MODE_PRIVATE)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_categorias)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Recupera e processa as atividades (continua igual)
        val atividadesSet = sharedPref.getStringSet(CadastroActivity.KEY_ATIVIDADES, setOf()) ?: setOf()
        val contagemCategorias = mutableMapOf<String, Int>()
        for (atividadeStr in atividadesSet) {
            val partes = atividadeStr.split("|")
            if (partes.size >= 3) {
                val categoria = partes[2]
                contagemCategorias[categoria] = contagemCategorias.getOrDefault(categoria, 0) + 1
            }
        }
        val categorias = contagemCategorias.map { (categoria, count) ->
            "$categoria - $count atividade${if (count > 1) "s" else ""}"
        }

        // Configura o adapter (continua igual)
        adapter = CategoriaAdapter(categorias.toMutableList())
        recyclerView.adapter = adapter

        // --- NOVA LÓGICA DA SEARCHVIEW ---
        // 1. Encontra a SearchView pelo ID do seu XML
        val searchView = findViewById<SearchView>(R.id.search_view)

        // 2. Configura o listener para reagir à digitação
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // Chamado quando o usuário pressiona "enter" (não precisamos usar)
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            // Chamado a cada letra digitada ou apagada
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText) // Chama o filtro do adapter
                return true
            }
        })
        // --- FIM DA NOVA LÓGICA ---


        // Configura o BottomNav (continua igual)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.selectedItemId = R.id.nav_categorias

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_cadastro -> {
                    startActivity(Intent(this, CadastroActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_categorias -> true
                else -> false
            }
        }
    }

    // REMOVIDO: Os métodos onCreateOptionsMenu e onBackPressed não são mais necessários
    // para esta abordagem.
}