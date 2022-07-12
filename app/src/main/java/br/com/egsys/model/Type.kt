package br.com.egsys.model

data class Type(
    val results: List<PokemonResultType> = emptyList(),
    val pokemon: List<TypePokemon> = emptyList()
)
