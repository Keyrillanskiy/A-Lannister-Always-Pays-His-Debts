package com.github.keyrillanskiy.city_mobil_test.presentation.ui.screens.characterlist

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.github.keyrillanskiy.city_mobil_test.R
import com.github.keyrillanskiy.city_mobil_test.data.network.CHARACTER_PAGE_SIZE
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterListActivityTest {

    @get:Rule
    var activityTestRule = ActivityTestRule(CharacterListActivity::class.java)

    @Test
    fun recyclerViewIsNotEmpty() {
        onView(withId(R.id.characterListRecyclerView)).check(matches(hasMinimumChildCount(2)))
    }

}