package de.htwBerlin.ois.ServerCommunication;

import java.io.File;
import java.util.ArrayList;

import de.htwBerlin.ois.FileStructure.RemoteDirectory;
import de.htwBerlin.ois.FileStructure.RemoteFile;

public interface AsyncResponse
{
    void getOhdmFiles(ArrayList<RemoteFile> remoteFiles);
    void getRemoteDirectories(ArrayList<RemoteDirectory> remoteDirectories);
}
