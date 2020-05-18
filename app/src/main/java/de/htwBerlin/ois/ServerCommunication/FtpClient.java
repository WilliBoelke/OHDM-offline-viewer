package de.htwBerlin.ois.ServerCommunication;

import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.SocketException;
import java.util.ArrayList;

import static de.htwBerlin.ois.MainActivity.MainActivity.MAP_FILE_PATH;
import static de.htwBerlin.ois.ServerCommunication.Variables.FTP_Port;
import static de.htwBerlin.ois.ServerCommunication.Variables.SERVER_IP;
import static de.htwBerlin.ois.ServerCommunication.Variables.USER_NAME;
import static de.htwBerlin.ois.ServerCommunication.Variables.USER_PASSWORD;

/**
 * Wraps the apache FTPClient
 *
 * @author NoteFox
 * @author WilliBoelke
 */
public class FtpClient
{

    //------------Instance Variables------------

    private final String TAG = getClass().getSimpleName();
    private FTPClient client;
    private PrintStream logStream;
    private boolean LOGGING_OVER_FILE = true;


    //------------Constructors-----------

    public FtpClient(OutputStream os)
    {
        logStream = new PrintStream(os);
    }

    public FtpClient()
    {
        LOGGING_OVER_FILE = false;
    }


    //------------Connection-----------

    /**
     * if called, connects to a server
     *
     * @return 0 = successful connection,
     * 1 = FTP server refused connection,
     * 2 = Could not login to FTP Server (probably wrong password),
     * 3 = Socket exception thrown, Server not found,
     * 4 = IO Exception
     */
    public int connect()
    {
        if (client == null)
        {
            Log.i(TAG, "Getting passive FTP client");
            client = new FTPClient();

            try
            {
                client.connect(SERVER_IP, FTP_Port);
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
                if (!client.login(USER_NAME, USER_PASSWORD))
                {
                    Log.e(TAG, "Could not login to FTP Server");
                    return 2;
                }
                client.setFileType(FTP.BINARY_FILE_TYPE);
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


    //------------Listing-----------

    public FTPFile[] getDirList(String path) throws IOException
    {
        if (client == null)
        {
            return null;
        }
        Log.i(TAG, " Getting file listing for current director");
        FTPFile[] files = client.listFiles(path);
        ArrayList<FTPFile> dirList = new ArrayList<>();
        for (FTPFile f : files)
        {
            if (f.isDirectory()) dirList.add(f);
        }
        return dirList.toArray(new FTPFile[dirList.size()]);
    }

    /**
     * gives back the File list, given in current working dir
     *
     * @return FTPFiles in current dir
     * @throws IOException couldn't read from current dir
     */
    public FTPFile[] getFileList(String path) throws IOException
    {
        if (client == null)
        {
            Log.i(TAG, "First initialize the FTPClient by calling 'initFTPPassiveClient()");
            return null;
        }

        Log.i(TAG, "Getting file listing for current director");
        FTPFile[] filesAndDirs = client.listFiles(path);
        ArrayList<FTPFile> files = new ArrayList<>();

        for (FTPFile f : filesAndDirs)
        {
            if (!f.isDirectory())
            {
                files.add(f);
            }
        }

        return files.toArray(new FTPFile[files.size()]);
    }


    //------------Downloading-----------

    /**
     * call to download a File from current dir
     *
     * @param remoteFileName file to download from Server
     * @param downloadPath   Path to write to
     * @throws IOException couldn't download from current dir
     */
    public void downloadFile(String remoteFileName, String downloadPath) throws IOException
    {
        File downloadFile = new File(MAP_FILE_PATH, remoteFileName);
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
        InputStream inputStream = client.retrieveFileStream(remoteFileName);
        byte[] bytesArray = new byte[4096];

        long total = 0;
        int bytesRead;
        double progress;

        while (-1 != (bytesRead = inputStream.read(bytesArray)))
        {
            total += bytesRead;
            progress = ((total * 100) / (23 * 1024));
            outputStream.write(bytesArray, 0, bytesRead);
            Log.i(TAG, "Download progress " + (int) progress);
        }

        if (client.completePendingCommand()) Log.i(TAG, "File Download successful");

        outputStream.close();
        inputStream.close();
    }



}
