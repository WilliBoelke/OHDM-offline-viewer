package de.htwBerlin.ois.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import de.htwBerlin.ois.models.fileStructure.RemoteFile;
import de.htwBerlin.ois.models.repositories.remoteRepositories.FtpRepository;

/**
 * ViewModel for the {@link de.htwBerlin.ois.views.fragments.FragmentDownloadCenterAll}
 * Communicates with the {@link FtpRepository} to retrieve the Data
 *
 * @author  WilliBoelke
 */
public class ViewModelDownloadCenterAll extends ViewModel
{

    //------------Instance Variables------------

    private MutableLiveData<ArrayList<RemoteFile>> allMaps;
    private MutableLiveData<ArrayList<RemoteFile>> mostRecentMaps;
    private final String TAG = getClass().getSimpleName();
    private FtpRepository ftpRepository;


    //------------Constructors------------

    /**
     * Initializes all necessary Objects for the first time
     */
    public void init()
    {
        Log.d(TAG, "init : initializing ");
        ftpRepository = FtpRepository.getInstance();
        allMaps = ftpRepository.getInstance().getAllMaps();
        mostRecentMaps = ftpRepository.getInstance().getMostRecentMaps();
    }


    //------------Getter------------

    public LiveData<ArrayList<RemoteFile>> getAllMaps()
    {
        return allMaps;
    }

    public LiveData<ArrayList<RemoteFile>> getMostRecentMaps()
    {
        return mostRecentMaps;
    }


    //------------Others------------

    /**
     * Starts the download of a RemoteFile via the FtpRepository
     * @param fileToDownload
     */
    public void downloadMap(RemoteFile fileToDownload)
    {
        ftpRepository.downloadMap(fileToDownload);
    }

    /**
     * Triggers a refresh of ALL data manged by the {@link FtpRepository}
     * thou also for the data displayed by the {@link de.htwBerlin.ois.views.fragments.FragmentDownloadCenterCategories}
     */
    public void refresh()
    {
         ftpRepository.refresh();
    }
}
