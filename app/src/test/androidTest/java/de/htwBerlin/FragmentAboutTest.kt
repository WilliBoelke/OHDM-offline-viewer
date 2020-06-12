package de.htwBerlin

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import de.htwBerlin.ois.R
import de.htwBerlin.ois.factory.FragmentFactory
import de.htwBerlin.ois.ui.fragments.FragmentAbout
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class FragmentAboutTest {

    @Before
    fun setup() {
        val fragmentFactory = FragmentFactory()
        val bundle = Bundle()
        val scenario = launchFragmentInContainer<FragmentAbout>(themeResId = R.style.LightTheme, fragmentArgs = bundle, factory = fragmentFactory)
    }


    //------------Layout Tests------------

    @Test
    fun infoTextDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.info1_tv)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.info2_tv)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.info3_tv)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun contentCardsDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.content_card1)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.content_card2)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.content_card3)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun headlineTextViewDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.headline1_tv)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.headline2_tv)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.headline3_tv)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun imagesViewsDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.ohdm_logo_iv)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.htw_logo_iv)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun infoTextContents() {
        Espresso.onView(ViewMatchers.withId(R.id.info1_tv)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.about_ohdm)))
        Espresso.onView(ViewMatchers.withId(R.id.info2_tv)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.about_ohdm_offline_viewer)))
        Espresso.onView(ViewMatchers.withId(R.id.info3_tv)).check(ViewAssertions.matches(ViewMatchers.withText("https://github.com/OpenHistoricalDataMap")))
    }

    @Test
    fun headlineContents() {
        Espresso.onView(ViewMatchers.withId(R.id.headline1_tv)).check(ViewAssertions.matches(ViewMatchers.withText("OHDM ...")))
        Espresso.onView(ViewMatchers.withId(R.id.headline3_tv)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.find_us)))
        Espresso.onView(ViewMatchers.withId(R.id.headline2_tv)).check(ViewAssertions.matches(ViewMatchers.withText("OHDM Offline Viewer ...")))
    }


    //------------Function Tests------------


}