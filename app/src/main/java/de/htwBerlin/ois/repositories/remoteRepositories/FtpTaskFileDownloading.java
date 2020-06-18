package de.htwBerlin.ois.repositories.remoteRepositories;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import de.htwBerlin.ois.models.fileStructure.RemoteFile;

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
    private WeakReference<Context> context;
    private SftpClient sftpClient;


    //------------Constructors------------

    public FtpTaskFileDownloading(Context context)
    {
        Log.d(TAG, "Constructor : new FtpTaskFileDownloading with");
        this.context = new WeakReference(context);
    }


    //------------AsyncTask Implementation------------

    @Override
    protected Long doInBackground(RemoteFile[] ohdmFile)
    {
        Log.d(TAG, "doingInBackground : initializing new FtpClient ");
        if (sftpClient == null)
        {
            //in case a mock object was inserted before that
            sftpClient = new SftpClient();
        }
        sftpClient.connect();
        Log.d(TAG, "doingInBackground : connected to FtpClient");

        Log.d(TAG, "doingInBackground : starting file download...");
        sftpClient.downloadFile(ohdmFile[0].getFilename(), ohdmFile[0].getPath());
        Log.d(TAG, "doingInBackground :  download finished successfully");

        Log.d(TAG, "doingInBackground :  closing server connection");
        sftpClient.closeConnection();
        return null;
    }

    @Override
    protected void onPostExecute(Long params)
    {
        Context context = this.context.get();
        Toast.makeText(context, "Download Finished", Toast.LENGTH_SHORT).show();
    }

    public void insertMockSftpClient(SftpClient mockSftpClient)
    {
        this.sftpClient = mockSftpClient;
    }
}
