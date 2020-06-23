package de.htwBerlin.ois.models.repositories.remoteRepositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;

import de.htwBerlin.ois.models.fileStructure.RemoteDirectory;
import de.htwBerlin.ois.models.fileStructure.RemoteFile;
import de.htwBerlin.ois.serverCommunication.AsyncResponse;
import de.htwBerlin.ois.serverCommunication.FtpTaskDirListing;
import de.htwBerlin.ois.serverCommunication.FtpTaskFileDownloading;
import de.htwBerlin.ois.serverCommunication.FtpTaskFileListing;
import de.htwBerlin.ois.serverCommunication.SftpClient;

import static de.htwBerlin.ois.models.repositories.localRepositories.Variables.FTP_ROOT_DIRECTORY;
import static de.htwBerlin.ois.models.repositories.localRepositories.Variables.MOST_RECENT_PATH;

/**
 * This Repository manages all access to the ftp server
 */
public class FtpRepository
{

    //------------Static Variables------------

    private static FtpRepository instance;


    //------------Instance Variables------------

    private final String TAG = this.getClass().getSimpleName();
    private MutableLiveData<ArrayList<RemoteFile>> allMaps;
    private MutableLiveData<ArrayList<RemoteFile>> mostRecentMaps;
    private MutableLiveData<ArrayList<RemoteDirectory>> directories;
    /**
     * Stores K/V pairs where the String is the name of the
     * RemoteDirectory and the ArrayList the contents of the named dir
     */
    private MutableLiveData<HashMap<String, ArrayList<RemoteFile>>> remoteDirectoryContents;


    //------------Constructors------------

    private FtpRepository()
    {
        allMaps = new MutableLiveData<>(new ArrayList<>());
        mostRecentMaps = new MutableLiveData<>(new ArrayList<>());
        directories = new MutableLiveData<>(new ArrayList<>());
        remoteDirectoryContents = new MutableLiveData<>(new HashMap<>());
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


    //------------Getter------------

    /**
     * GGetter for  the remoteDirectoryContents
     *
     * @return remoteDirectoryContents
     */
    public MutableLiveData<HashMap<String, ArrayList<RemoteFile>>> getDirectoryContents()
    {
        return remoteDirectoryContents;
    }

    /**
     * Returns the mostRecentMaps
     *
     * @return mostRecentMaps
     */
    public MutableLiveData<ArrayList<RemoteFile>> getMostRecentMaps()
    {
        Log.d(TAG, "getMostRecentMaps called");
        return mostRecentMaps;
    }

    /**
     * Getter for the mostRecentMaps
     *
     * @return mostRecentMaps
     */
    public MutableLiveData<ArrayList<RemoteFile>> getAllMaps()
    {
        return allMaps;
    }

    /**
     * Getter for the directories
     *
     * @return directories
     */
    public MutableLiveData<ArrayList<RemoteDirectory>> getDirectories()
    {
        return directories;
    }


    //------------Retrieving Data------------

    private void retrieveRemoteFilesFrom(String path, MutableLiveData<ArrayList<RemoteFile>> liveData)
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

    private void retrieveRemoteDirectories()
    {
        FtpTaskDirListing dirListing = new FtpTaskDirListing(FTP_ROOT_DIRECTORY, new AsyncResponse()
        {
            @Override
            public void getRemoteFiles(ArrayList<RemoteFile> remoteFiles)
            {

            }

            @Override
            public void getRemoteDirectories(ArrayList<RemoteDirectory> dirs)
            {
                if (dirs.size() == 0)
                {

                }
                else
                {
                    directories.postValue(dirs);
                    for (RemoteDirectory d : dirs)
                    {
                        retrieveDirContent(d.getPath());
                    }
                }
            }

            @Override
            public void getHttpResponse(String response)
            {
                //Not needed here
            }
        });
        dirListing.execute();
    }

    private void retrieveDirContent(String path)
    {
        FtpTaskFileListing ftpTaskFileListing = new FtpTaskFileListing(path, new SftpClient(), false, new AsyncResponse()
        {
            @Override
            public void getRemoteFiles(ArrayList<RemoteFile> remoteFiles)
            {
                if (remoteFiles.size() == 0) // Server directory was empty or server hasn't responded
                {

                }
                else
                {
                    Log.i(TAG, "received " + remoteFiles.size() + " files.");
                    HashMap<String, ArrayList<RemoteFile>> temp = remoteDirectoryContents.getValue();
                    temp.put(path, remoteFiles);
                    remoteDirectoryContents.postValue(temp);
                }

            }

            @Override
            public void getRemoteDirectories(ArrayList<RemoteDirectory> dirs)
            {
                //No need to be implemented here
            }

            @Override
            public void getHttpResponse(String response)
            {
                //Not needed here
            }
        });
        ftpTaskFileListing.execute();
    }


    //------------Others------------

    public void refresh()
    {
        Log.d(TAG, "Refresh called");
        retrieveRemoteFilesFrom(MOST_RECENT_PATH, mostRecentMaps);
        retrieveRemoteFilesFrom(FTP_ROOT_DIRECTORY, allMaps);
        retrieveRemoteDirectories();
    }

    /**
     * Starts the executing of an AsyncTask witch downloads the passed remoteFile
     * <p>
     * Using a remoteFile instead of an index
     * because we are working with several different
     * lists an thou with different indices
     *
     * @param fileToDownload The file7map to be downloaded
     */
    public void downloadMap(RemoteFile fileToDownload)
    {
        FtpTaskFileDownloading ftpTaskFileDownloading = new FtpTaskFileDownloading();
        ftpTaskFileDownloading.execute(fileToDownload);
    }
}
