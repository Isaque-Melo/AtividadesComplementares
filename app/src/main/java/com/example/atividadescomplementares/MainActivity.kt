package com.example.atividadescomplementares

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.example.atividadescomplementares.adapters.AtividadesAdapter
import androidx.appcompat.widget.Toolbar
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView


class MainActivity : AppCompatActivity() {
    companion object {
        private const val PREFS_NAME = "AtividadesPrefs"
        private const val KEY_ATIVIDADES = "lista_atividades"
    }

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var recyclerView: RecyclerView

    private lateinit var sharedPref: SharedPreferences
    private lateinit var adapter: AtividadesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        // Configuração do Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toobal: Toolbar = findViewById(R.id.toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toobal,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val headerView = navView.getHeaderView(0)
        val nomeUsuario = headerView.findViewById<TextView>(R.id.txt_nome)
        val cargoUsuario = headerView.findViewById<TextView>(R.id.txt_cargo)
        val fotoPerfil = headerView.findViewById<CircleImageView>(R.id.profile_image)

        nomeUsuario.text = "Isaque Melo"
        cargoUsuario.text = "Programador Senior"
        fotoPerfil.setImageResource(R.drawable.tralalero)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_login -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    true
                }
                R.id.nav_sobre -> {
                    startActivity(Intent(this, SobreActivity::class.java))
                    true
                }
                else -> false
            }
            drawerLayout.closeDrawers()
            true
        }





        // Configuração do Bottom Navigation
        bottomNav = findViewById(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_home -> {
                    // Já estamos na Home
                    true
                }
                R.id.nav_cadastro -> {
                    startActivity(Intent(this, CadastroActivity::class.java))
                    true
                }
                R.id.nav_categorias -> {
                    startActivity(Intent(this, CategoriasActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Configuração do RecyclerView
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        atualizarListaAtividades()
    }

    private fun atualizarListaAtividades() {
        // Recupera as atividades do SharedPreferences
        val atividadesSet = sharedPref.getStringSet(KEY_ATIVIDADES, setOf()) ?: setOf()

        val atividadesList = atividadesSet.map { atividadeStr ->
            val partes = atividadeStr.split("|")
            Atividade(
                titulo = partes.getOrElse(0) { "Sem título" },
                descricao = partes.getOrElse(1) { "Sem descrição" },
                categoria = partes.getOrElse(2) { "Sem categoria" },
                status = partes.getOrNull(3)?.toBoolean() ?: false // Mudança para booleano
            )
        }.toMutableList() // Convertendo para MutableList para permitir alterações

        // Atualiza o RecyclerView
        adapter = AtividadesAdapter(atividadesList) { position, isChecked ->
            atividadesList[position].status = isChecked
            salvarStatusNoSharedPref(atividadesList) // Salva o status atualizado
        }
        recyclerView.adapter = adapter
    }

    private fun salvarStatusNoSharedPref(atividades: List<Atividade>) {
        val atividadesString = atividades.map {
            "${it.titulo}|${it.descricao}|${it.categoria}|${it.status}"
        }.toSet()

        sharedPref.edit().putStringSet(KEY_ATIVIDADES, atividadesString).apply()
    }

    override fun onResume() {
        super.onResume()
        atualizarListaAtividades() // Atualiza sempre que retornar à tela
    }
}
