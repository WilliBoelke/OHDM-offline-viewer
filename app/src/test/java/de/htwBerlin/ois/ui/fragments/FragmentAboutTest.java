package de.htwBerlin.ois.ui.fragments;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.runner.RunWith;

import de.htwBerlin.ois.factory.FragmentFactory;


@RunWith(AndroidJUnit4.class)
class FragmentAboutTest
{
    FragmentFactory fragmentFactory;

    @Before
    public void setup()
    {
        FragmentFactory fragmentFactory = new FragmentFactory();

    }
}