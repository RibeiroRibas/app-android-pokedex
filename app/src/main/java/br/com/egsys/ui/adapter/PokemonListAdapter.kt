package br.com.egsys.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.egsys.databinding.ItemPokemonBinding
import br.com.egsys.ui.extensions.onLoadImage
import br.com.egsys.model.Pokemon

class PokemonListAdapter(
    private val context: Context,
    private val pokemons: MutableList<Pokemon?> = mutableListOf(),
    var onPokemonClickListener: (pokemon: Pokemon) -> Unit = {}
) : RecyclerView.Adapter<PokemonListAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: ItemPokemonBinding,
        private val onPokemonClickListener: (pokemon: Pokemon) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(pokemon: Pokemon) {
            binding.itemPokemonImg.onLoadImage(pokemon.sprites.front_default)
            binding.itemPokemonName.text = pokemon.name
            itemView.setOnClickListener { onPokemonClickListener(pokemon) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ItemPokemonBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, onPokemonClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = pokemons[position]
        pokemon?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = pokemons.size

    private fun clear() {
        val size = this.pokemons.size
        this.pokemons.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun update(pokemons: MutableList<Pokemon?>) {
        clear()
        val size = this.pokemons.size
        this.pokemons.addAll(pokemons)
        notifyItemRangeInserted(size, pokemons.size)
    }
}