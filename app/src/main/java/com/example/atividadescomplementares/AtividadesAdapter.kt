package com.example.atividadescomplementares.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.atividadescomplementares.R
import com.example.atividadescomplementares.Atividade

class AtividadesAdapter(
    private val atividades: MutableList<Atividade>,
    private val onStatusChanged: (Int, Boolean) -> Unit,
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<AtividadesAdapter.AtividadeViewHolder>() {

    inner class AtividadeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.tv_titulo)
        val descricao: TextView = itemView.findViewById(R.id.tv_descricao)
        val categoria: TextView = itemView.findViewById(R.id.tv_categoria)
        val status: CheckBox = itemView.findViewById(R.id.cb_realizada)
        val tv_status: TextView = itemView.findViewById(R.id.tv_status)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClicked(position)
                }
            }
        }

        fun bind(atividade: Atividade) {
            titulo.text = atividade.titulo
            descricao.text = atividade.descricao
            categoria.text = atividade.categoria
            status.isChecked = atividade.status // Atualizando o estado do CheckBox


            // Listener para quando o CheckBox é alterado
            status.setOnCheckedChangeListener { _, isChecked ->
                onStatusChanged(adapterPosition, isChecked)
                tv_status.visibility = if (atividade.status) View.GONE else View.VISIBLE
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AtividadeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_atividade, parent, false)
        return AtividadeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AtividadeViewHolder, position: Int) {
        val atividade = atividades[position]
        holder.bind(atividade)
    }

    override fun getItemCount() = atividades.size
}
