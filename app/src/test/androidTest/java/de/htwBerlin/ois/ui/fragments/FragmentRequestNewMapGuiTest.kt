package de.htwBerlin.ois.ui.fragments

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import de.htwBerlin.ois.R
import de.htwBerlin.ois.factory.FragmentFactory
import de.htwBerlin.ois.serverCommunication.SftpClient
import de.htwBerlin.ois.ui.fragments.FragmentRequestNewMap
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class FragmentRequestNewMapGuiTest {

    @Before
    fun setup() {
        val sftpClient = SftpClient()
        val fragmentFactory = FragmentFactory(sftpClient)
        val bundle = Bundle()
        val scenario = launchFragmentInContainer<FragmentRequestNewMap>(themeResId = R.style.LightTheme, fragmentArgs = bundle, factory = fragmentFactory)
    }


    //------------Layout Tests------------

    @Test
    fun viewsAreDisplayed() {
        onView(withId(R.id.fragment_request_new_map_parent)).check(matches(isDisplayed()))
        onView(withId(R.id.inner_constraint_layout)).check(matches(isDisplayed()))

        onView(withId(R.id.name_et)).check(matches(isDisplayed()))

        onView(withId(R.id.content_card_info)).check(matches(isDisplayed()))
        onView(withId(R.id.info_inner_constraint_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.info_tv)).check(matches(isDisplayed()))

        onView(withId(R.id.content_card_coordinates)).check(matches(isDisplayed()))
        onView(withId(R.id.coordinates_inner_constraint_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.long_bottom_et)).check(matches(isDisplayed()))
        onView(withId(R.id.lat_left_et)).check(matches(isDisplayed()))
        onView(withId(R.id.long_top_et)).check(matches(isDisplayed()))
        onView(withId(R.id.lat_right_et)).check(matches(isDisplayed()))
        onView(withId(R.id.coords_info_tv)).check(matches(isDisplayed()))

        onView(withId(R.id.content_card_date_picker)).check(matches(isDisplayed()))
        onView(withId(R.id.date_picker)).check(matches(isDisplayed()))

        onView(withId(R.id.request_button)).check(matches(isDisplayed()))

    }

    @Test
    fun viewContents() {
        onView(withId(R.id.info_tv)).check(matches(withText(R.string.request_map_info)))
        onView(withId(R.id.name_et)).check(matches(withHint(R.string.name)))

        onView(withId(R.id.long_bottom_et)).check(matches(withHint(R.string.long_bottom)))
        onView(withId(R.id.lat_left_et)).check(matches(withHint(R.string.lat_left)))
        onView(withId(R.id.long_top_et)).check(matches(withHint(R.string.long_top)))
        onView(withId(R.id.lat_right_et)).check(matches(withHint(R.string.lat_right)))
        onView(withId(R.id.coords_info_tv)).check(matches(withText(R.string.coords_info)))

        onView(withId(R.id.request_button)).check(matches(withText(R.string.submit_request)))
    }

    @Test
    fun hierarchy() {
        //scroll_view just has inner.Constraint_layout as child
        onView(withId(R.id.fragment_request_new_map_parent)).check(matches(withChild(withId(R.id.inner_constraint_layout))))

        //inner_constraint_layout
        onView(withId(R.id.inner_constraint_layout)).check(matches(withParent(withId(R.id.fragment_request_new_map_parent))))
        onView(withId(R.id.inner_constraint_layout)).check(matches(withChild(withId(R.id.name_et))))
        onView(withId(R.id.inner_constraint_layout)).check(matches(withChild(withId(R.id.content_card_coordinates))))
        onView(withId(R.id.inner_constraint_layout)).check(matches(withChild(withId(R.id.content_card_date_picker))))
        onView(withId(R.id.inner_constraint_layout)).check(matches(withChild(withId(R.id.space))))
        onView(withId(R.id.inner_constraint_layout)).check(matches(withChild(withId(R.id.content_card_info))))

        //name et
        onView(withId(R.id.name_et)).check(matches(withParent(withId(R.id.inner_constraint_layout))))
        onView(withId(R.id.name_et)).check(matches(hasChildCount(0)))

    }


}