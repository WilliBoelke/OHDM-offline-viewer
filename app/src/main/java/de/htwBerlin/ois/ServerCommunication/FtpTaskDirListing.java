package de.htwBerlin.ois.ServerCommunication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.htwBerlin.ois.FileStructure.RemoteDirectory;

/**
 * Async task that lists directories hosted on FTP Remote Server
 *
 * @author WilliBoelke
 */
public class FtpTaskDirListing extends AsyncTask<Void, Void, String>
{


    //------------Instance Variables------------

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
    /**
     * Log Tag
     */
    private final String TAG = getClass().getSimpleName();
    /**
     * The list to be filled with remote directories
     */
    private ArrayList<RemoteDirectory> directoryList;
    /**
     * Implementation of the {@link  AsyncResponse} interface
     * (To be implemented when initializing this class)
     */
    private AsyncResponse delegate;
    /**
     * Context
     */
    private WeakReference<Context> context;
    /**
     * The path to the directory
     */
    private String path;


    //------------Constructors------------

    /**
     * Public Constructor
     *
     * @param context
     * @param path
     * @param asyncResponse
     */
    public FtpTaskDirListing(Context context, String path, AsyncResponse asyncResponse)
    {
        Log.d(TAG, "Constructor : new FtpTaskDirListing with : path  = " + path);
        this.delegate = asyncResponse;
        this.path = path;
        this.context = new WeakReference<Context>(context);
    }


    //------------AsyncTask Implementation------------

    @Override
    protected String doInBackground(Void... params)
    {
        directoryList = new ArrayList<>();
        Log.d(TAG, "doingInBackground : initializing new FtpClient ");
        FtpClient ftpClient = new FtpClient();
        ftpClient.connect();
        Log.d(TAG, "doingInBackground : connected to FtpClient");

        FTPFile[] files = new FTPFile[0];
        try
        {
            Log.d(TAG, "doingInBackground : trying to get directories");
            files = ftpClient.getDirList(path);
        }
        catch (IOException e)
        {
            Log.e(TAG, "doingInBackground : something went wrong while while retrieving the directories");
            e.printStackTrace();
        }

        for (FTPFile ftpFile : files)
        {
            Date date = ftpFile.getTimestamp().getTime();
            RemoteDirectory dir = new RemoteDirectory(ftpFile.getName(), sdf.format(date.getTime()));
            directoryList.add(dir);
            Log.d(TAG, "doingInBackground : got dir " + dir.toString());
        }
        Log.d(TAG, "doingInBackground :  closing server connection");
        ftpClient.closeConnection();
        return null;
    }

    @Override
    protected void onPostExecute(String result)
    {
        if (directoryList.size() == 0)
        {
            Log.e(TAG, "Server not available or empty");
        }
        else
        {
            Log.i(TAG, "Found " + directoryList.size() + " directories");
            delegate.getRemoteDirectories(this.directoryList);
        }
    }
}