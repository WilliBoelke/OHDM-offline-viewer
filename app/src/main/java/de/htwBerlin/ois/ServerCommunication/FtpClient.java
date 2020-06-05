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
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.htwBerlin.ois.FileStructure.RemoteFile;

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
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");


    //------------Constructors-----------

    /**
     * Empty constructor
     * Used within the "real" code
     */
    protected FtpClient()
    {
        this.client = new FTPClient();
        Log.d(TAG, "Constructor : new FtpClient ");
    }

    /**
     * Constructor which takes a apache FTPClient
     * used for testing by inserting a mocked FTPClient
     */
    protected FtpClient(FTPClient mockClient)
    {
        this.client = mockClient;
        Log.d(TAG, "Constructor : new FtpClient ");
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
     * 5 = if already connected
     */
   protected int connect()
    {
        Log.d(TAG, "connect : connecting to ftp client...");
        if (! client.isConnected())
        {
            Log.d(TAG, "connect : getting passive FTP client");
            try
            {
                Log.d(TAG, "connect : connecting to " + SERVER_IP + " : " + FTP_Port);
                client.connect(SERVER_IP, FTP_Port);
                // After connection attempt, you should check the reply code to verify
                // success.
                int reply = client.getReplyCode();

                if (!FTPReply.isPositiveCompletion(reply))
                {
                    client.disconnect();
                    Log.e(TAG, "connect : FTP server refused connection.");
                    return 1;
                }
                else
                {
                    Log.d(TAG, "connect : connected successfully");
                }

                //after connecting to the server set the local passive mode
                client.enterLocalPassiveMode();
                //send username and password to login to the server
                Log.d(TAG, "connect : trying to log in ...");
                if (!client.login(USER_NAME, USER_PASSWORD))
                {
                    Log.e(TAG, "connect : Could not login to FTP Server");
                    return 2;
                }
                else
                {
                    Log.d(TAG, "connect : log in successful");
                }
                client.setFileType(FTP.BINARY_FILE_TYPE);
            }
            catch (SocketException e)
            {
                Log.e(TAG, "connect : socket exception thrown, Server not found" + "\n" + e);
                return 3;
            }
            catch (IOException e)
            {
                Log.e(TAG, "IO Exception" + "\n" + e);
                return 4;
            }
        }
        else
        {
            Log.d(TAG, "was already connected ");
            this.closeConnection();
            this.connect();
        }
        Log.d(TAG, "connect : successfully connected");
        return 0;
    }

    /**
     * checking if connected
     *
     * @return true if connected, else false
     */
    boolean isConnected()
    {
        return client.isConnected();
    }

    /**
     * closes connection
     * pls always use at the end !!!!
     */
    protected void closeConnection()
    {
        Log.d(TAG, "closeConnection : trying to close connection with " + SERVER_IP + " : " + FTP_Port);
        if (!client.isConnected())
        {
            Log.d(TAG, "closeConnection : nothing to close, the FTPClient wasn't initialized");
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
            Log.e(TAG, "closeConnection : Could not logout");
        }
        Log.d(TAG, "closeConnection : log out successful");
    }


    //------------Listing-----------

    /**
     * Returns a list of all directories in <code>path</code>
     *
     * @param path the path
     * @return all directories
     * @throws IOException
     */
    protected FTPFile[] getDirList(String path) throws IOException
    {
        if (!client.isConnected())
        {
            Log.e(TAG, "getDirList : wasnt connected to server, call connect() first");
            return null;
        }
        Log.d(TAG, " getDirList : getting file list for " + path + " ...");
        FTPFile[] files = client.listFiles(path);
        ArrayList<FTPFile> dirList = new ArrayList<>();
        for (FTPFile f : files)
        {
            if (f.isDirectory()) dirList.add(f);
        }
        Log.d(TAG, " getDirList : got " + dirList.size() + "dirs  from " + path);
        return dirList.toArray(new FTPFile[dirList.size()]);
    }

    /**
     * gives back the File list, given in path dir
     * ignores sub dirs completely
     *
     * @return FTPFiles in current dir
     * @throws IOException couldn't read from current dir
     */
    protected FTPFile[] getFileList(String path) throws IOException
    {
        if (!client.isConnected())
        {
            Log.e(TAG, "getFileList : wasnt connected to server, call connect() first");
            return null;
        }

        Log.d(TAG, " getFileList : getting file list for " + path + " ...");
        FTPFile[] filesAndDirs = client.listFiles(path);
        ArrayList<FTPFile> files = new ArrayList<>();

        for (FTPFile f : filesAndDirs)
        {
            if (!f.isDirectory())
            {
                files.add(f);
            }
        }
        Log.d(TAG, " getFileList : got " + files.size() + "files from " + path);
        return files.toArray(new FTPFile[files.size()]);
    }

    /**
     * gives back the File list, given in path dir
     * including files from sub dirs
     *
     * @return FTPFiles in current dir
     * @throws IOException couldn't read from current dir
     */
    protected ArrayList<RemoteFile> getAllFileList(String path) throws IOException
    {
        if (!client.isConnected())
        {
            Log.e(TAG, "getAllFileList : wasnt connected to server, call connect() first");
            return null;
        }
        Log.d(TAG, " getAllFileList : getting file list for " + path + " ...");
        FTPFile[] filesAndDirs = client.listFiles(path);
        ArrayList<RemoteFile> files = new ArrayList<>();
        ArrayList<FTPFile> dirs = new ArrayList<>();

        for (FTPFile f : filesAndDirs)
        {
            if (!f.isDirectory())
            {
                Date date = f.getTimestamp().getTime();
                RemoteFile ohdm = new RemoteFile(f.getName(), path,  (f.getSize() / 1024), sdf.format(date.getTime()));
                files.add(ohdm);
            }
            else
            {
                dirs.add(f);
            }
        }

        for (FTPFile d : dirs)
        {
            String subPath = path + "/" + d.getName();
            ArrayList<RemoteFile> subFiles = getAllFileList(subPath);

            for (RemoteFile f : subFiles)
            {
                files.add(f);
            }
        }
        Log.d(TAG, " getAllFileList : got " + files.size() + "files from " + path);
        return files;
    }

    //------------Downloading-----------

    /**
     * call to download a File from current dir
     *
     * @param remoteFileName file to download from Server
     * @param downloadPath   Path to write to
     * @throws IOException couldn't download from current dir
     */
    protected boolean downloadFile(String remoteFileName, String downloadPath) throws IOException
    {
        if (!client.isConnected())
        {
            Log.e(TAG, "downloadFile : wasnt connected to server, call connect() first");
            return false;
        }
        Log.d(TAG, "downloadFile : trying to download " + remoteFileName + downloadPath);
        File downloadFile = new File(MAP_FILE_PATH, remoteFileName);
        client.changeWorkingDirectory(downloadPath);
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

        return true;
    }


}
