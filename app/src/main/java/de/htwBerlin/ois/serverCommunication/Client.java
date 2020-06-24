package de.htwBerlin.ois.serverCommunication;


import java.io.IOException;
import java.util.ArrayList;

import de.htwBerlin.ois.model.models.fileStructure.RemoteDirectory;
import de.htwBerlin.ois.model.models.fileStructure.RemoteFile;

public interface Client
{

    //------------Connection------------

    int connect();

    boolean isConnected();

    void closeConnection();


    //------------Listing------------

    ArrayList<RemoteFile> getAllFileList(String path) throws IOException;

    ArrayList<RemoteDirectory> getDirList(String path);

    /**
     * @param path
     * @return
     * @throws IOException
     */
    ArrayList<RemoteFile> getFileList(String path) throws IOException;


    //------------Downloading------------

    boolean downloadFile(String remoteFileName, String downloadPath);

}
