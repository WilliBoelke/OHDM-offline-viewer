package de.htwBerlin.ois.serverCommunication;

import java.util.ArrayList;

import de.htwBerlin.ois.fileStructure.RemoteDirectory;
import de.htwBerlin.ois.fileStructure.RemoteFile;

public interface AsyncResponse
{
    void getRemoteFiles(ArrayList<RemoteFile> remoteFiles);

    void getRemoteDirectories(ArrayList<RemoteDirectory> remoteDirectories);

    void getHttpResponse(String response);
}
