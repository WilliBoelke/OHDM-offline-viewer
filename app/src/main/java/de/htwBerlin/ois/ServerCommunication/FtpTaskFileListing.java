package de.htwBerlin.ois.ServerCommunication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.htwBerlin.ois.FileStructure.OhdmFile;

/**
 * Async task that lists files hosted on FTP Remote Server
 *
 * @author morelly_t1
 * @author  WilliBoelke
 */
public class FtpTaskFileListing extends AsyncTask<Void, Void, String>
{


    //------------Instance Variables------------

    /**
     * Log Tag
     */
    private  final String TAG = getClass().getSimpleName();
    /**
     *The list to be filled with remote ohdmFiles
     */
    private ArrayList<OhdmFile> ohdmFiles;
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


    //------------Static Variables------------

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm");


    //------------Constructors------------

    /**
     * Public Constructor
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
        ohdmFiles = new ArrayList<>();
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
            OhdmFile ohdm = new OhdmFile(ftpFile.getName(), (ftpFile.getSize() / 1024), sdf.format(date.getTime()), Boolean.FALSE);
            ohdmFiles.add(ohdm);
            Log.i(TAG, ohdm.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result)
    {
        Context context = this.context.get();
        if (ohdmFiles.size() == 0)
            Toast.makeText(context, "Download Service not available", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Found " + ohdmFiles.size() + " maps!", Toast.LENGTH_SHORT).show();
        delegate.getOhdmFiles(this.ohdmFiles);
    }
}
