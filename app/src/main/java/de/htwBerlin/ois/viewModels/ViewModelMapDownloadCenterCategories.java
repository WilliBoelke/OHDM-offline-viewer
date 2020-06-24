package de.htwBerlin.ois.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import de.htwBerlin.ois.model.models.fileStructure.RemoteDirectory;
import de.htwBerlin.ois.model.models.fileStructure.RemoteFile;
import de.htwBerlin.ois.model.repositories.remoteRepositories.FtpRepository;

public class ViewModelMapDownloadCenterCategories extends ViewModel
{
    //------------Instance Variables------------

    private MutableLiveData<ArrayList<RemoteDirectory>> directories;
    private MutableLiveData<HashMap<String, ArrayList<RemoteFile>>> directoriesContents;
    private final String TAG = getClass().getSimpleName();
    private FtpRepository ftpRepository;


    //------------Instance Variables-----------

    /**
     * Initializes all necessary Objects for the first time
     */
    public void init()
    {
        Log.d(TAG, "init : initializing ");
        ftpRepository = FtpRepository.getInstance();
        directoriesContents = ftpRepository.getDirectoryContents();
        directories = ftpRepository.getDirectories();
    }


    //------------Getter------------

    public LiveData<ArrayList<RemoteDirectory>> getDirectories()
    {
        return directories;
    }

    public LiveData<HashMap<String, ArrayList<RemoteFile>>> getDirectoriesContents()
    {
        return directoriesContents;
    }


    //------------Others------------

    /**
     * Starts the download of a RemoteFile via the FtpRepository
     *
     * @param fileToDownload
     */
    public void downloadMap(RemoteFile fileToDownload)
    {
        ftpRepository.downloadMap(fileToDownload);
    }

    /**
     * Triggers a refresh of ALL data manged by the {@link FtpRepository}
     * thou also for the data displayed by the {@link de.htwBerlin.ois.views.fragments.FragmentDownloadCenterAll}
     */
    public void refresh()
    {
        FtpRepository.getInstance().refresh();
    }
}
