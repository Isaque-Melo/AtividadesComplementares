package com.example.atividadescomplementares

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable // IMPORTANTE: Importar a interface
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

// MODIFICADO: Adiciona ", Filterable" para implementar a interface.
// A lista também precisa ser mutável (var e MutableList).
class CategoriaAdapter(private var categorias: MutableList<String>) :
    RecyclerView.Adapter<CategoriaAdapter.ViewHolder>(), Filterable {

    // ADICIONADO: Uma cópia da lista original para não perder os dados ao filtrar.
    private var categoriasListCompleta: List<String> = ArrayList(categorias)

    // Seu ViewHolder continua exatamente igual.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val texto: TextView = itemView.findViewById(R.id.tv_categoria)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categoria, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.texto.text = categorias[position]
    }

    override fun getItemCount() = categorias.size

    // ADICIONADO: Esta é a função que faltava!
    // Ela fornece a propriedade ".filter" para a sua activity.
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val listaFiltrada = mutableListOf<String>()
                if (constraint.isNullOrEmpty()) {
                    listaFiltrada.addAll(categoriasListCompleta)
                } else {
                    val filterPattern = constraint.toString().lowercase(Locale.ROOT).trim()
                    for (item in categoriasListCompleta) {
                        if (item.lowercase(Locale.ROOT).contains(filterPattern)) {
                            listaFiltrada.add(item)
                        }
                    }
                }
                val results = FilterResults()
                results.values = listaFiltrada
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                categorias.clear()
                categorias.addAll(results?.values as List<String>)
                notifyDataSetChanged()
            }
        }
    }
}