package com.github.keyrillanskiy.city_mobil_test.presentation.common

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.keyrillanskiy.city_mobil_test.data.network.CHARACTER_PAGE_SIZE

/**
 * [RecyclerView.OnScrollListener] для поддержки пагинации.
 * Вызывает [onFetchNewItems] для загрузки данных при скролле до конца списка.
 */
class PagingRecyclerScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val onFetchNewItems: () -> Unit
) : RecyclerView.OnScrollListener() {

    var isLoading = false
    var isLastPage = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                && firstVisibleItemPosition >= 0
                && totalItemCount >= CHARACTER_PAGE_SIZE
            ) {
                onFetchNewItems.invoke()
            }
        }
    }

}