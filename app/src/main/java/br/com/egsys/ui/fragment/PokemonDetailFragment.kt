package br.com.egsys.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import br.com.egsys.databinding.FragmentDetailPokemonBinding
import br.com.egsys.model.Pokemon
import br.com.egsys.ui.activity.POKEMON
import br.com.egsys.ui.extensions.onLoadImage
import br.com.egsys.viewmodel.HomeViewModel

class PokemonDetailFragment : DialogFragment() {

    private val viewModel by lazy {
        val viewModel: HomeViewModel by activityViewModels()
        viewModel
    }

    private val binding by lazy {
        FragmentDetailPokemonBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        closeFragmentListener()
        bindPokemon()

    }

    private fun closeFragmentListener() {
        binding.fragmentDetailPokemonCloseImg.setOnClickListener { dismiss() }
    }

    private fun bindPokemon() {
        val pokemon = arguments?.getSerializable(POKEMON) as Pokemon
        binding.fragmentDetailPokemonImg.onLoadImage(pokemon.sprites.front_default)
        binding.fragmentDetailPokemonName.text = pokemon.name.uppercase()
        binding.fragmentDetailPokemonHeight.text = pokemon.convertToMeters()
        binding.fragmentDetailPokemonWeight.text = pokemon.convertToKg()
        binding.fragmentDetailPokemonType.text = pokemon.concatPokemonType()
    }
}