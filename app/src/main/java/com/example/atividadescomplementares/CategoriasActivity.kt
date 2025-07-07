package com.example.atividadescomplementares

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.atividadescomplementares.CategoriaAdapter
import com.example.atividadescomplementares.CadastroActivity
import com.example.atividadescomplementares.MainActivity
import com.example.atividadescomplementares.R
import android.content.SharedPreferences

class CategoriasActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categorias)

        // Inicializa SharedPreferences
        sharedPref = getSharedPreferences(CadastroActivity.PREFS_NAME, Context.MODE_PRIVATE)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_categorias)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Recupera as atividades do SharedPreferences
        val atividadesSet = sharedPref.getStringSet(CadastroActivity.KEY_ATIVIDADES, setOf()) ?: setOf()

        // Contar atividades por categoria
        val contagemCategorias = mutableMapOf<String, Int>()
        for (atividadeStr in atividadesSet) {
            val partes = atividadeStr.split("|")
            if (partes.size >= 3) {
                val categoria = partes[2] // A terceira parte é a categoria
                contagemCategorias[categoria] = contagemCategorias.getOrDefault(categoria, 0) + 1
            }
        }

        // Criar uma lista de categorias com contagem
        val categorias = contagemCategorias.map { (categoria, count) ->
            "$categoria - $count atividade${if (count > 1) "s" else ""}" // Corrigido para plural
        }

        // Configura o adapter com as categorias
        recyclerView.adapter = CategoriaAdapter(categorias)

        // Configura o BottomNav
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.selectedItemId = R.id.nav_categorias // Destaca o item atual

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
                R.id.nav_categorias -> true // Já está na tela de categorias
                else -> false
            }
        }
    }
}
