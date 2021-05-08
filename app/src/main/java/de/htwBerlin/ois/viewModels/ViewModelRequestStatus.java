package de.htwBerlin.ois.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.StringTokenizer;

import de.htwBerlin.ois.model.repositories.remoteRepositories.HttpRepository;

import static de.htwBerlin.ois.serverCommunication.HttpClient.RESPONSE_NO_CONNECTION;
import static de.htwBerlin.ois.serverCommunication.HttpClient.RESPONSE_NO_REQUESTS;

public class ViewModelRequestStatus extends ViewModel
{
    //------------Instance Variables------------

    private final String TAG = getClass().getSimpleName();
    HttpRepository requestStatusRepository;
    MutableLiveData<String> response;
    MutableLiveData<Boolean> requestReceived;
    MutableLiveData<Boolean> noConnection;
    MutableLiveData<Boolean> noRequests;
    MutableLiveData<Boolean> connecting;
    MutableLiveData<ArrayList<String>> requests;

    public void init()
    {
        Log.d(TAG, "init : initializing ... ");
        if (response != null)
        {
            Log.d(TAG, "init : already initialized ");
            return;
        }
        requests = new MutableLiveData<>();
        response = new MutableLiveData<>();
        requestReceived = new MutableLiveData<>();
        noConnection = new MutableLiveData<>();
        noRequests = new MutableLiveData<>();
        connecting = new MutableLiveData<>();
        requests.setValue(new ArrayList<>());
        requestReceived.setValue(true);
        noRequests.setValue(true);
        connecting.setValue(true);
        noConnection.setValue(true);
        requestReceived.setValue(true);

        Log.d(TAG, "init : finished ");
    }

    /**
     * Gets the response from the HttpRequest as String, this will e observed by the view.
     * As soon as the value changes the View will call processString() with it and the which will the
     * alter the value of {@link requests}, and/or the Booleans which again are observed by the views
     *
     * @return the Server response as String
     */
    public LiveData<String> getResponse()
    {
        requestStatusRepository = HttpRepository.getInstance();
        this.response = requestStatusRepository.getRequests();
        Log.d(TAG, "getResponse called");
        return this.response;
    }

    /**
     * Provides a Live Data object to the view, which  will get notified as soon as it changes.
     * It will change when there came a valid response from the server by the processString() method
     * @return
     */
    public LiveData<ArrayList<String>> getRequests()
    {
        Log.d(TAG, "getRequests called");
        return this.requests;
    }

    public LiveData<Boolean> getNoConnectionBoolean()
    {
        Log.d(TAG, "getNoConnectionBoolean called");
        return this.noConnection;
    }

    public LiveData<Boolean> getRequestReceivedBoolean()
    {
        Log.d(TAG, "getRequestReceivedBoolean called");
        return this.requestReceived;
    }

    public LiveData<Boolean> getNoRequestsForIdBoolean()
    {
        Log.d(TAG, "getNoRequestsForIdBoolean called");
        return this.noRequests;
    }

    public LiveData<Boolean> getConnectionBoolean()
    {
        Log.d(TAG, "getConnectionBoolean called");
        return this.connecting;
    }

    public void refresh()
    {
        Log.d(TAG, "refresh called");
        this.requestStatusRepository.getRequests();
    }

    /**
     * Processes the String the server returned as response
     * Checks if its valid , the builds an ArrayList from it which can be displayed by the view.
     * the string also can be :
     * {@link RESPONSE_NO_CONNECTION} when the server didn't respond
     * {@link RESPONSE_NO_REQUESTS} when the server responded but there weren't any requests made yet
     * "" before a request was made
     *
     * In all cases this method will changes the value of the corresponding boolean so the view can react to it
     * @param s
     */
    public void processString(String s)
    {
        if (s.equals(RESPONSE_NO_CONNECTION))
        {
            Log.e(TAG, "processString :  Server returned no connection ");
            noConnection.postValue(!noConnection.getValue());

        }
        else if (s.equals(RESPONSE_NO_REQUESTS))
        {
            Log.e(TAG, "processString :  Server returned no requests for id ");
            noRequests.postValue(!noRequests.getValue());
        }
        else if (s.equals(""))
        {
            connecting.postValue(!connecting.getValue());
        }
        else
        {
            Log.d(TAG, "processString :  Server returned requests ");
            requestReceived.postValue(!requestReceived.getValue());
            String formatted = s.replace("-", " ");
            formatted = formatted.replace("[", "");
            formatted = formatted.replace("]", "");
            formatted = formatted.replace("\"", "");
            StringTokenizer st = new StringTokenizer(formatted, ",");
            ArrayList<String> temp = new ArrayList<>();
            while (st.hasMoreTokens())
            {
                temp.add(st.nextToken());
            }
            requests.setValue(temp);
            Log.d(TAG, "processString :  Made reques list from string  = " + temp.toString());
        }
    }


}
