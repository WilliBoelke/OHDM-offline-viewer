package de.htwBerlin.ois.ServerCommunication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.lang.ref.WeakReference;

import de.htwBerlin.ois.FileStructure.RemoteFile;

/**
 * Asynctask that downloads files from FTP Remote server
 *
 * @author morelly_t1
 * @author WilliBoelke
 */
public class FtpTaskFileDownloading extends AsyncTask<RemoteFile, Integer, Long>
{

    //------------Instance Variables------------

    private final String TAG = getClass().getSimpleName();
    private final String path;
    private WeakReference<Context> context;
    private FtpClient ftpClient;


    //------------Constructors------------

    public FtpTaskFileDownloading(Context context, String path)
    {
        Log.d(TAG, "Constructor : new FtpTaskFileDownloading with : path  = " + path);
        this.path = path;
        this.context = new WeakReference<Context>(context);
    }


    //------------AsyncTask Implementation------------

    @Override
    protected Long doInBackground(RemoteFile... ohdmFile)
    {
        Log.d(TAG, "doingInBackground : initializing new FtpClient ");
        ftpClient = new FtpClient();
        ftpClient.connect();
        Log.d(TAG, "doingInBackground : connected to FtpClient");
        try
        {
            Log.d(TAG, "doingInBackground : starting file download...");
            ftpClient.downloadFile(ohdmFile[0].getFilename(), this.path);
            Log.d(TAG, "doingInBackground :  download finished successfully");
        }
        catch (IOException e)
        {
            Log.d(TAG, "doingInBackground :  something went wrong while downloading the file");
            e.printStackTrace();
        }
        Log.d(TAG, "doingInBackground :  closing server connection");
        ftpClient.closeConnection();
        return null;
    }

    @Override
    protected void onPostExecute(Long params)
    {
        Context context = this.context.get();
        Toast.makeText(context, "Download Finished", Toast.LENGTH_SHORT).show();
    }
}
