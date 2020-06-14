package de.htwBerlin.ois.serverCommunication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.htwBerlin.ois.fileStructure.RemoteFile;


/**
 * Async task that lists files hosted on FTP Remote Server
 *
 * @author morelly_t1
 * @author WilliBoelke
 */
public class FtpTaskFileListing extends AsyncTask<Void, Void, String>
{


    //------------Instance Variables------------

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
    /**
     * Log Tag
     */
    private final String TAG = getClass().getSimpleName();
    /**
     * The list to be filled with remote ohdmFiles
     */
    private ArrayList<RemoteFile> remoteFiles;
    /**
     * Implementation of the {@link  AsyncResponse} interface
     * (To be implemented when initializing this class)
     */
    private AsyncResponse delegate;
    /**
     * Context
     */
    private WeakReference<Context> context;
    private boolean includeSubDirs;
    /**
     * The path to the directory
     */
    private String path;


    private Client client;


    //------------Constructors------------

    /**
     * Public Constructor
     *
     * @param context
     * @param path
     * @param asyncResponse
     */
    public FtpTaskFileListing(Context context, String path, Client client, boolean includeSubDirs, AsyncResponse asyncResponse)
    {
        Log.d(TAG, "Constructor : new FtpTaskFileListing with : path  = " + path + " includeSubDirs = " + includeSubDirs);
        this.includeSubDirs = includeSubDirs;
        this.delegate = asyncResponse;
        this.path = path;
        this.client = client;
        this.context = new WeakReference<Context>(context);
    }


    //------------AsyncTask Implementation------------


    @Override
    protected String doInBackground(Void... params)
    {
        remoteFiles = new ArrayList<>();
        Log.d(TAG, "doingInBackground : initializing new FtpClient ");

        client.connect();
        Log.d(TAG, "doingInBackground : connected to FtpClient");

        try
        {
            if (includeSubDirs == true)
            {
                Log.d(TAG, "doingInBackground : getting all files including sub dirs...");
                remoteFiles.addAll(client.getAllFileList(path));
            }
            else
            {
                Log.d(TAG, "doingInBackground : getting all files...");
                remoteFiles.addAll(client.getFileList(path));
            }
        }
        catch (IOException e)
        {
            Log.e(TAG, "doingInBackground : something went wrong while retrieving files from the FTP Server");
            e.printStackTrace();
        }
        catch (NullPointerException e)
        {
            Log.e(TAG, "doingInBackground : something went wrong while retrieving files from the FTP Server maybe the path wasnt valid?");
            e.printStackTrace();
        }

        Log.d(TAG, "doingInBackground : finished - closing connection : ");
        client.closeConnection();
        return null;
    }

    @Override
    protected void onPostExecute(String result)
    {
        if (remoteFiles.size() == 0)
        {
            Log.e(TAG, "Server not available or empty");
        }
        else
        {
            Log.d(TAG, "Found " + remoteFiles.size() + " files from server");
            delegate.getOhdmFiles(this.remoteFiles);
        }
    }

    public ArrayList<RemoteFile> getResultList()
    {
        return this.remoteFiles;
    }
}
