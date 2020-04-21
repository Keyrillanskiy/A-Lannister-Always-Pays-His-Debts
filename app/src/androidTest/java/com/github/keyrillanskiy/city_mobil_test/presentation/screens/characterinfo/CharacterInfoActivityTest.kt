package com.github.keyrillanskiy.city_mobil_test.presentation.screens.characterinfo

import android.content.Intent
import android.widget.ImageButton
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.github.keyrillanskiy.city_mobil_test.R
import com.github.keyrillanskiy.city_mobil_test.presentation.screens.characterlist.CharacterListItem
import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterInfoActivityTest {

    @get:Rule
    var activityTestRule = ActivityTestRule(CharacterInfoActivity::class.java, true, false)
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun onlyNameShowed() {
        val name = "Name"
        val characterInfoWithOnlyName = characterInfoStub(name = name)
        val intent = startActivityIntent(characterInfoWithOnlyName)
        activityTestRule.launchActivity(intent)

        onView(withId(R.id.characterInfoToolbar)).check(matches(hasDescendant(withText(name))))

        val nameField = context.getString(R.string.name_field)
        val nameFieldAndValue = "$nameField : $name"
        onView(withId(R.id.characterInfoContainerLinearLayout)).check(matches(hasDescendant(withText(nameFieldAndValue))))
    }

    @Test
    fun listDataShowedCorrectly() {
        val text = "Name"
        val emptyCharacterInfo = characterInfoStub(name = text, aliases = listOf(text, text, text))
        val intent = startActivityIntent(emptyCharacterInfo)
        activityTestRule.launchActivity(intent)

        val nameField = context.getString(R.string.name_field)
        val nameFieldAndValue = "$nameField : $text"

        val aliasesField = context.getString(R.string.aliases_field)
        val aliasesFieldAndValues = "$aliasesField : $text, $text, $text"

        onView(withId(R.id.characterInfoContainerLinearLayout))
            .check(matches(hasDescendant(withText(nameFieldAndValue))))
            .check(matches(hasDescendant(withText(aliasesFieldAndValues))))
    }

    @Test
    fun toolbarBackButtonWorksCorrectly() {
        val characterInfoWithOnlyName = characterInfoStub(name = "Name")
        val intent = startActivityIntent(characterInfoWithOnlyName)
        activityTestRule.launchActivity(intent)

        onView(allOf(instanceOf(ImageButton::class.java), withParent(withId(R.id.characterInfoToolbar))))
            .perform(click())

        assertTrue(activityTestRule.activity.isFinishing)
    }

    private fun startActivityIntent(characterInfo: CharacterListItem.CharacterInfo): Intent {
        return Intent(context, CharacterInfoActivity::class.java).apply {
            putExtra(CharacterInfoActivity.KEY_CHARACTER_INFO, characterInfo)
        }
    }

    private fun characterInfoStub(
        name: String? = null,
        gender: String? = null,
        culture: String? = null,
        born: String? = null,
        died: String? = null,
        titles: List<String>? = null,
        aliases: List<String>? = null,
        father: String? = null,
        mother: String? = null,
        tvSeries: List<String>? = null,
        playedBy: List<String>? = null
    ): CharacterListItem.CharacterInfo {
        return CharacterListItem.CharacterInfo(
            name = name ?: "",
            gender = gender ?: "",
            culture = culture ?: "",
            born = born ?: "",
            died = died ?: "",
            titles = titles ?: listOf(),
            aliases = aliases ?: listOf(),
            father = father ?: "",
            mother = mother ?: "",
            tvSeries = tvSeries ?: listOf(),
            playedBy = playedBy ?: listOf()
        )
    }

}