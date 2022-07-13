package br.com.egsys.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.egsys.model.NamedAPIResource
import br.com.egsys.model.Pokemon
import br.com.egsys.model.Type
import br.com.egsys.model.TypePokemon
import br.com.egsys.retrofit.PokemonWebClient

class PokemonRepository(
    private val webClient: PokemonWebClient = PokemonWebClient()
) {
    private val pokemonTypeLiveData = MutableLiveData<Resource<Type?>>()

    fun getPokemonByName(name: String): LiveData<Resource<Pokemon?>> {
        val mutablePokemon = MutableLiveData<Resource<Pokemon?>>()
        webClient.getPokemonByName(
            name = name,
            onSuccess = { pokemon ->
                mutablePokemon.value = Resource(data = pokemon)
            },
            onError = { error ->
                mutablePokemon.value = Resource(data = null, error = error)
            }
        )
        return mutablePokemon
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
        resource: (Resource<MutableList<Pokemon?>>) -> Unit
    ) {
        webClient.getPokemonsByType(
            id = position,
            onSuccess = { pokemonsByType ->
                pokemonsByType?.pokemon?.let { pokemons ->
                    getPokemonsDetails(pokemons, resource)
                }
            },
            onError = { error ->
                Resource(error = error, data = null)
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

    fun getRandomPokemon(): LiveData<Resource<Pokemon?>> {
        val mutablePokemon = MutableLiveData<Resource<Pokemon?>>()
        webClient.getTotalPokemons(
            onSuccess = { totalPokemons ->
                val randomPokemon = totalPokemons?.let { pokemons ->
                    sortRandomPokemon(pokemons.results)
                }
                randomPokemon?.name?.let { pokemonName ->
                    getPokemonByName(pokemonName,
                        onSuccess = { pokemon ->
                            mutablePokemon.value = Resource(data = pokemon)
                        }, onError = { error ->
                            mutablePokemon.value = Resource(data = null, error = error)
                        })
                }
            },
            onError = { error ->
                mutablePokemon.value = Resource(data = null, error = error)
            }
        )
        return mutablePokemon
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

    private fun sortRandomPokemon(totalPokemons: List<NamedAPIResource>): NamedAPIResource {
        val diceRange = 1..totalPokemons.size
        return totalPokemons[diceRange.random()]
    }

}