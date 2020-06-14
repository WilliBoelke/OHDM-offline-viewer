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

    private RemoteFile remoteFileOne = new RemoteFile("nameOne", "pathOne", 2321l, "11.11.1111");
    private RemoteFile remoteFileTwo = new RemoteFile("nameOne", "pathOne", 2321l, "11.11.1111");
    private RemoteFile remoteFileThree = new RemoteFile("nameOne", "pathOne", 2321l, "11.11.1111");
    private RemoteFile remoteFileFour = new RemoteFile("nameOne", "pathOne", 2321l, "11.11.1111");
    private ArrayList allFiles = new ArrayList<RemoteFile>();

    private ArrayList recentFiles = new  ArrayList<RemoteFile>();

    public MockClient()
    {
        allFiles.add (remoteFileOne);
        allFiles.add(remoteFileTwo);
        allFiles.add(remoteFileThree);
        allFiles.add(remoteFileFour);
        recentFiles.add(remoteFileFour);
        recentFiles.add(remoteFileTwo);
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
        return recentFiles;
    }

    @Override
    public boolean downloadFile(String remoteFileName, String downloadPath)
    {
        return false;
    }
}
