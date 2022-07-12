package br.com.egsys.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.egsys.model.Pokemon
import br.com.egsys.model.TotalPokemons
import br.com.egsys.model.Type
import br.com.egsys.model.TypePokemon
import br.com.egsys.retrofit.PokemonWebClient

class PokemonRepository(
    private val webClient: PokemonWebClient = PokemonWebClient()
) {
    private val totalPokemonsLiveData = MutableLiveData<Resource<TotalPokemons?>>()
    private val pokemonTypeLiveData = MutableLiveData<Resource<Type?>>()

    fun getPokemonByName(name: String): LiveData<Resource<Pokemon?>> {
        val pokemonLiveData = MutableLiveData<Resource<Pokemon?>>()
        webClient.getPokemonByName(
            name = name,
            onSuccess = { pokemon ->
                pokemonLiveData.value = Resource(data = pokemon)
            },
            onError = { error ->
                pokemonLiveData.value = Resource(data = null, error = error)
            }
        )
        return pokemonLiveData
    }

    fun getTypes(): LiveData<Resource<Type?>> {
        if (pokemonTypeLiveData.value == null)
            webClient.getTypes(
                onSuccess = { pokemonType ->
                    pokemonTypeLiveData.value = Resource(data = pokemonType)
                },
                onError = { error ->
                    pokemonTypeLiveData.value = Resource(data = null, error = error)
                }
            )
        return pokemonTypeLiveData
    }

    fun getPokemonsByType(
        position: Int,
        resource: (Resource<MutableList<Pokemon?>>) -> Unit,
    ) {
        webClient.getPokemonsByType(
            id = position,
            onSuccess = { pokemonsByType ->
                val pokemons = pokemonsByType?.pokemon
                pokemons?.let { getPokemonsDetails(it, resource) }
            },
            onError = { error ->
                resource(Resource(error = error, data = null))
            }
        )
    }

    private fun getPokemonsDetails(
        pokemonsByType: List<TypePokemon>,
        resource: (Resource<MutableList<Pokemon?>>) -> Unit
    ) {
        val pokemons: MutableList<Pokemon?> = mutableListOf()
        for (i in pokemonsByType.indices) {
            val pokemonName = pokemonsByType[i].pokemon.name
            getPokemonByName(pokemonName,
                onSuccess = {
                    pokemons.add(it)
                    if (pokemons.size == pokemonsByType.size)
                        resource(Resource(data = pokemons))
                }, onError = { error ->
                    resource(Resource(error = error, data = null))
                })
        }
    }

    fun getTotalPokemons(): LiveData<Resource<TotalPokemons?>> {
        if (totalPokemonsLiveData.value == null)
            webClient.getTotalPokemons(
                onSuccess = { totalPokemons ->
                    totalPokemonsLiveData.value = Resource(data = totalPokemons)
                },
                onError = { error ->
                    totalPokemonsLiveData.value = Resource(data = null, error = error)
                }
            )
        return totalPokemonsLiveData
    }

    private fun getPokemonByName(
        name: String,
        onSuccess: (pokemons: Pokemon?) -> Unit,
        onError: (error: String?) -> Unit
    ) {
        webClient.getPokemonByName(
            name = name,
            onSuccess = onSuccess,
            onError = onError
        )
    }

}