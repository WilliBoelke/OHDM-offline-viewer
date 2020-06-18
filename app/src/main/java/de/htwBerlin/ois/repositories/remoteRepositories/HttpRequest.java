package de.htwBerlin.ois.repositories.remoteRepositories;

import android.os.AsyncTask;
import android.util.Log;

import static de.htwBerlin.ois.repositories.remoteRepositories.HttpClient.RESPONSE_NO_CONNECTION;


/**
 * AsyncTask to make HTTP request using the {@link HttpClient}
 * <p>
 *
 * @author WilliBoelke
 */
public class HttpRequest extends AsyncTask<Void, Void, String>
{
    public static final String REQUEST_TYPE_ID = "/id";
    public static final String REQUEST_TYPE_MAP_REQUEST = "/request";
    /**
     * Ypu need to pass the id in the paramsString
     * when using this request type
     * <p>
     * example :
     * id=63Wjwqs
     */
    public static final String REQUEST_TYP_STATUS_BY_ID = "/statusByID";

    //------------Instance Variables------------

    private final String TAG = this.getClass().getSimpleName();
    private String requestType;
    private String paramsString;
    private AsyncResponse delegate;
    private HttpClient httpClient;
    private String response;


    //------------Constructors------------

    /**
     * Public constructor
     *
     * @param
     */
    public HttpRequest()
    {
        Log.d(TAG, "Constructor:  new HttpRequestNewMap ");
        this.paramsString = "";
    }


    //------------Setter------------

    /**
     * setter for the 
     * @param client
     */
    public void setHttpClient(HttpClient client)
    {
        this.httpClient = client;
    }

    public void setRequestType(String requestType)
    {
        this.requestType = requestType;
    }

    public void setParams(String params)
    {
        this.paramsString = params;
    }

    public void setAsyncResponse(AsyncResponse delegate)
    {
        this.delegate = delegate;
    }


    //------------AsyncTask Implementation------------

    @Override
    protected void onPreExecute()
    {
        int result = httpClient.connect(requestType);
        if (result  != 0)
        {
            response = RESPONSE_NO_CONNECTION;
            this.onPostExecute("");
        }
    }

    @Override
    protected String doInBackground(Void... params)
    {
        int result = httpClient.sendRequest(paramsString);
        if(result == 0)
        {
            response = httpClient.getServerResponse();
            httpClient.closeConnection();
        }
        else
        {
            response =RESPONSE_NO_CONNECTION;
        }
        return "";
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        delegate.getHttpResponse(response);
    }


}
