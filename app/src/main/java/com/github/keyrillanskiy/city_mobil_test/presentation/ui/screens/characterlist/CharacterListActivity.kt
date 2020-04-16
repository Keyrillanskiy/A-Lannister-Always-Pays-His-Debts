package com.github.keyrillanskiy.city_mobil_test.presentation.ui.screens.characterlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.keyrillanskiy.city_mobil_test.R
import com.github.keyrillanskiy.city_mobil_test.data.common.Response
import com.github.keyrillanskiy.city_mobil_test.domain.models.CharacterInfoResponse
import kotlinx.android.synthetic.main.activity_character_list.*

/**
 * Экран, на котором отображается список персонажей.
 * Поддерживает "пагинацию".
 */
class CharacterListActivity : AppCompatActivity() {

    private lateinit var viewModel: CharacterListViewModel
    private lateinit var viewInteractor: CharacterListViewInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_list)

        viewModel = ViewModelProvider(this).get(CharacterListViewModel::class.java)
        viewInteractor = CharacterListViewInteractor(rootView).setup {
            onLoadNewPage = { viewModel.fetchNextCharacterInfoPage() }
        }

        with(viewModel) {
            observe()
            fetchNextCharacterInfoPage()
        }
    }

    private fun CharacterListViewModel.observe() {
        characterListLiveData.observe(this@CharacterListActivity, Observer { response ->
            when (response) {
                is Response.Success -> viewInteractor.showItems(response.value.map { item -> item.mapToCharacterListItem() })
                is Response.Loading -> viewInteractor.showPageLoading()
                is Response.Failure -> handleFailure(response)
            }
        })
    }

    private fun CharacterInfoResponse.mapToCharacterListItem(): CharacterListItem {
        return CharacterListItem.CharacterInfo(
            name = name,
            gender = gender,
            culture = culture,
            born = born,
            died = died,
            titles = titles,
            aliases = aliases,
            father = father,
            mother = mother,
            spouse = spouse,
            tvSeries = tvSeries,
            playedBy = playedBy
        )
    }

    private fun handleFailure(failure: Response.Failure<*>) {
        viewInteractor.tryToShowRetryButton()

        when (failure) {
            is Response.Failure.NetworkUnavailable -> toast(R.string.network_unavailable_error)
            is Response.Failure.ConnectException -> toast(R.string.connection_error)
            is Response.Failure.RequestException -> toast(R.string.request_invalid_error)
            is Response.Failure.ServerException -> toast(R.string.server_error)
        }
    }

    private fun toast(@StringRes messageRes: Int) {
        Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show()
    }

}
