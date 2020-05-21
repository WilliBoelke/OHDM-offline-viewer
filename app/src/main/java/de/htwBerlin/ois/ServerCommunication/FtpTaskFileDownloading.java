package de.htwBerlin.ois.ServerCommunication;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.lang.ref.WeakReference;

import de.htwBerlin.ois.FileStructure.OhdmFile;

/**
 * Asynctask that downloads files from FTP Remote server
 *
 * @author morelly_t1
 * @author WilliBoelke
 */
public class FtpTaskFileDownloading extends AsyncTask<OhdmFile, Integer, Long>
{

    //------------Instance Variables------------

    private final String TAG = getClass().getSimpleName();
    private final String path;
    private WeakReference<Context> context;
    private FtpClient ftpClient;


    //------------Constructors------------

    public FtpTaskFileDownloading(Context context, String path)
    {
        this.path = path;
        this.context = new WeakReference<Context>(context);
    }


    //------------AsyncTask Implementation------------

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected Long doInBackground(OhdmFile... ohdmFile)
    {
        ftpClient = new FtpClient();
        ftpClient.connect();
        try
        {
            ftpClient.downloadFile(ohdmFile[0].getFilename(), this.path);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
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
