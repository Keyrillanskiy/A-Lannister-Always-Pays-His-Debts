package com.github.keyrillanskiy.city_mobil_test.presentation.screens.characterinfo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.github.keyrillanskiy.city_mobil_test.R
import com.github.keyrillanskiy.city_mobil_test.presentation.screens.characterlist.CharacterListItem
import kotlinx.android.synthetic.main.activity_character_info.*

/**
 * Экран для просмотра информации о персонаже.
 */
class CharacterInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_info)

        val characterInfo: CharacterListItem.CharacterInfo = intent.extras?.getParcelable(KEY_CHARACTER_INFO)
            ?: throw IllegalArgumentException("Character info is null")

        characterInfoToolbar.setNavigationOnClickListener { finish() }

        showCharacterInfo(characterInfo)
    }

    private fun showCharacterInfo(characterInfo: CharacterListItem.CharacterInfo) {
        fun textView(): TextView {
            return TextView(this).apply { textSize = 24f }
        }

        fun addInfo(@StringRes description: Int, content: String) {
            if (content.isBlank()) {
                return
            }
            val info = "${getString(description)} : $content"
            characterInfoContainerLinearLayout.addView(textView().apply { text = info })
        }

        fun addInfoList(@StringRes description: Int, content: List<String>) {
            if (content.firstOrNull().isNullOrBlank()) {
                return
            }
            val info = buildString {
                append(getString(description))
                append(" : ")
                content.forEachIndexed { i, item ->
                    if(item.isNotBlank()) {
                        append(item)
                        if(i < content.count() - 1) {
                            append(", ")
                        }
                    }
                }
            }
            characterInfoContainerLinearLayout.addView(textView().apply { text = info })
        }

        val title = characterInfo.name.takeIf { it.isNotEmpty() }
            ?: characterInfo.aliases.firstOrNull()
            ?: throw IllegalArgumentException("No available name for character")
        characterInfoToolbar.title = title

        with(characterInfo) {
            addInfo(R.string.name_field, name)
            addInfo(R.string.gender_field, gender)
            addInfo(R.string.culture_field, culture)
            addInfo(R.string.born_field, born)
            addInfo(R.string.died_field, died)
            addInfoList(R.string.titles_field, titles)
            addInfoList(R.string.aliases_field, aliases)
            addInfo(R.string.father_field, father)
            addInfo(R.string.mother_field, mother)
            addInfoList(R.string.tv_series_field, tvSeries)
            addInfoList(R.string.played_by_field, playedBy)
        }
    }

    companion object {
        const val KEY_CHARACTER_INFO = "com.github.keyrillanskiy.character_info"

        fun launch(context: Context, characterInfo: CharacterListItem.CharacterInfo) {
            val intent = Intent(context, CharacterInfoActivity::class.java).apply {
                putExtra(KEY_CHARACTER_INFO, characterInfo)
            }
            context.startActivity(intent)
        }
    }

}