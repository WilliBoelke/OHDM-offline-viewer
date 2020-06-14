package de.htwBerlin.ois.ui.fragments

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import de.htwBerlin.ois.R
import de.htwBerlin.ois.factory.FragmentFactory
import de.htwBerlin.ois.serverCommunication.SftpClient
import de.htwBerlin.ois.ui.fragments.FragmentAbout
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class FragmentAboutGuiTest {

    @Before
    fun setup() {
        val sftpClient = SftpClient()
        val fragmentFactory = FragmentFactory(sftpClient)
        val bundle = Bundle()
        val scenario = launchFragmentInContainer<FragmentAbout>(themeResId = R.style.LightTheme, fragmentArgs = bundle, factory = fragmentFactory)
    }


    //------------Layout Tests------------

    @Test
    fun viewsAreDisplayed()
    {
        onView(withId(R.id.info1_tv)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.info2_tv)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.info3_tv)).check(ViewAssertions.matches(isDisplayed()))

        onView(withId(R.id.content_card1)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.content_card2)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.content_card3)).check(ViewAssertions.matches(isDisplayed()))

        onView(withId(R.id.headline1_tv)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.headline2_tv)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.headline3_tv)).check(ViewAssertions.matches(isDisplayed()))

        onView(withId(R.id.ohdm_logo_iv)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.htw_logo_iv)).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun viewContents() {
        onView(withId(R.id.info1_tv)).check(ViewAssertions.matches(withText(R.string.about_ohdm)))
        onView(withId(R.id.info2_tv)).check(ViewAssertions.matches(withText(R.string.about_ohdm_offline_viewer)))
        onView(withId(R.id.info3_tv)).check(ViewAssertions.matches(withText("https://github.com/OpenHistoricalDataMap")))

        onView(withId(R.id.headline1_tv)).check(ViewAssertions.matches(withText("OHDM ...")))
        onView(withId(R.id.headline3_tv)).check(ViewAssertions.matches(withText(R.string.find_us)))
        onView(withId(R.id.headline2_tv)).check(ViewAssertions.matches(withText("OHDM Offline Viewer ...")))
    }


    //------------Function Tests------------


}