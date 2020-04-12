package com.github.keyrillanskiy.city_mobil_test.presentation.ui.screens.characterlist

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.keyrillanskiy.city_mobil_test.domain.models.CharacterInfoResponse
import kotlinx.android.synthetic.main.item_character.view.*

class CharacterListAdapter : RecyclerView.Adapter<CharacterListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterListViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: CharacterListViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}

sealed class CharacterListViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {

    class CharacterInfoViewHolder(private val rootView: View) : CharacterListViewHolder(rootView) {
        fun bindData(data: CharacterInfoResponse) {
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