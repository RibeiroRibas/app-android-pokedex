package br.com.ribeiroribas.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.com.ribeiroribas.databinding.ActivityHomeBinding
import br.com.ribeiroribas.ui.extensions.showError
import br.com.ribeiroribas.model.*
import br.com.ribeiroribas.repository.Resource
import br.com.ribeiroribas.retrofit.POKEMON_NOT_FOUND
import br.com.ribeiroribas.ui.adapter.PokemonListAdapter
import br.com.ribeiroribas.ui.fragment.PokemonDetailFragment
import br.com.ribeiroribas.viewmodel.HomeViewModel

const val POKEMON = "pokemon"
private const val EMPTY_FIELD = "Insira o nome do Pokemon para realizar a busca"

class HomeActivity : AppCompatActivity() {

    private val adapter by lazy {
        PokemonListAdapter(context = this)
    }

    private val viewModel by lazy {
        val viewModel: HomeViewModel by viewModels()
        viewModel
    }

    private val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setAdapterRecyclerView()

        // LISTENERS
        getPokemonsByTypeListener()
        onClickPokemonListener() // on click recycler view
        randomPokemonListener()
        searchPokemonByNameListener()

    }

    override fun onResume() {
        super.onResume()
        adapterPokemonTypesObserver() // AutoCompleteTextView
        pokemonsObserver()
    }

    private fun pokemonsObserver() {
        viewModel.pokemonsLiveData.observe(this) { resource ->
            resource.data?.let { pokemons ->
                binding.activityHomeProgressBar.visibility = View.GONE
                adapter.update(pokemons)
            }
            resource.error?.let { error ->
                showError(error)
            }
        }
    }

    private fun pokemonObserver(resource: Resource<Pokemon?>) {
        resource.data.let { pokemon ->
            if (pokemon == null) {
                showError(POKEMON_NOT_FOUND)
            } else {
                val pokemonDetailFragment = pokemonDetailFragment(pokemon)
                pokemonDetailFragment.show(supportFragmentManager, POKEMON)
            }
        }
        resource.error?.let { error ->
            showError(error)
        }
    }

    private fun pokemonDetailFragment(pokemon: Pokemon): PokemonDetailFragment {
        val pokemonDetailFragment = PokemonDetailFragment()
        val bundle = Bundle()
        bundle.putSerializable(POKEMON, pokemon)
        pokemonDetailFragment.arguments = bundle
        return pokemonDetailFragment
    }

    private fun searchPokemonByNameListener() {
        binding.activityHomePokemonSearchImg.setOnClickListener {
            if (isInputPokemonNameEmpty()) {
                showError(EMPTY_FIELD)
            } else {
                val pokemonName = binding.activityHomePokemonNameSearch.text.toString().lowercase()
                viewModel.getPokemonByName(pokemonName).observe(this) { resource ->
                    pokemonObserver(resource)
                }
            }
        }
    }

    private fun isInputPokemonNameEmpty() =
        binding.activityHomePokemonNameSearch.text.toString().isEmpty()

    private fun randomPokemonListener() {
        binding.activityHomeRandomPokeball.setOnClickListener {
            viewModel.getRandomPokemon().observe(this) { resource ->
                pokemonObserver(resource)
            }
        }
    }

    private fun onClickPokemonListener() {
        adapter.onPokemonClickListener = { selectedPokemon ->
            val pokemonDetailFragment = pokemonDetailFragment(selectedPokemon)
            pokemonDetailFragment.show(supportFragmentManager, POKEMON)
        }
    }

    private fun getPokemonsByTypeListener() {
        binding.activityHomeInputTypePokemon.setOnItemClickListener { _, _, position, _ ->
            viewModel.clearPokemonsLiveData()
            binding.activityHomeProgressBar.visibility = View.VISIBLE
            val pokemonType = getTypePokemonNameByPositionAdapter(position)
            getPokemonsByType(pokemonType)
        }
    }

    private fun getPokemonsByType(pokemonType: Int) {
        viewModel.getPokemonsByType(pokemonType)
    }

    private fun getTypePokemonNameByPositionAdapter(position: Int) = position + 1

    private fun setAdapterRecyclerView() {
        binding.activityHomeRecyclerView.adapter = adapter
    }

    private fun adapterPokemonTypesObserver() {
        viewModel.getTypes().observe(this) { resource ->
            resource.data?.let { types ->
                val pokemonTypes = getPokemonTypes(types)
                setAdapterPokemonTypes(pokemonTypes)
            }
            resource.error?.let { error ->
                showError(error)
            }
        }
    }

    private fun setAdapterPokemonTypes(pokemonTypes: List<String>?) {
        pokemonTypes?.let {
            val adapterItems = ArrayAdapter(this, android.R.layout.simple_list_item_1, it)
            binding.activityHomeInputTypePokemon.setAdapter(adapterItems)
        }
    }

    private fun getPokemonTypes(types: Type): List<String>? {
        return types.results.map { pokemonTypes ->
            pokemonTypes.name
        }
    }
}