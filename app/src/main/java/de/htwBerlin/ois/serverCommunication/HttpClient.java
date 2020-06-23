package de.htwBerlin.ois.serverCommunication;

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

import static de.htwBerlin.ois.models.repositories.localRepositories.Variables.HTTP_PORT;
import static de.htwBerlin.ois.models.repositories.localRepositories.Variables.SERVER_IP;

/**
 * A simple wrapper class for the {@link java.net.HttpURLConnection}
 *
 * @author WilliBoelke
 */
public class HttpClient
{

    public static final String RESPONSE_NO_CONNECTION = "noconn";
    public static final String RESPONSE_NO_REQUESTS= "[]";

    //------------Instance Variables------------

    /**
     * Log Tag
     */
    private final String TAG = this.getClass().getSimpleName();
    /**
     * Java HttpUrlConnection
     * which in the end this is a wrapper for
     */
    private HttpURLConnection conn;
    /**
     * The URL, formatted like that:
     * "http://[server_ip]:[port]+[request type]
     */
    private URL url;
    /**
     * The servers response
     */
    private String response;

    //------------Connection------------

    public int connect(String requestType)
    {
        Log.d(TAG, "connect:  building url ....");
        try
        {

            url = new URL("http://" + SERVER_IP + ":" + HTTP_PORT + requestType);
            Log.d(TAG, "connect:  Url = " + url);

            if(conn == null)
            {
                Log.d(TAG, "connect: connecting with server " + url);
                conn = (HttpURLConnection) url.openConnection();
            }
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
        }
        catch (ProtocolException e)
        {
            e.printStackTrace();
            return 1;
        }
        catch (IllegalStateException e)
        {
            Log.e(TAG, "connect : Already connected");
            e.printStackTrace();
            closeConnection();
            return connect(requestType);
        }
        catch (MalformedURLException e)
        {
            Log.e(TAG, "connect : Something went wrong while building the URL");
            e.printStackTrace();
            return 2;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return 3;
        }
        Log.d(TAG, "connect : connected successfully");
        return 0;
    }

    public void closeConnection()
    {
        conn.disconnect();
        conn = null;
    }


    //------------Request/Response------------

    /**
     * Sends the paramsString to the server
     *
     * @param paramsString
     * @return
     */
    public int sendRequest(String paramsString)
    {
        try
        {
            Log.d(TAG, "sendRequest : writing to server, request = " + paramsString);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
            writer.write(paramsString);
            writer.flush();
            writer.close();
            os.close();
            Log.d(TAG, "sendRequest : transmission to server finished");
            Log.d(TAG, "sendRequest : getting server response code...");
            retrieveResponse();
        }
        catch (ProtocolException e)
        {
            //This exception can occur whe calling conn.setRequestMethod()
            Log.e(TAG, "sendRequest : couldn't connect with the server " + url);
            e.printStackTrace();
            return 1;
        }
        catch (IOException e)
        {
            Log.e(TAG, "sendRequest : either problem to write to write to the server  or problem to get the servers response");
            e.printStackTrace();
            return 2;
        }
        return 0;
    }

    /**
     * Gets the response from the server after a request was send
     *
     * @throws IOException
     */
    private void retrieveResponse() throws IOException
    {
        response = ""; // needs to be here ...trus me
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK)
        {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = br.readLine()) != null)
            {
                response += line;
            }
            Log.d(TAG, "sendRequest: server response : " + response);
        }
        if(response == null)
        {
            response = RESPONSE_NO_CONNECTION;
        }
    }

    /**
     * Returns the servers response
     *
     * @return Server response
     */
    public String getServerResponse()
    {
        return this.response;
    }


    //------------Test------------

    /**
     * To insert a mocked {@link HttpURLConnection}
     *
     * @param mock
     */
    public void insertMockHTTPConnection(HttpURLConnection mock)
    {
        this.conn = mock;
    }
}
