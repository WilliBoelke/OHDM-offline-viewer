package de.htwBerlin.ois.FTP;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.lang.ref.WeakReference;

import de.htwBerlin.ois.FileStructure.OhdmFile;

import static de.htwBerlin.ois.FTP.Variables.FTP_Port;
import static de.htwBerlin.ois.FTP.Variables.SERVER_IP;
import static de.htwBerlin.ois.FTP.Variables.USER_NAME;
import static de.htwBerlin.ois.FTP.Variables.USER_PASSWORD;

/**
 * Asynctask that downloads files from FTP Remote server
 *
 * @author morelly_t1
 */
public class FtpTaskFileDownloading extends AsyncTask<OhdmFile, Integer, Long>
{

    private static final String TAG = "FtpTaskFileListing";
    private static final String MAP_FILE_PATH = Environment.getExternalStorageDirectory().toString() + "/OHDM";

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
        ftpClient.connect(SERVER_IP, FTP_Port, USER_NAME, USER_PASSWORD);
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
    protected void onProgressUpdate(Integer... params)
    {
        Log.i(TAG, "Download progress :" + params[0] + "%");
        //if (this.progressBar.get() != null) this.progressBar.get().setProgress(params[0]);
    }

    @Override
    protected void onPostExecute(Long params)
    {
        Context context = this.context.get();
        Toast.makeText(context, "Download Finished!", Toast.LENGTH_SHORT).show();
    }
}
