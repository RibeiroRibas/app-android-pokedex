package br.com.ribeiroribas.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.ribeiroribas.model.Pokemon
import br.com.ribeiroribas.model.Type
import br.com.ribeiroribas.repository.PokemonRepository
import br.com.ribeiroribas.repository.Resource

class HomeViewModel : ViewModel() {

    private val repository = PokemonRepository()
    private var mutablePokemons = MutableLiveData<Resource<MutableList<Pokemon?>>> ()
    val pokemonsLiveData:LiveData<Resource<MutableList<Pokemon?>>> =mutablePokemons

    fun getPokemonsByType(id: Int) {
       repository.getPokemonsByType(id){resource ->
           mutablePokemons.value = resource
       }
    }

    fun getPokemonByName(name: String): LiveData<Resource<Pokemon?>> {
        return repository.getPokemonByName(name)
    }

    fun getTypes(): LiveData<Resource<Type?>> {
        return repository.getTypes()
    }

    fun getRandomPokemon(): LiveData<Resource<Pokemon?>> {
       return repository.getRandomPokemon()
    }

    fun clearPokemonsLiveData() {
        mutablePokemons.value = Resource(data= mutableListOf())
    }

}