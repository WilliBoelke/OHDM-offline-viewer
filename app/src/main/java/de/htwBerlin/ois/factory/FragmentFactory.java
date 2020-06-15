package de.htwBerlin.ois.factory;

import android.util.Log;

import androidx.fragment.app.Fragment;

import de.htwBerlin.ois.serverCommunication.Client;
import de.htwBerlin.ois.ui.fragments.FragmentAbout;
import de.htwBerlin.ois.ui.fragments.FragmentDownloadCenterAll;
import de.htwBerlin.ois.ui.fragments.FragmentDownloadCenterCategories;
import de.htwBerlin.ois.ui.fragments.FragmentFAQ;
import de.htwBerlin.ois.ui.fragments.FragmentHome;
import de.htwBerlin.ois.ui.fragments.FragmentNavigation;
import de.htwBerlin.ois.ui.fragments.FragmentOptions;
import de.htwBerlin.ois.ui.fragments.FragmentRequestNewMap;
import de.htwBerlin.ois.ui.fragments.FragmentRequestStatus;


public class FragmentFactory extends androidx.fragment.app.FragmentFactory
{

    private String TAG = getClass().getSimpleName();
    private Client client;

    public FragmentFactory(Client client)
    {
        this.client = client;
    }

    @Override
    public Fragment instantiate(ClassLoader classLoader, String className)
    {

        if (className.equals(FragmentHome.class.getName()))
        {
            Log.d(TAG, "Instantiate : FragmentHome");
            return new FragmentHome();
        }
        if (className.equals(FragmentNavigation.class.getName()))
        {
            Log.d(TAG, "Instantiate : FragmentNavigation");
            return new FragmentNavigation();
        }
        if (className.equals(FragmentDownloadCenterAll.class.getName()))
        {
            Log.d(TAG, "Instantiate : FragmentDownloadCenterAll");
            return new FragmentDownloadCenterAll(client);
        }
        if (className.equals(FragmentDownloadCenterCategories.class.getName()))
        {
            Log.d(TAG, "Instantiate : FragmentDownloadCenterCategories");
            return new FragmentDownloadCenterCategories(client);
        }
        if (className.equals(FragmentRequestNewMap.class.getName()))
        {
            Log.d(TAG, "Instantiate : FragmentRequestNewMap");
            return new FragmentRequestNewMap();
        }
        if (className.equals(FragmentAbout.class.getName()))
        {
            Log.d(TAG, "Instantiate : FragmentAbout");
            return new FragmentAbout();
        }
        if (className.equals(FragmentOptions.class.getName()))
        {
            Log.d(TAG, "Instantiate : FragmentOptions");
            return new FragmentOptions();
        }
        if (className.equals(FragmentFAQ.class.getName()))
        {
            Log.d(TAG, "Instantiate : FragmentFAQ");
            return new FragmentFAQ();
        }
        if (className.equals(FragmentRequestStatus.class.getName()))
        {
            Log.d(TAG, "Instantiate : FragmentRequestStatus");
            return new FragmentRequestStatus();
        }
        else
        {
            super.instantiate(classLoader, className);
        }

        return new FragmentHome();
    }

}

