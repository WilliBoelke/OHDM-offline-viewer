package de.htwBerlin.ois.FTP;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.htwBerlin.ois.FileStructure.OhdmFile;

/**
 * Async task that lists files hosted on FTP Remote Server
 *
 * @author morelly_t1
 */
public class FtpTaskFileListing extends AsyncTask<Void, Void, String> {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm");
    private static final String TAG = "FtpTaskFileListing";

    private ArrayList<OhdmFile> ohdmFiles;
    private FTPClient ftpClient;
    private AsyncResponse delegate;
    private WeakReference<Context> context;

    public FtpTaskFileListing(AsyncResponse asyncResponse, Context context) {
        this.delegate = asyncResponse;
        this.context = new WeakReference<Context>(context);
    }

    @Override
    protected void onPreExecute() {
        ftpClient = new FTPClient();
        ohdmFiles = new ArrayList<>();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {

        try {
            ftpClient.connect(FtpEndpointSingleton.getInstance().getServerIp(), FtpEndpointSingleton.getInstance().getServerPort());
            ftpClient.login(FtpEndpointSingleton.getInstance().getFtpUser(), FtpEndpointSingleton.getInstance().getFtpPassword());
            ftpClient.enterLocalPassiveMode();
            Log.i(TAG, "Reply Code: " + ftpClient.getReplyCode());

            boolean status = ftpClient.changeWorkingDirectory("ohdm");
            Log.i(TAG, "change working dir to ohdm: " + status);


            for (FTPFile ftpFile : ftpClient.listFiles()) {
                Date date = ftpFile.getTimestamp().getTime();
                OhdmFile ohdm = new OhdmFile(ftpFile.getName(), (ftpFile.getSize() / 1024), sdf.format(date.getTime()), Boolean.FALSE);
                ohdmFiles.add(ohdm);
                Log.i(TAG, ohdm.toString());
            }

        } catch (SocketException e) {
            Log.e(TAG, "doInBackground, SocketException; " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground, IOException; " + e.getMessage());
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error in finally " + e.getMessage());
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... params) {
    }

    @Override
    protected void onPostExecute(String result) {
        Context context = this.context.get();
        if (ohdmFiles.size() == 0 )  Toast.makeText(context, "Download Service not available", Toast.LENGTH_SHORT).show();
        else                        Toast.makeText(context, "Found " + ohdmFiles.size()  + " maps!", Toast.LENGTH_SHORT).show();
        delegate.getOhdmFiles(this.ohdmFiles);
    }
}
