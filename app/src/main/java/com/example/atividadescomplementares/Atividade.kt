package com.example.atividadescomplementares

data class Atividade(
    val titulo: String,
    val descricao: String,
    val categoria: String,
    var status: Boolean = false
)
