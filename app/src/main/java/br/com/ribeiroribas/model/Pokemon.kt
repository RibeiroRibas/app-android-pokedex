package br.com.ribeiroribas.model

import java.io.Serializable

data class Pokemon(
    val name: String,
    val height: String,
    val weight: Int,
    val types: List<PokemonType> = emptyList(),
    val sprites: PokemonSprites
) : Serializable{
    fun concatPokemonType(): String {
        var type = ""
        for (i in types.indices) {
            type = if (i == 0) {
                types[i].type.name
            } else {
                "$type / ${types[i].type.name}"
            }
        }
        return type.uppercase()
    }

    fun convertToKg(): String = "${weight.toDouble() / 10}"
    fun convertToMeters(): String = "${height.toDouble() / 10}"
}
