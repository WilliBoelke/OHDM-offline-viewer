package de.htwBerlin.ois.ui.fragments;

import java.io.IOException;
import java.util.ArrayList;

import de.htwBerlin.ois.fileStructure.RemoteDirectory;
import de.htwBerlin.ois.fileStructure.RemoteFile;
import de.htwBerlin.ois.serverCommunication.Client;

/**
 * This is a "Mock" implementation used to substitute
 * FtpClient or SftpClient.
 * <p>
 * This class should be replaced with a equivalent Mockito or
 * Mockk implementation as soon as possible
 */
class MockClient implements Client
{

    private RemoteFile remoteFileOne = new RemoteFile("MapOne", "pathOne", 1234l, "11.11.1111");
    private RemoteFile remoteFileTwo = new RemoteFile("MapTwo", "pathTwo", 2345l, "11.11.1111");
    private RemoteFile remoteFileThree = new RemoteFile("MapThree", "pathThree", 3456l, "11.11.1111");
    private RemoteFile remoteFileFour = new RemoteFile("MapFour", "pathFour", 4567l, "11.11.1111");
    private RemoteFile remoteFileFive = new RemoteFile("MapFive", "pathFive", 5678l, "11.11.1111");
    private RemoteFile remoteFileSix = new RemoteFile("MapSix", "pathSix", 6789l, "11.11.1111");
    private ArrayList allFiles = new ArrayList<RemoteFile>();

    private ArrayList recentFiles = new ArrayList<RemoteFile>();

    private Boolean wait = false;
    private Boolean returnNull = false;

    public MockClient(Boolean wait, boolean returnNull)
    {
        allFiles.add(remoteFileOne);
        allFiles.add(remoteFileTwo);
        allFiles.add(remoteFileThree);
        allFiles.add(remoteFileFour);
        recentFiles.add(remoteFileFive);
        recentFiles.add(remoteFileSix);

        this.wait = wait;
        this.returnNull = returnNull;
    }

    @Override
    public int connect()
    {
        return 0;
    }

    @Override
    public boolean isConnected()
    {
        return true;
    }

    @Override
    public void closeConnection()
    {

    }

    @Override
    public ArrayList<RemoteFile> getAllFileList(String path) throws IOException
    {
        if(wait)
        {
            try
            {
                Thread.sleep(200);
                return allFiles;
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        if(returnNull)
        {
            return null;
        }
        return allFiles;
    }

    @Override
    public ArrayList<RemoteDirectory> getDirList(String path)
    {
        return null;
    }

    @Override
    public ArrayList<RemoteFile> getFileList(String path) throws IOException
    {
        if(wait)
        {
            try
            {
                Thread.sleep(200);
                return recentFiles;
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        if(returnNull)
        {
            return null;
        }
        return recentFiles;
    }

    @Override
    public boolean downloadFile(String remoteFileName, String downloadPath)
    {
        return false;
    }
}
