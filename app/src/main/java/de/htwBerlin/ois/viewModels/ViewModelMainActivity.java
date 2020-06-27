package de.htwBerlin.ois.viewModels;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.io.File;
import java.util.ArrayList;

import de.htwBerlin.ois.model.models.fileStructure.RemoteDirectory;
import de.htwBerlin.ois.model.models.fileStructure.RemoteFile;
import de.htwBerlin.ois.model.repositories.localRepositories.UserPreferences;
import de.htwBerlin.ois.serverCommunication.AsyncResponse;
import de.htwBerlin.ois.serverCommunication.HttpClient;
import de.htwBerlin.ois.serverCommunication.HttpTaskRequest;
import de.htwBerlin.ois.views.fragments.FragmentHome;
import de.htwBerlin.ois.views.fragments.FragmentOptions;

import static de.htwBerlin.ois.model.repositories.localRepositories.Variables.MAP_FILE_PATH;
import static de.htwBerlin.ois.serverCommunication.HttpTaskRequest.REQUEST_TYPE_ID;

/**
 * The ViewModel for the MainActivity
 *
 * @author WilliBoelke
 */
public class ViewModelMainActivity extends ViewModel
{
    private final String TAG = getClass().getSimpleName();


    //------------On First App Start------------

    /**
     * Creates OHDM Folder if not exists
     */
    public void createOhdmDirectory()
    {
        Log.d(TAG, "createOhdmDirectory : Creating OHDM directory...");
        File dir = new File(MAP_FILE_PATH);
        Log.d(TAG, "createOhdmDirectory : OHDM directory created");
    }

    /**
     * At the first start oft the app, i want to ge an ID
     * from the server, to identify the user /  his requests,
     * - but completely anonymous
     */
    public void HttpGetIdFromServer()
    {
        if (UserPreferences.getInstance().getUserID() == null)
        {
            HttpTaskRequest httpRequest = new HttpTaskRequest();
            httpRequest.setRequestType(REQUEST_TYPE_ID);
            httpRequest.setHttpClient(new HttpClient());
            httpRequest.setAsyncResponse(new AsyncResponse()
            {
                @Override
                public void getRemoteFiles(ArrayList<RemoteFile> remoteFiles)
                {
                    //not needed here
                }

                @Override
                public void getRemoteDirectories(ArrayList<RemoteDirectory> remoteDirectories)
                {
                    //not needed here
                }

                @Override
                public void getHttpResponse(String response)
                {

                    if (response != null)
                    {
                        UserPreferences.getInstance().setUserId(response);
                        Log.d(TAG, "ID from server = " + response);
                    }

                }
            });

            httpRequest.execute();
        }
    }


    //------------Other------------

    /**
     * returns the "correct" Fragment which in this case is either the {@link FragmentHome}
     * or in case the user just switched the App Theme the {@link FragmentOptions}
     *
     * @param intent the intent which started the MainActivity
     * @return FragmentHome.class or FragmentOptions.class
     */
    public Class getCorrectFragment(Intent intent, Bundle savedInstanceState)
    {

        if (intent.getStringExtra("Fragment") != null)
        {
            if (intent.getStringExtra("Fragment").equals(FragmentOptions.ID))
            {
                //if we came her from the reset method in the options fragment, we want the options fragment to appear again
                intent.putExtra("Fragment", "");
                return FragmentOptions.class;
            }
        }
        else
        {
            if (savedInstanceState == null)
            {
                return FragmentHome.class;
            }
        }

        return null;
    }

    public boolean isDarkModeEnabled()
    {
        return UserPreferences.getInstance().isDarkModeEnabled();
    }


}
