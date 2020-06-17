package de.htwBerlin.ois.serverCommunication;

import android.os.AsyncTask;
import android.util.Log;


/**
 * AsyncTask to make a HTTP Request to the Server
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
        httpClient.connect(requestType);
    }

    @Override
    protected String doInBackground(Void... params)
    {
        httpClient.sendRequest(paramsString);
        httpClient.closeConnection();
        return "";
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        delegate.getHttpResponse(httpClient.getServerResponse());
    }


}
