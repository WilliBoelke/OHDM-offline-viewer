package de.htwBerlin.ois.ui.fragments

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import de.htwBerlin.ois.R
import de.htwBerlin.ois.factory.FragmentFactory
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class Blabla
{


    @org.junit.Test
    fun testEventFragment() {
        val fragmentFactory = FragmentFactory()
        val bundle = Bundle()

        val scenario = launchFragmentInContainer<FragmentAbout>(fragmentArgs = bundle, factory = fragmentFactory)

        Espresso.onView(ViewMatchers.withId(R.id.headline1_tv)).check(ViewAssertions.matches(ViewMatchers.withText("OHDM ..\"")))
    }
}