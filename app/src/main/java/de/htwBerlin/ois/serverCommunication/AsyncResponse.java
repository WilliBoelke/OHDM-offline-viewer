package de.htwBerlin.ois.serverCommunication;

import java.util.ArrayList;

import de.htwBerlin.ois.model.models.fileStructure.RemoteDirectory;
import de.htwBerlin.ois.model.models.fileStructure.RemoteFile;

public interface AsyncResponse
{
    void getRemoteFiles(ArrayList<RemoteFile> remoteFiles);

    void getRemoteDirectories(ArrayList<RemoteDirectory> remoteDirectories);

    void getHttpResponse(String response);
}
