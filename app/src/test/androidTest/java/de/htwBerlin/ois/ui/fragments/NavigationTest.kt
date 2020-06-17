package de.htwBerlin.ois.ui.fragments

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import de.htwBerlin.ois.R
import de.htwBerlin.ois.ui.mainActivity.MainActivity
import org.junit.Test
import org.junit.runner.RunWith

/**
 *----- This class DOES NOT TEST THE FragmentNavigation----
 * It tests the navigation in between all fragments of the app
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class NavigationTest {

    @Test
    fun testAppFragmentNavigation() {
        //Setup
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        //The FragmentHome should be on the screen
        onView(withId(R.id.fragment_home_parent)).check(matches(isDisplayed()))
        //Navigation to the download tab via the Bottom Navigation --the FragmentMapDownloadCenterAll should be displayed
        onView(withId(R.id.nav_download)).perform(click())
        onView(withId(R.id.fragment_map_download_all_parent)).check(matches(isDisplayed()))
        //Pressing the all maps button should'nt open another fragment because we are already in it
        onView(withId(R.id.nav_download)).perform(click())
        onView(withId(R.id.fragment_map_download_all_parent)).check(matches(isDisplayed()))
        //same for the all_maps_button
        onView(withId(R.id.button_all_maps)).perform(click())
        onView(withId(R.id.fragment_map_download_all_parent)).check(matches(isDisplayed()))
        //NowSwitching to the categories by clicking the categories button
        onView(withId(R.id.button_categories)).perform(click())
        onView(withId(R.id.fragment_map_download_categories_parent)).check(matches(isDisplayed()))
        //by clicking it again we should stay here
        onView(withId(R.id.button_categories)).perform(click())
        onView(withId(R.id.fragment_map_download_categories_parent)).check(matches(isDisplayed()))
        //now open the FragmentRequestNewMap by clicking the fab
        onView(withId(R.id.request_new_map_fab)).perform(click())
        onView(withId(R.id.fragment_request_new_map_parent)).check(matches(isDisplayed()))
        //going back  - the categories fragment should be in view
        Espresso.pressBack()
        onView(withId(R.id.fragment_map_download_categories_parent)).check(matches(isDisplayed()))
        //Now open the FragmentRequestNewMap from the All maps tab again
        onView(withId(R.id.button_all_maps)).perform(click())
        onView(withId(R.id.fragment_map_download_all_parent)).check(matches(isDisplayed()))
        onView(withId(R.id.request_new_map_fab)).perform(click())
        onView(withId(R.id.fragment_request_new_map_parent)).check(matches(isDisplayed()))
        Espresso.pressBack()
        onView(withId(R.id.fragment_map_download_all_parent)).check(matches(isDisplayed()))
        //Open the ActionBars  option menu
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext);
        //Open the FragmentOptions
        onView(withText(R.string.menu_settings)).check(matches(isDisplayed()))
        onView(withText(R.string.menu_settings)).perform(click())
        onView(withId(R.id.fragment_options_parent)).check(matches(isDisplayed()))
        //clicking the darkmode switch
        onView(withId(R.id.dark_mode_switch)).perform(click())
        //should bring us back in the options fragment (now in dark )
        onView(withId(R.id.fragment_options_parent)).check(matches(isDisplayed()))
        //Open the ActionBars option menu
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext);
        //Open the FragmentFAQ
        onView(withText(R.string.menu_faq)).check(matches(isDisplayed()))
        onView(withText(R.string.menu_faq)).perform(click())
        onView(withId(R.id.menu_faq_parent)).check(matches(isDisplayed()))
        //Open the ActionBars option menu
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext);
        //Open the FragmentFAQ
        onView(withText(R.string.menu_about)).check(matches(isDisplayed()))
        onView(withText(R.string.menu_about)).perform(click())
        onView(withId(R.id.fragment_about_parent)).check(matches(isDisplayed()))

    }

}