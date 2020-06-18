package de.htwBerlin.ois.models.repositories.remoteRepositories;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;


import de.htwBerlin.ois.models.fileStructure.RemoteDirectory;
import de.htwBerlin.ois.models.fileStructure.RemoteFile;
import de.htwBerlin.ois.models.repositories.cacheRepostitories.RuntimeVariables;
import de.htwBerlin.ois.serverCommunication.AsyncResponse;
import de.htwBerlin.ois.serverCommunication.HttpClient;
import de.htwBerlin.ois.serverCommunication.HttpRequest;


import static de.htwBerlin.ois.serverCommunication.HttpRequest.REQUEST_TYP_STATUS_BY_ID;


public class RequestStatusRepository
{

    private static RequestStatusRepository instance;
    private final String TAG = getClass().getSimpleName();
    MutableLiveData<String> data;
    private String requests;

    public static RequestStatusRepository getInstance()
    {
        if (instance == null)
        {
            instance = new RequestStatusRepository();
        }
        return instance;
    }



    private RequestStatusRepository()
    {
        data = new MutableLiveData<>();
        requests = "";
    }

    public MutableLiveData<String> getRequests()
    {
        retrieveRequests();
        data.setValue(requests);
        return data;
    }

    /**
     * Scans OHDM Directory for .map Files
     *
     * @return a set of .map files
     */
    private void retrieveRequests()
    {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setParams("id=" + RuntimeVariables.Instance().getUserID());
        httpRequest.setRequestType(REQUEST_TYP_STATUS_BY_ID);
        httpRequest.setHttpClient(new HttpClient());
        httpRequest.setAsyncResponse(new AsyncResponse()
        {

            @Override
            public void getRemoteFiles(ArrayList<RemoteFile> remoteFiles)
            {

            }

            @Override
            public void getRemoteDirectories(ArrayList<RemoteDirectory> remoteDirectories)
            {

            }

            @Override
            public void getHttpResponse(String response)
            {
                requests = response;
                data.setValue(requests);
            }
        });

        httpRequest.execute();
    }
}

