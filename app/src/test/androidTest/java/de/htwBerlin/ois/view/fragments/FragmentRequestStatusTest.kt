package de.htwBerlin.ois.view.fragments

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import de.htwBerlin.ois.R
import de.htwBerlin.ois.views.factory.FragmentFactory
import de.htwBerlin.ois.serverCommunication.HttpClient
import de.htwBerlin.ois.views.fragments.FragmentRequestNewMap
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class FragmentRequestStatusTest
{
    @Before
    fun setup() {
        val sftpClient = MockClient(false, false)
        val httpClient = HttpClient()
        val fragmentFactory = FragmentFactory(sftpClient, httpClient)
        val bundle = Bundle()
        val scenario = launchFragmentInContainer<FragmentRequestNewMap>(themeResId = R.style.LightTheme, fragmentArgs = bundle, factory = fragmentFactory)
    }


}