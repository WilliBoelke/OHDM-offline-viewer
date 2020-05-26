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

import de.htwBerlin.ois.FileStructure.RemoteFile;

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


    //------------Static Variables------------
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
    public FtpTaskFileListing(Context context, String path, AsyncResponse asyncResponse)
    {
        this.delegate = asyncResponse;
        this.path = path;
        this.context = new WeakReference<Context>(context);
    }


    //------------AsyncTask Implementation------------

    @Override
    protected void onPreExecute()
    {
        Log.i(TAG, "onPreExecute: ");
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params)
    {
        remoteFiles = new ArrayList<>();
        FtpClient ftpClient = new FtpClient();
        ftpClient.connect();

        FTPFile[] files = new FTPFile[0];
        try
        {
            files = ftpClient.getFileList(path);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        for (FTPFile ftpFile : files)
        {
            Date date = ftpFile.getTimestamp().getTime();
            RemoteFile ohdm = new RemoteFile(ftpFile.getName(), (ftpFile.getSize() / 1024), sdf.format(date.getTime()), Boolean.FALSE);
            remoteFiles.add(ohdm);
            Log.i(TAG, ohdm.toString());
        }

        ftpClient.closeConnection();
        return null;
    }

    @Override
    protected void onPostExecute(String result)
    {
        if (remoteFiles.size() == 0) Log.e(TAG, "Server not available or empty");
        else Log.i(TAG, "Found " + remoteFiles.size() + " directories");
        delegate.getOhdmFiles(this.remoteFiles);
    }
}
