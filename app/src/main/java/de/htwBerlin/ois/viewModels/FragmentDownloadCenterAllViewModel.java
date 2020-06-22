package de.htwBerlin.ois.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import de.htwBerlin.ois.models.fileStructure.RemoteFile;
import de.htwBerlin.ois.models.repositories.remoteRepositories.FtpRepository;

public class FragmentDownloadCenterAllViewModel extends ViewModel
{
    //------------Instance Variables------------

    /**
     *
     */
    private MutableLiveData<ArrayList<RemoteFile>> allMaps;
    private MutableLiveData<ArrayList<RemoteFile>> mostRecentMaps;
    private final String TAG = getClass().getSimpleName();
    private FtpRepository ftpRepository;


    public void init()
    {
        Log.d(TAG, "init : initializing ");
        ftpRepository = FtpRepository.getInstance();
        allMaps = ftpRepository.getInstance().getAllMaps();
        mostRecentMaps = ftpRepository.getInstance().getMostRecentMaps();
    }


    public LiveData<ArrayList<RemoteFile>> getAllMaps()
    {
        return allMaps;
    }

    public LiveData<ArrayList<RemoteFile>> getMostRecentMaps()
    {
        return mostRecentMaps;
    }

    public void downloadMap(int index)
    {
        ftpRepository.downloadMap(index);
    }

    public void refresh()
    {
         ftpRepository.getInstance().refresh();
    }
}
