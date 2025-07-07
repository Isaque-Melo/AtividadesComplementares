package com.example.atividadescomplementares

import android.content.Intent
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.Spinner
import android.widget.Toast
import android.content.SharedPreferences

class CadastroActivity : AppCompatActivity() {

    companion object {
        const val PREFS_NAME = "AtividadesPrefs"
        const val KEY_ATIVIDADES = "lista_atividades" // Chave para o SharedPreferences
    }

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val edtTitulo = findViewById<EditText>(R.id.edt_titulo)
        val edtDescricao = findViewById<EditText>(R.id.edt_descricao)
        val spinnerCategoria = findViewById<Spinner>(R.id.spinner_categoria)
        val btnSalvar = findViewById<Button>(R.id.btn_salvar)

        val categorias = arrayOf("Categoria A", "Categoria B", "Categoria C")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategoria.adapter = adapter

        btnSalvar.setOnClickListener {
            val titulo = edtTitulo.text.toString()
            val descricao = edtDescricao.text.toString()
            val categoria = spinnerCategoria.selectedItem.toString()

            // Verifica se os campos não estão vazios
            if (titulo.isNotEmpty() && descricao.isNotEmpty()) {
                val atividades = sharedPref.getStringSet(KEY_ATIVIDADES, mutableSetOf())?.toMutableSet() ?: mutableSetOf()

                // Adiciona nova atividade (formato: título|descrição|categoria)
                atividades.add("$titulo|$descricao|$categoria")

                // Salva
                sharedPref.edit().putStringSet(KEY_ATIVIDADES, atividades).apply()

                Toast.makeText(this, "Título: $titulo\nDescrição: $descricao\nCategoria: $categoria", Toast.LENGTH_LONG).show()
                finish() // Volta para a tela principal
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }

        // Configura o BottomNav
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.selectedItemId = R.id.nav_cadastro // Destaca o item atual
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_cadastro -> {
                    // Já está na tela de cadastro
                    true
                }
                R.id.nav_categorias -> {
                    startActivity(Intent(this, CategoriasActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}
