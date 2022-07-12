package br.com.egsys.retrofit

import br.com.egsys.model.Pokemon
import br.com.egsys.model.TotalPokemons
import br.com.egsys.model.Type
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url


interface PokemonService {

    @GET("pokemon/{name}")
    fun getPokemonByName(@Path("name") name: String): Call<Pokemon>

    @GET("type")
    fun getTypes(): Call<Type>

    @GET("type/{id}")
    fun getPokemonsByType(@Path("id") id: Int): Call<Type>

    @GET("pokemon?limit=1154")
    fun getTotalPokemons(): Call<TotalPokemons>

}