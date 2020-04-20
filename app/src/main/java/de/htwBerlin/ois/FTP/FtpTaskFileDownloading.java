package de.htwBerlin.ois.FTP;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.SocketException;

import de.htwBerlin.ois.FileStructure.OhdmFile;

/**
 * Asynctask that downloads files from FTP Remote server
 *
 * @author morelly_t1
 */
public class FtpTaskFileDownloading extends AsyncTask<OhdmFile, Integer, Long> {

    private static final String TAG = "FtpTaskFileListing";
    private static final String MAP_FILE_PATH = Environment.getExternalStorageDirectory().toString() + "/OHDM";

    private WeakReference<ProgressBar> progressBar;
    private WeakReference<Context> context;
    private FTPClient ftpClient;

    public FtpTaskFileDownloading(ProgressBar progressbar, Context context) {
        this.progressBar = new WeakReference<ProgressBar>(progressbar);
        this.context = new WeakReference<Context>(context);
    }

    @Override
    protected void onPreExecute() {
        ProgressBar progressBar = this.progressBar.get();
        progressBar.setVisibility(View.VISIBLE);
        ftpClient = new FTPClient();
        super.onPreExecute();
    }

    @Override
    protected Long doInBackground(OhdmFile... ohdmFile) {

        try {
            ftpClient.connect(FtpEndpointSingleton.getInstance().getServerIp(), FtpEndpointSingleton.getInstance().getServerPort());
            ftpClient.login(FtpEndpointSingleton.getInstance().getFtpUser(), FtpEndpointSingleton.getInstance().getFtpPassword());
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            Log.i(TAG, "Reply Code: " + ftpClient.getReplyCode());

            boolean status = ftpClient.changeWorkingDirectory("ohdm");
            Log.i(TAG, "change working dir to ohdm: " + status);

            File downloadFile = new File(MAP_FILE_PATH, ohdmFile[0].getFilename());
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
            InputStream inputStream = ftpClient.retrieveFileStream(ohdmFile[0].getFilename());
            byte[] bytesArray = new byte[4096];

            long total = 0;
            int bytesRead;
            double progress;

            while (-1 != (bytesRead = inputStream.read(bytesArray))) {
                total += bytesRead;
                progress = ((total * 100) / (ohdmFile[0].getFileSize() * 1024));
                outputStream.write(bytesArray, 0, bytesRead);
                Log.i(TAG, "Download progress " + (int) progress);
                publishProgress((int) progress);
            }

            if (ftpClient.completePendingCommand()) Log.i(TAG, "File Download successful");

            outputStream.close();
            inputStream.close();

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
    protected void onProgressUpdate(Integer... params) {
        if (this.progressBar.get() != null) this.progressBar.get().setProgress(params[0]);
    }

    @Override
    protected void onPostExecute(Long params) {
        Context context = this.context.get();
        Toast.makeText(context, "Download Finished!", Toast.LENGTH_SHORT).show();
    }
}
