package de.htwBerlin.ois.FTP;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.htwBerlin.ois.FileStructure.OhdmFile;

public class FTPTaskRequestNewMap extends AsyncTask<OhdmFile, Integer, Long>
{
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm");
    private static final String TAG = "FtpTaskFileListing";

    private ArrayList<OhdmFile> ohdmFiles;
    private FTPClient ftpClient;
    private AsyncResponse delegate;
    private WeakReference<Context> context;

    @Override
    protected void onPreExecute()
    {
        //  ProgressBar progressBar = this.progressBar.get();
        // progressBar.setVisibility(View.VISIBLE);
        ftpClient = new FTPClient();
        super.onPreExecute();
    }

    @Override
    protected Long doInBackground(OhdmFile... ohdmFiles)
    {
        try
        {
            ftpClient.connect(FtpEndpointSingleton.getInstance().getServerIp(), FtpEndpointSingleton.getInstance().getServerPort());
            ftpClient.login(FtpEndpointSingleton.getInstance().getFtpUser(), FtpEndpointSingleton.getInstance().getFtpPassword());
            ftpClient.enterLocalPassiveMode();
            Log.i(TAG, "Reply Code: " + ftpClient.getReplyCode());
            boolean status = ftpClient.changeWorkingDirectory("ohdm");
            Log.i(TAG, "change working dir to ohdm: " + status);

            InputStream inStream = ftpClient.retrieveFileStream("KABQ.TXT");
            InputStreamReader isr = new InputStreamReader(inStream, StandardCharsets.UTF_8);
        }
        catch (SocketException e)
        {
            Log.e(TAG, "doInBackground, SocketException; " + e.getMessage());
        }
        catch (IOException e)
        {
            Log.e(TAG, "doInBackground, IOException; " + e.getMessage());
        }
        finally
        {
            try
            {
                if (ftpClient.isConnected())
                {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            }
            catch (IOException e)
            {
                Log.e(TAG, "Error in finally " + e.getMessage());
            }
        }
        return null;
    }
}
