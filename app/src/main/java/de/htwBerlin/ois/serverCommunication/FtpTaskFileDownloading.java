package de.htwBerlin.ois.serverCommunication;

import android.os.AsyncTask;
import android.util.Log;

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
    private SftpClient sftpClient;


    //------------Constructors------------

    public FtpTaskFileDownloading()
    {
        Log.d(TAG, "Constructor : new FtpTaskFileDownloading with");
    }


    //------------AsyncTask Implementation------------

    @Override
    protected Long doInBackground(RemoteFile[] remoteFile)
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
        sftpClient.downloadFile(remoteFile[0].getFilename(), remoteFile[0].getPath());
        Log.d(TAG, "doingInBackground :  download finished successfully");

        Log.d(TAG, "doingInBackground :  closing server connection");
        sftpClient.closeConnection();
        return null;
    }

    @Override
    protected void onPostExecute(Long params)
    {
        // Context context = this.context.get();
        //  TODO
        // Toast.makeText(context, "Download Finished", Toast.LENGTH_SHORT).show();
    }

    public void insertMockSftpClient(SftpClient mockSftpClient)
    {
        this.sftpClient = mockSftpClient;
    }
}
