package de.htwBerlin.ois.factory;

import android.util.Log;

import androidx.fragment.app.Fragment;

import org.apache.http.impl.client.HttpRequestFutureTask;

import de.htwBerlin.ois.serverCommunication.FtpTaskDirListing;
import de.htwBerlin.ois.serverCommunication.FtpTaskFileDownloading;
import de.htwBerlin.ois.serverCommunication.FtpTaskFileListing;
import de.htwBerlin.ois.serverCommunication.HTTPRequestNewMap;
import de.htwBerlin.ois.serverCommunication.SftpClient;
import de.htwBerlin.ois.ui.fragments.FragmentAbout;
import de.htwBerlin.ois.ui.fragments.FragmentDownloadCenterAll;
import de.htwBerlin.ois.ui.fragments.FragmentDownloadCenterCategories;
import de.htwBerlin.ois.ui.fragments.FragmentFAQ;
import de.htwBerlin.ois.ui.fragments.FragmentHome;
import de.htwBerlin.ois.ui.fragments.FragmentNavigation;
import de.htwBerlin.ois.ui.fragments.FragmentOptions;
import de.htwBerlin.ois.ui.fragments.FragmentRequestNewMap;


public class FragmentFactory extends androidx.fragment.app.FragmentFactory
{

    private String TAG = getClass().getSimpleName();
    private SftpClient sftpClient;

    public FragmentFactory(SftpClient sftpClient)
    {
        this.sftpClient = sftpClient;
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
            return new FragmentDownloadCenterAll(sftpClient);
        }
        if (className.equals(FragmentDownloadCenterCategories.class.getName()))
        {
            Log.d(TAG, "Instantiate : FragmentDownloadCenterCategories");
            return new FragmentDownloadCenterCategories(sftpClient);
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
        else
        {
            super.instantiate(classLoader, className);
        }

        return new FragmentHome();
    }

}

