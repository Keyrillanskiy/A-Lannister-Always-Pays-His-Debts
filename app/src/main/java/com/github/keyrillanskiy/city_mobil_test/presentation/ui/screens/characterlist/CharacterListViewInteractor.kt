package com.github.keyrillanskiy.city_mobil_test.presentation.ui.screens.characterlist

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.keyrillanskiy.city_mobil_test.presentation.ui.common.PagingRecyclerScrollListener
import kotlinx.android.synthetic.main.activity_character_list.view.*

/**
 * Инкапсулирует взаимодействие с [View] на экране [CharacterListActivity].
 */
class CharacterListViewInteractor(private val rootView: View) {

    var onLoadNewPage: (() -> Unit)? = null
    private val listAdapter = CharacterListAdapter()
    private val linearLayoutManager = LinearLayoutManager(rootView.context)
    private val pagingScrollListener = PagingRecyclerScrollListener(linearLayoutManager, onFetchNewItems = {
        onLoadNewPage?.invoke()
    })

    fun setup(init: CharacterListViewInteractor.() -> Unit): CharacterListViewInteractor {
        init.invoke(this)

        with(rootView) {
            characterListRecyclerView.apply {
                adapter = listAdapter
                layoutManager = linearLayoutManager
                addOnScrollListener(pagingScrollListener)
            }

            characterListRetryButton.setOnClickListener { onLoadNewPage?.invoke() }
        }

        return this
    }

    fun showItems(newItems: List<CharacterListItem>) {
        with(rootView) {
            characterListRetryButton.visibility = View.GONE
            pagingScrollListener.isLoading = false
            pagingScrollListener.isLastPage = newItems.isEmpty()
            listAdapter.insertItems(newItems)
        }
    }

    fun showPageLoading() {
        pagingScrollListener.isLoading = true
        listAdapter.showLoading()
    }

    fun tryToShowRetryButton() {
        if (listAdapter.itemCount == 0) {
            rootView.characterListRetryButton.visibility = View.VISIBLE
        }
    }

}