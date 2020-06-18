package de.htwBerlin.ois.view.fragments

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import de.htwBerlin.ois.R
import de.htwBerlin.ois.views.factory.FragmentFactory
import de.htwBerlin.ois.repositories.remoteRepositories.HttpClient
import de.htwBerlin.ois.views.fragments.FragmentDownloadCenterAll
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test

class FragmentDownloadCenterCategoriesTest
{
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
    fun recyclerViewTestIsDisplayed() {
        val sftpClient = MockClient(false, false)
        val httpClient = HttpClient()
        val fragmentFactory = FragmentFactory(sftpClient, httpClient)
        val bundle = Bundle()
        val scenario = launchFragmentInContainer<FragmentDownloadCenterAll>(themeResId = R.style.LightTheme, fragmentArgs = bundle, factory = fragmentFactory)

        Espresso.onView(ViewMatchers.withText("MapOne")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText("MapTwo")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText("MapThree")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText("MapFour")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText("MapFive")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText("MapSix")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.connecting_pb)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.connecting_tv)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
    }

    @Test
    fun recyclerViewTestNotDisplayed() {
        val sftpClient = MockClient(false, true)
        val httpClient = HttpClient()
        val fragmentFactory = FragmentFactory(sftpClient, httpClient)
        val bundle = Bundle()
        val scenario = launchFragmentInContainer<FragmentDownloadCenterAll>(themeResId = R.style.LightTheme, fragmentArgs = bundle, factory = fragmentFactory)

        Espresso.onView(ViewMatchers.withId(R.id.all_maps_recycler)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.most_recent_maps_recycler)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))

        Espresso.onView(ViewMatchers.withId(R.id.connecting_pb)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.connecting_tv)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}