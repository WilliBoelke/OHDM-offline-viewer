package de.htwBerlin.ois.model.repositories.remoteRepositories;


import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import de.htwBerlin.ois.model.models.fileStructure.RemoteDirectory;
import de.htwBerlin.ois.model.models.fileStructure.RemoteFile;
import de.htwBerlin.ois.model.repositories.localRepositories.UserPreferences;
import de.htwBerlin.ois.serverCommunication.AsyncResponse;
import de.htwBerlin.ois.serverCommunication.HttpClient;
import de.htwBerlin.ois.serverCommunication.HttpRequest;

import static de.htwBerlin.ois.serverCommunication.HttpRequest.REQUEST_TYP_STATUS_BY_ID;


public class HttpRequestsRepository
{
    //------------Static Variables------------

    private static HttpRequestsRepository instance;


    //------------Instance Variables------------

    private final String TAG = getClass().getSimpleName();
    private MutableLiveData<String> data;
    private String requests;


    //------------Constructors------------

    /**
     * Private constructor
     */
    private HttpRequestsRepository()
    {
        data = new MutableLiveData<>();
        requests = "";
    }

    public static HttpRequestsRepository getInstance()
    {
        if (instance == null)
        {
            instance = new HttpRequestsRepository();
        }
        return instance;
    }


    //------------Getter------------

    /**
     * @return
     */
    public MutableLiveData<String> getRequests()
    {
        retrieveRequests();
        data.setValue(requests);
        return data;
    }


    //------------Retrieving Data------------

    /**
     * Scans OHDM Directory for .map Files
     *
     * @return a set of .map files
     */
    private void retrieveRequests()
    {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setParams("id=" + UserPreferences.getInstance().getUserID());
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


    //------------Send Data------------
}

