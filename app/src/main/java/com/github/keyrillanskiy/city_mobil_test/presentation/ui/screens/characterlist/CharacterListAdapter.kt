package com.github.keyrillanskiy.city_mobil_test.presentation.ui.screens.characterlist

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.keyrillanskiy.city_mobil_test.R
import com.github.keyrillanskiy.city_mobil_test.presentation.ui.screens.characterlist.CharacterListItem.CharacterInfo
import com.github.keyrillanskiy.city_mobil_test.presentation.ui.screens.characterlist.CharacterListItem.CharacterPageLoadingItem
import com.github.keyrillanskiy.city_mobil_test.presentation.ui.screens.characterlist.CharacterListViewHolder.CharacterInfoViewHolder
import com.github.keyrillanskiy.city_mobil_test.presentation.ui.screens.characterlist.CharacterListViewHolder.CharacterPageLoadingViewHolder
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.item_character.view.*

/**
 * Адаптер списка персонажей. Поддерживает "пагинацию".
 */
class CharacterListAdapter(private val items: MutableList<CharacterListItem> = mutableListOf()) :
    RecyclerView.Adapter<CharacterListViewHolder>() {

    private var isLoadingItems = false
    var onItemClick: ((CharacterInfo) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is CharacterInfo -> ITEM_CHARACTER_INFO
            CharacterPageLoadingItem -> ITEM_PAGE_LOADING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM_CHARACTER_INFO -> {
                val itemView = inflater.inflate(R.layout.item_character, parent, false)
                CharacterInfoViewHolder(itemView)
            }
            ITEM_PAGE_LOADING -> {
                val itemView = inflater.inflate(R.layout.item_character_page_loading, parent, false)
                CharacterPageLoadingViewHolder(itemView)
            }
            else -> throw IllegalArgumentException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: CharacterListViewHolder, position: Int) {
        val item = items[position]
        when {
            holder is CharacterInfoViewHolder && item is CharacterInfo -> {
                holder.bindData(item)
                holder.itemView.setOnClickListener { onItemClick?.invoke(item) }
            }
            holder is CharacterPageLoadingViewHolder && item is CharacterPageLoadingItem -> {
                /*nothing*/
            }
        }
    }

    override fun getItemCount(): Int = items.count()

    private fun lastPosition() = items.count() - 1

    fun insertItems(newItems: List<CharacterListItem>) {
        if (isLoadingItems) {
            hideLoading()
        }
        val insertPosition = items.count()
        items.addAll(newItems)
        notifyItemRangeInserted(insertPosition, newItems.count())
    }

    fun showLoading() {
        isLoadingItems = true
        items.add(CharacterPageLoadingItem)
        notifyItemInserted(lastPosition())
    }

    fun hideLoading() {
        isLoadingItems = false
        val lastPosition = lastPosition()
        items.removeAt(lastPosition)
        notifyItemRemoved(lastPosition)
    }

    companion object {
        private const val ITEM_CHARACTER_INFO = 1
        private const val ITEM_PAGE_LOADING = 2
    }

}

sealed class CharacterListViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {

    class CharacterInfoViewHolder(private val rootView: View) : CharacterListViewHolder(rootView) {
        fun bindData(data: CharacterInfo) {
            with(rootView) {
                val info = data.name.takeIf { it.isNotEmpty() }
                    ?: data.aliases.firstOrNull()
                    ?: throw IllegalArgumentException("No available name for character")
                characterInfoTextView.text = info
            }
        }
    }

    class CharacterPageLoadingViewHolder(rootView: View) : CharacterListViewHolder(rootView)

}

sealed class CharacterListItem {
    @Parcelize
    data class CharacterInfo(
        val name: String,
        val gender: String,
        val culture: String,
        val born: String,
        val died: String,
        val titles: List<String>,
        val aliases: List<String>,
        val father: String,
        val mother: String,
        val tvSeries: List<String>,
        val playedBy: List<String>
    ) : CharacterListItem(), Parcelable

    object CharacterPageLoadingItem : CharacterListItem()
}