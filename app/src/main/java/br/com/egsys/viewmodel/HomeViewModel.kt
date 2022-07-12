package br.com.egsys.viewmodel

import android.opengl.Visibility
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.egsys.model.Pokemon
import br.com.egsys.model.TotalPokemons
import br.com.egsys.model.Type
import br.com.egsys.repository.PokemonRepository
import br.com.egsys.repository.Resource

class HomeViewModel : ViewModel() {

    private val repository = PokemonRepository()
    private var mutableSelectedPokemon = MutableLiveData<Pokemon?>()
    val selectedPokemon: LiveData<Pokemon?> get() = mutableSelectedPokemon
    private var mutablePokemonList = MutableLiveData<Resource<MutableList<Pokemon?>>>()
    val getPokemons: LiveData<Resource<MutableList<Pokemon?>>> get() = mutablePokemonList

    fun selectedPokemon(pokemon: Pokemon) {
        mutableSelectedPokemon = MutableLiveData<Pokemon?>()
        mutableSelectedPokemon.value = pokemon
    }

    private fun addPokemons(pokemons: Resource<MutableList<Pokemon?>>) {
        mutablePokemonList.value = pokemons
    }

    fun getPokemonsByType(id: Int) {
        repository.getPokemonsByType(id) {pokemons->
            addPokemons(pokemons)
        }
    }

    fun getPokemonByName(name: String): LiveData<Resource<Pokemon?>> {
        return repository.getPokemonByName(name)
    }

    fun getTypes(): LiveData<Resource<Type?>> {
        return repository.getTypes()
    }

    fun getTotalPokemons(): LiveData<Resource<TotalPokemons?>> {
        return repository.getTotalPokemons()
    }

    fun clearPokemonListByType() {
        mutablePokemonList.value = Resource(data = mutableListOf())
    }

}