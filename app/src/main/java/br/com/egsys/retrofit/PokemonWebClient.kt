package br.com.egsys.retrofit

import br.com.egsys.model.Pokemon
import br.com.egsys.model.TotalPokemons
import br.com.egsys.model.Type
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val REQUEST_FAILURE = "Problemas no Servidor"
const val POKEMON_NOT_FOUND = "Nenhum pokémom Encontrado"


class PokemonWebClient(
    private val service: PokemonService = AppRetrofit().pokemonService
) {

    private fun <T> onRequest(
        call: Call<T>,
        onSuccess: (pokemons: T?) -> Unit,
        onError: (error: String?) -> Unit
    ) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    onSuccess(response.body())
                } else {
                    onError (checkResponseCode(response))
                }
            }
            override fun onFailure(call: Call<T>, t: Throwable) {
                onError(t.message)
            }
        })
    }

    private fun <T> checkResponseCode(response: Response<T>): String {
        if (response.code() == 404) {
            return POKEMON_NOT_FOUND
        }
        return REQUEST_FAILURE
    }

    fun getPokemonByName(
        name: String,
        onSuccess: (pokemons: Pokemon?) -> Unit,
        onError: (error: String?) -> Unit
    ) {
        onRequest(
            service.getPokemonByName(name),
            onSuccess,
            onError
        )
    }

    fun getTypes(
        onSuccess: (results: Type?) -> Unit,
        onError: (error: String?) -> Unit
    ) {
        onRequest(
            service.getTypes(),
            onSuccess,
            onError
        )
    }

    fun getPokemonsByType(
        id: Int,
        onSuccess: (Type?) -> Unit,
        onError: (error: String?) -> Unit
    ) {
        onRequest(
            service.getPokemonsByType(id),
            onSuccess,
            onError
        )
    }

    fun getTotalPokemons(
        onSuccess: (TotalPokemons?) -> Unit,
        onError: (error: String?) -> Unit
    ) {
        onRequest(
            service.getTotalPokemons(),
            onSuccess,
            onError
        )
    }

}