package de.htwBerlin.ois.factory;

import androidx.fragment.app.Fragment;

import de.htwBerlin.ois.ui.fragments.FragmentAbout;
import de.htwBerlin.ois.ui.fragments.FragmentDownloadCenterAll;
import de.htwBerlin.ois.ui.fragments.FragmentDownloadCenterCategories;
import de.htwBerlin.ois.ui.fragments.FragmentFAQ;
import de.htwBerlin.ois.ui.fragments.FragmentHome;
import de.htwBerlin.ois.ui.fragments.FragmentNavigation;
import de.htwBerlin.ois.ui.fragments.FragmentOptions;
import de.htwBerlin.ois.ui.fragments.FragmentrequestNewMap;


public class FragmentFactory extends androidx.fragment.app.FragmentFactory
{
    private String TAG = getClass().getSimpleName();

    @Override
    public  Fragment instantiate(ClassLoader classLoader, String className)
    {

        if (className.equals(FragmentHome.class.getName()))
        {
            return new FragmentHome();
        }
        if (className.equals(FragmentNavigation.class.getName()))
        {
            return new FragmentNavigation();
        }
        if (className.equals(FragmentDownloadCenterAll.class.getName()))
        {
            return new FragmentDownloadCenterAll();
        }
        if (className.equals(FragmentDownloadCenterCategories.class.getName()))
        {
            return new FragmentDownloadCenterCategories();
        }
        if (className.equals(FragmentrequestNewMap.class.getName()))
        {
            return new FragmentrequestNewMap();
        }
        if (className.equals(FragmentAbout.class.getName()))
        {
            return new FragmentAbout();
        }
        if (className.equals(FragmentOptions.class.getName()))
        {
            return new FragmentOptions();
        }
        if (className.equals(FragmentFAQ.class.getName()))
        {
            return new FragmentFAQ();
        }

        return  new FragmentHome();
    }

}

