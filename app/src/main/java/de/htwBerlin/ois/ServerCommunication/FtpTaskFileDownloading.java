package de.htwBerlin.ois.ServerCommunication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.IOException;
import java.lang.ref.WeakReference;

import de.htwBerlin.ois.FileStructure.OhdmFile;

import static de.htwBerlin.ois.ServerCommunication.Variables.FTP_Port;
import static de.htwBerlin.ois.ServerCommunication.Variables.SERVER_IP;
import static de.htwBerlin.ois.ServerCommunication.Variables.USER_NAME;
import static de.htwBerlin.ois.ServerCommunication.Variables.USER_PASSWORD;

/**
 * Asynctask that downloads files from FTP Remote server
 *
 * @author morelly_t1
 * @author WilliBoelke
 */
public class FtpTaskFileDownloading extends AsyncTask<OhdmFile, Integer, Long>
{
    private final String TAG = getClass().getSimpleName();
    private WeakReference<Context> context;
    private FtpClient ftpClient;

    public FtpTaskFileDownloading(Context context)
    {
        this.context = new WeakReference<Context>(context);
    }

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
            ftpClient.downloadFile(ohdmFile[0].getFilename(), ohdmFile[0].getFilename());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Long params)
    {
        Context context = this.context.get();
        Toast.makeText(context, "Download Finished!", Toast.LENGTH_SHORT).show();
    }
}
