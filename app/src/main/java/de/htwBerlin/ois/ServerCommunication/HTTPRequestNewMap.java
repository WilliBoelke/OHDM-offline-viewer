package de.htwBerlin.ois.ServerCommunication;

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

import static de.htwBerlin.ois.ServerCommunication.Variables.HTTP_PORT;
import static de.htwBerlin.ois.ServerCommunication.Variables.SERVER_IP;


/**
 * AsyncTask to make a HTTP Request to the Server
 * <p>
 * The request contains name, coordinates and a date.
 * The server will the create that map and make it available
 *
 * @author WilliBoelke
 */
public class HTTPRequestNewMap extends AsyncTask<Void, Void, String>
{


    //------------Instance Variables------------

    /**
     * Log tag
     */
    private final String TAG = this.getClass().getSimpleName();
    /**
     * A date as String
     */
    private String date;
    /**
     * 8 Coordinates (Lat/Long) as String
     */
    private String coordinates;
    /**
     * map name
     */
    private String name;
    /**
     * Remote server url
     */
    private URL url;


    //------------Constructors------------

    /**
     * Public constructor
     *
     * @param date
     * @param coordinates
     * @param name
     */
    public HTTPRequestNewMap(String date, String coordinates, String name)
    {
        Log.d(TAG, "Constructor:  new GttpRequestNewMap with : date = " + date + " coords = " + coordinates + " name = " + name);
        this.date = date;
        this.name = name;
        this.coordinates = coordinates;
    }


    //------------AsyncTask Implementation------------

    @Override
    protected void onPreExecute()
    {
        Log.d(TAG, "onPreExecute:  building url ....");
        try
        {
            url = new URL("http://" + SERVER_IP + ":" + HTTP_PORT + "/request");
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
        String response = null;
        try
        {
            Log.d(TAG, "doingInBackground : connecting with server " + url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            Log.d(TAG, "doingInBackground : connected successfully");

            Log.d(TAG, "doingInBackground : writing to server, request = " + this.buildParamsString());
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
            writer.write(this.buildParamsString());
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
            Log.e(TAG, "doingInBackground : couldnt connect with the server " + url );
            e.printStackTrace();
        }
        catch (IOException e)
        {
            Log.e(TAG, "doingInBackground : couldnt write to the server " );
            e.printStackTrace();
        }
        return null;
    }


    //------------Others------------

    /**
     * Builds a string as accepted by the server
     * example:
     * name=mapname&coords=13.005,15.123_13.005,15.123_13.005,15.123_13.005,15.123_13.005,15.123&date=2117-12-11
     *
     * @return the String
     */
    private String buildParamsString()
    {
        Log.e(TAG, "buildParamsString : building params string...");
        StringBuilder sb = new StringBuilder();
        sb.append("name=");
        sb.append(this.name);
        sb.append("&coords=");
        sb.append(this.coordinates);
        sb.append("&date=");
        sb.append(this.date);
        Log.e(TAG, "buildParamsString : builded params string");
        return sb.toString();
    }

}
