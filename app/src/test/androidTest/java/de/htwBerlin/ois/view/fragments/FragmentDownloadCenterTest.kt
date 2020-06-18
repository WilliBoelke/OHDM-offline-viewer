package de.htwBerlin.ois.view.fragments

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import de.htwBerlin.ois.R
import de.htwBerlin.ois.views.factory.FragmentFactory
import de.htwBerlin.ois.repositories.remoteRepositories.HttpClient
import de.htwBerlin.ois.views.fragments.FragmentDownloadCenterAll
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class FragmentDownloadCenterTest {

    @Before
    fun setup() {
        val sftpClient = MockClient(false, false)
        val httpClient = HttpClient()
        val fragmentFactory = FragmentFactory(sftpClient, httpClient)
        val bundle = Bundle()
        val scenario = launchFragmentInContainer<FragmentDownloadCenterAll>(themeResId = R.style.LightTheme, fragmentArgs = bundle, factory = fragmentFactory)
    }


    @Test
    fun viewsAreDisplayed() {
        val sftpClient = MockClient(false, false)
        val httpClient = HttpClient()
        val fragmentFactory = FragmentFactory(sftpClient, httpClient)
        val bundle = Bundle()
        val scenario = launchFragmentInContainer<FragmentDownloadCenterAll>(themeResId = R.style.LightTheme, fragmentArgs = bundle, factory = fragmentFactory)

        Espresso.onView(ViewMatchers.withId(R.id.fragment_map_download_all_parent)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.request_new_map_fab)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.swipe_to_refresh)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.scroll_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.inner_constraint_layout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.all_tv)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.all_maps_recycler)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.all_sv)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.lates_tv)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.most_recent_maps_recycler)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.recent_sv)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.button_categories)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.button_all_maps)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun searchTest()
    {

    }

    @Test
    fun recyclerViewTestIsDisplayed() {
        val sftpClient = MockClient(false, false)
        val httpClient = HttpClient()
        val fragmentFactory = FragmentFactory(sftpClient, httpClient)
        val bundle = Bundle()
        val scenario = launchFragmentInContainer<FragmentDownloadCenterAll>(themeResId = R.style.LightTheme, fragmentArgs = bundle, factory = fragmentFactory)

        Espresso.onView(ViewMatchers.withText("MapOne")).check(matches(isDisplayed()))
        Espresso.onView(ViewMatchers.withText("MapTwo")).check(matches(isDisplayed()))
        Espresso.onView(ViewMatchers.withText("MapThree")).check(matches(isDisplayed()))
        Espresso.onView(ViewMatchers.withText("MapFour")).check(matches(isDisplayed()))
        Espresso.onView(ViewMatchers.withText("MapFive")).check(matches(isDisplayed()))
        Espresso.onView(ViewMatchers.withText("MapSix")).check(matches(isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.connecting_pb)).check(matches(not(isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.connecting_tv)).check(matches(not(isDisplayed())))
    }

    @Test
    fun recyclerViewTestNotDisplayed() {
        val sftpClient = MockClient(false, true)
        val httpClient = HttpClient()
        val fragmentFactory = FragmentFactory(sftpClient, httpClient)
        val bundle = Bundle()
        val scenario = launchFragmentInContainer<FragmentDownloadCenterAll>(themeResId = R.style.LightTheme, fragmentArgs = bundle, factory = fragmentFactory)

        Espresso.onView(ViewMatchers.withId(R.id.all_maps_recycler)).check(matches(not(isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.most_recent_maps_recycler)).check(matches(not(isDisplayed())))

        Espresso.onView(ViewMatchers.withId(R.id.connecting_pb)).check(matches(isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.connecting_tv)).check(matches(isDisplayed()))
    }
}