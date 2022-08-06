package br.com.ribeiroribas.model

data class Type(
    val results: List<PokemonResultType> = emptyList(),
    val pokemon: List<TypePokemon> = emptyList()
)
