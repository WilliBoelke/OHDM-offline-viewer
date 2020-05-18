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
    private WeakReference<Context> context;
    private FtpClient ftpClient;


    //------------Constructors------------

    public FtpTaskFileDownloading(Context context)
    {
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
        Context context = this.context.get();
        ftpClient = new FtpClient();
        ftpClient.connect();
        Toast.makeText(context, "Download Started", Toast.LENGTH_SHORT).show();
        try
        {
            ftpClient.downloadFile(ohdmFile[0].getFilename(), ohdmFile[0].getFilename());

        }
        catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(context, "Download Interrupted", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Long params)
    {
        Context context = this.context.get();
        Toast.makeText(context, "Download Finished", Toast.LENGTH_SHORT).show();
    }
}
