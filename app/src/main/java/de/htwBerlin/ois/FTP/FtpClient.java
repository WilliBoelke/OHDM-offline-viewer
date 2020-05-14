package de.htwBerlin.ois.FTP;

import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.SocketException;

public class FtpClient
{

    private final String TAG = getClass().getSimpleName();
    private FTPClient client;
    private PrintStream logStream;
    private String LOG_TAG = "FTP Client";
    private boolean LOGGING_OVER_FILE = true;

    public FtpClient(OutputStream os)
    {
        logStream = new PrintStream(os);
    }

    public FtpClient()
    {
        LOGGING_OVER_FILE = false;
    }

    /**
     * if called, connects to a server
     *
     * @param server server ip/hostname
     * @param port   FTPServer port
     * @param user   user to log in with
     * @param pass   passwort used to log in
     * @return 0 = successful connection,
     * 1 = FTP server refused connection,
     * 2 = Could not login to FTP Server (probably wrong password),
     * 3 = Socket exception thrown, Server not found,
     * 4 = IO Exception
     */
    public int connect(String server, int port, String user, String pass)
    {
        if (client == null)
        {
            Log.i(TAG, "Getting passive FTP client");
            client = new FTPClient();

            try
            {

                client.connect(server, port);
                // After connection attempt, you should check the reply code to verify
                // success.
                int reply = client.getReplyCode();

                if (!FTPReply.isPositiveCompletion(reply))
                {
                    client.disconnect();
                    Log.e(TAG, " FTP server refused connection.");
                    return 1;
                }

                //after connecting to the server set the local passive mode
                client.enterLocalPassiveMode();

                //send username and password to login to the server
                if (!client.login(user, pass))
                {
                    Log.e(TAG, "Could not login to FTP Server");
                    return 2;
                }
            }
            catch (SocketException e)
            {
                String message = "Socket exception thrown, Server not found";
                Log.e(TAG, "ERROR :" + message + "\n" + e);
                return 3;
            }
            catch (IOException e)
            {
                String message = "IO Exception";
                Log.e(TAG, "ERROR :" + message + "\n" + e);
                return 4;
            }
        }
        return 0;
    }

    /**
     * checking if connected
     *
     * @return
     */
    public boolean isConnected()
    {
        return client.isConnected();
    }


    /**
     * gives back the File list, given in current working dir
     *
     * @return FTPFiles in current dir
     * @throws IOException couldn't read from current dir
     */
    public FTPFile[] getFileList() throws IOException
    {
        if (client == null)
        {
            Log.i(TAG, "First initialize the FTPClient by calling 'initFTPPassiveClient()");
            return null;
        }

        Log.i(TAG, "Getting file listing for current director");
        FTPFile[] files = client.listFiles("");

        return files;
    }


    /**
     * call to download a File from current dir
     *
     * @param remoteFileName file to download from Server
     * @param downloadPath   Path to write to
     * @throws IOException couldn't download from current dir
     */
    public void downloadFile(String remoteFileName, String downloadPath) throws IOException
    {
        // Download File using retrieveFile(String, OutputStream)
        OutputStream fileOutputStream = new BufferedOutputStream(new FileOutputStream(new File(downloadPath)));
        boolean success = client.retrieveFile(remoteFileName, fileOutputStream);
        fileOutputStream.close();

        if (success) Log.i(TAG, "File " + remoteFileName + " has been downloaded successfully.");
        else Log.e(TAG, "couldn't download " + remoteFileName + "from server!");

    }

    /**
     * closes connection
     * pls always use at the end !!!!
     */
    public void closeConnection()
    {
        if (client == null)
        {
            Log.i(TAG, "Nothing to close, the FTPClient wasn't initialized");
            return;
        }
        //be polite and logout & close the connection before the application finishes
        try
        {
            client.logout();
            client.disconnect();
        }
        catch (IOException e)
        {
            Log.e(TAG, "Could not logout");
        }
    }
}
