package de.htwBerlin.ois.serverCommunication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

import static de.htwBerlin.ois.serverCommunication.Variables.HTTP_PORT;
import static de.htwBerlin.ois.serverCommunication.Variables.SERVER_IP;


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
     *
     * example :
     * id=63Wjwqs
     *
     */
    public static final String REQUEST_TYP_STATUS_BY_ID = "/statusByID";

    //------------Instance Variables------------

    /**
     * Log tag
     */
    private final String TAG = this.getClass().getSimpleName();
    private final String requestType;
    /**
     * A date as String
     */
    private String paramsString;

    /**
     * Remote server url
     */
    private URL url;

    private AsyncResponse delegate;

    private String response;

    private HttpURLConnection conn;

    //------------Constructors------------

    /**
     * Public constructor
     *
     * @param
     */
    public HttpRequest(String requestType, String paramsString, AsyncResponse asyncResponse)
    {
        Log.d(TAG, "Constructor:  new HttpRequestNewMap with : request = " + paramsString);
        this.paramsString = paramsString;
        this.requestType = requestType;
        this.delegate = asyncResponse;
    }


    //------------AsyncTask Implementation------------

    @Override
    protected void onPreExecute()
    {
        Log.d(TAG, "onPreExecute:  building url ....");
        try
        {
            url = new URL("http://" + SERVER_IP + ":" + HTTP_PORT + requestType);
            Log.d(TAG, "onPreExecute:  Url = " + url);
        }
        catch (MalformedURLException e)
        {
            Log.e(TAG, "Something went wrong while building the URL");
            e.printStackTrace();
        }
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params)
    {
        response = "";
        try
        {
            Log.d(TAG, "doingInBackground : connecting with server " + url);
            if (conn == null)
            {
                conn = (HttpURLConnection) url.openConnection();
            }
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            Log.d(TAG, "doingInBackground : connected successfully");

            Log.d(TAG, "doingInBackground : writing to server, request = " + paramsString);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
            writer.write(paramsString);
            writer.flush();
            writer.close();
            os.close();
            Log.d(TAG, "doingInBackground : transmission to server finished  ");
            Log.d(TAG, "doingInBackground : getting server response code...");
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK)
            {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null)
                {
                    response += line;
                }
                Log.d(TAG, "doingInBackground: server response : " + response);
            }
        }
        catch (ProtocolException e)
        {
            //This exception can occur whe calling conn.setRequestMethod()
            Log.e(TAG, "doingInBackground : couldnt connect with the server " + url);
            e.printStackTrace();
        }
        catch (IOException e)
        {
            Log.e(TAG, "doingInBackground : couldnt write to the server ");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        delegate.getHttpResponse(this.response);
    }

    public void insertMockHTTPConnection(HttpURLConnection mock)
    {
        this.conn = mock;
    }
}
