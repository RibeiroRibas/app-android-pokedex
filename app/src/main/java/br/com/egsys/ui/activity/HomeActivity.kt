package br.com.egsys.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.com.egsys.databinding.ActivityHomeBinding
import br.com.egsys.ui.extensions.showError
import br.com.egsys.model.*
import br.com.egsys.retrofit.POKEMON_NOT_FOUND
import br.com.egsys.ui.adapter.PokemonListAdapter
import br.com.egsys.ui.fragment.PokemonDetailFragment
import br.com.egsys.viewmodel.HomeViewModel

private const val TAG_POKEMON = "pokemon"
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
        pokemonListObserver()
    }

    private fun searchPokemonByNameListener() {
        binding.activityHomePokemonSearchImg.setOnClickListener {
            if (isInputPokemonNameEmpty()) {
                showError(EMPTY_FIELD)
            } else {
                val pokemonName = binding.activityHomePokemonNameSearch.text.toString().lowercase()
                getPokemonByNameObserver(pokemonName)
            }
        }
    }

    private fun isInputPokemonNameEmpty() =
        binding.activityHomePokemonNameSearch.text.toString().isEmpty()

    private fun pokemonListObserver() {
        viewModel.getPokemons.observe(this) { resource ->
            resource?.data.let { pokemons ->
                binding.activityHomeProgressBar.visibility = View.GONE
                pokemons?.let { adapter.update(it) }
            }
            resource?.error.let { error ->
                error?.let { errorMessage -> showError(errorMessage) }
            }
        }
    }

    private fun randomPokemonListener() {
        binding.activityHomeRandomPokeball.setOnClickListener {
            viewModel.getTotalPokemons().observe(this, { resource ->
                resource?.data.let { totalPokemons ->
                    val pokemon = totalPokemons?.let { pokemons ->
                        sortRandomPokemon(pokemons.results)
                    }
                    pokemon?.let { getPokemonByNameObserver(it.name) }
                }
                resource?.error.let { error ->
                    error?.let { errorMessage -> showError(errorMessage) }
                }
            })
        }
    }

    private fun sortRandomPokemon(totalPokemons: List<NamedAPIResource>): NamedAPIResource {
        val diceRange = 1..totalPokemons.size
        return totalPokemons[diceRange.random()]
    }

    private fun getPokemonByNameObserver(pokemonName: String) {
        viewModel.getPokemonByName(pokemonName).observe(this) { resource ->
            resource?.data.let { pokemon ->
                if(pokemon == null){
                    showError(POKEMON_NOT_FOUND)
                }else{
                    viewModel.selectedPokemon(pokemon)
                    val pokemonDetailFragment = PokemonDetailFragment()
                    pokemonDetailFragment.show(supportFragmentManager, TAG_POKEMON)
                }
            }
        }
    }

    private fun onClickPokemonListener() {
        adapter.onPokemonClickListener = { selectedPokemon ->
            val pokemonDetailFragment = PokemonDetailFragment()
            pokemonDetailFragment.show(supportFragmentManager, TAG_POKEMON)
            viewModel.selectedPokemon(selectedPokemon)
        }
    }

    private fun getPokemonsByTypeListener() {
        binding.activityHomeInputTypePokemon.setOnItemClickListener { _, _, position, _ ->
            viewModel.clearPokemonListByType()
            binding.activityHomeProgressBar.visibility = View.VISIBLE
            val pokemonType = getTypePokemonNameByPositionAdapter(position)
            viewModel.getPokemonsByType(pokemonType) // observer inside onResume
        }
    }

    private fun getTypePokemonNameByPositionAdapter(position: Int) = position + 1

    private fun setAdapterRecyclerView() {
        binding.activityHomeRecyclerView.adapter = adapter
    }

    private fun adapterPokemonTypesObserver() {
        viewModel.getTypes().observe(this) { resource ->
            resource?.data.let { types ->
                val pokemonTypes = getPokemonTypes(types)
                setAdapterPokemonTypes(pokemonTypes)
            }
            resource?.error.let { error ->
                error?.let { errorMessage -> showError(errorMessage) }
            }
        }
    }

    private fun setAdapterPokemonTypes(pokemonTypes: List<String>?) {
        pokemonTypes?.let {
            val adapterItems = ArrayAdapter(this, android.R.layout.simple_list_item_1, it)
            binding.activityHomeInputTypePokemon.setAdapter(adapterItems)
        }
    }

    private fun getPokemonTypes(types: Type?): List<String>? {
        val results = types?.results
        return results?.map { pokemonTypes ->
            pokemonTypes.name
        }
    }
}