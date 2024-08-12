package com.example.myapplication.endToEnd

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.myapplication.MainActivity
import com.example.myapplication.screen.personList.PersonListScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class PersonListScreenTest {


    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()


    @Before
    fun setup() {
        // Inject Hilt dependencies before tests run
        hiltRule.inject()

        composeTestRule.setContent {
            PersonListScreen()
        }

    }

    @Test
    fun testAssignFoodToPerson() {

        // Wait for data to load (this depends on your implementation, might need to add idle resource if network-bound)
        composeTestRule.waitForIdle()

        // Find the first person item in the list
        composeTestRule.onNodeWithText("John Doe").assertExists()

        // Click on the MultiSelectDropdown and select a food
        composeTestRule.onNodeWithText("Select Favorite Foods").performClick()
        composeTestRule.onNodeWithText("Pizza").performClick()

        // Click the update button
        composeTestRule.onNodeWithText("Update Favorites").performClick()

        // Verify that the selected food is shown in the favorite foods list
        composeTestRule.onNodeWithText("Favorite Foods:").assertExists()
        composeTestRule.onNodeWithText("Pizza").assertExists()
    }
}