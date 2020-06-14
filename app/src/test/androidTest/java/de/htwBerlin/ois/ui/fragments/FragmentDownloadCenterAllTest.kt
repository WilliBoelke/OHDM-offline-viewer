package de.htwBerlin.ois.ui.fragments

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import de.htwBerlin.ois.R
import de.htwBerlin.ois.factory.FragmentFactory
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class FragmentDownloadCenterAllTest {

    @Before
    fun setup() {

        /*val mockSftpClient = mockk<SftpClient>()
        every {
            mockSftpClient.getAllFileList(FTP_ROOT_DIRECTORY)
        } returns allFiles
        every {
            mockSftpClient.getFileList(MOST_RECENT_PATH)
        } returns recentFiles*/

        val fragmentFactory = FragmentFactory(MockClient())
        val bundle = Bundle()
        val scenario = launchFragmentInContainer<FragmentDownloadCenterAll>(themeResId = R.style.LightTheme, fragmentArgs = bundle, factory = fragmentFactory)
    }


    @Test
    fun viewsAreDisplayed() {
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
    fun SearchTest()
    {

    }

    @Test
    fun RecyclerTest()
    {

    }



}