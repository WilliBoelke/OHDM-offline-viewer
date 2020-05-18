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

import static de.htwBerlin.ois.ServerCommunication.Variables.FTP_Port;
import static de.htwBerlin.ois.ServerCommunication.Variables.SERVER_IP;
import static de.htwBerlin.ois.ServerCommunication.Variables.USER_NAME;
import static de.htwBerlin.ois.ServerCommunication.Variables.USER_PASSWORD;

/**
 * Async task that lists files hosted on FTP Remote Server
 *
 * @author morelly_t1
 */
public class FtpTaskFileListing extends AsyncTask<Void, Void, String>
{

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm");
    private static final String TAG = "FtpTaskFileListing";

    private ArrayList<OhdmFile> ohdmFiles;
    private AsyncResponse delegate;
    private WeakReference<Context> context;
    private FtpClient ftpClient;

    public FtpTaskFileListing(AsyncResponse asyncResponse, Context context)
    {
        this.delegate = asyncResponse;
        this.context = new WeakReference<Context>(context);
    }

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
        ftpClient = new FtpClient();
        ftpClient.connect();
        FTPFile[] files = new FTPFile[0];
        try
        {
            files = ftpClient.getFileList();
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
