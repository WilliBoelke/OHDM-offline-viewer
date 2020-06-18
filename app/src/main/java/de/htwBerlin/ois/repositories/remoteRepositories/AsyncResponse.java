package de.htwBerlin.ois.repositories.remoteRepositories;

import java.util.ArrayList;

import de.htwBerlin.ois.models.fileStructure.RemoteDirectory;
import de.htwBerlin.ois.models.fileStructure.RemoteFile;

public interface AsyncResponse
{
    void getRemoteFiles(ArrayList<RemoteFile> remoteFiles);

    void getRemoteDirectories(ArrayList<RemoteDirectory> remoteDirectories);

    void getHttpResponse(String response);
}
