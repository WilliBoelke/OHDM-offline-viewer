package de.htwBerlin.ois.models.repositories.remoteRepositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import de.htwBerlin.ois.models.fileStructure.RemoteDirectory;
import de.htwBerlin.ois.models.fileStructure.RemoteFile;
import de.htwBerlin.ois.serverCommunication.AsyncResponse;
import de.htwBerlin.ois.serverCommunication.FtpTaskFileListing;
import de.htwBerlin.ois.serverCommunication.SftpClient;

import static de.htwBerlin.ois.serverCommunication.Variables.FTP_ROOT_DIRECTORY;
import static de.htwBerlin.ois.serverCommunication.Variables.MOST_RECENT_PATH;

public class FtpRepository
{
    private static  FtpRepository instance;
    private final String TAG = this.getClass().getSimpleName();
    private MutableLiveData<ArrayList<RemoteFile>> allMaps;
    private MutableLiveData<ArrayList<RemoteFile>> mostRecentMaps;

    private FtpRepository()
    {
        allMaps = new MutableLiveData<>();
        mostRecentMaps = new MutableLiveData<>();
        allMaps.setValue(new ArrayList<>());
        mostRecentMaps.setValue(new ArrayList<>());
        refresh();
    }

    public static FtpRepository getInstance()
    {
        if (instance == null)
        {
            instance = new FtpRepository();
        }
        return instance;
    }


    public MutableLiveData<ArrayList<RemoteFile>> getMapsFrom(String path)
    {
        return null;
    }

    public MutableLiveData<ArrayList<RemoteFile>> getMostRecentMaps()
    {
        Log.d(TAG, "getMostRecentMaps called");
        return mostRecentMaps;
    }

    public MutableLiveData<ArrayList<RemoteFile>> getAllMaps()
    {
        return allMaps;
    }

    private void getRemoteFilesFrom(String path, MutableLiveData<ArrayList<RemoteFile>> liveData)
    {
        FtpTaskFileListing ftpTaskFileListing = new FtpTaskFileListing(path, new SftpClient(), true, new AsyncResponse()
        {
            @Override
            public void getRemoteFiles(ArrayList<RemoteFile> remoteFiles)
            {
                if (remoteFiles.size() > 0)
                {
                    Log.i(TAG, "received " + remoteFiles.size() + " files.");
                    liveData.postValue(remoteFiles);

                }
                else // Server directory was empty or server hasn't responded
                {
                    liveData.postValue(new ArrayList<>());
                }
            }

            @Override
            public void getRemoteDirectories(ArrayList<RemoteDirectory> dirs)
            {
                //Not needed here
            }

            @Override
            public void getHttpResponse(String response)
            {
                //Not needed here
            }
        });
        ftpTaskFileListing.execute();
    }

    public void refresh()
    {
        Log.d(TAG, "Refresh called");
        getRemoteFilesFrom(MOST_RECENT_PATH, mostRecentMaps);
        getRemoteFilesFrom(FTP_ROOT_DIRECTORY, allMaps);
    }

    public void downloadMap(int index)
    {

    }
}
