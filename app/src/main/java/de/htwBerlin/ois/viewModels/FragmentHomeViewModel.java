package de.htwBerlin.ois.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.util.ArrayList;

import de.htwBerlin.ois.models.repositories.localRepositories.LocalMapsRepository;

/**
 * ViewMode for the homeFragment
 */
public class FragmentHomeViewModel extends ViewModel
{

    //------------Instance Variables------------

    /**
     *
     */
    private MutableLiveData<ArrayList<File>> localMapFiles;
    private final  String TAG = getClass().getSimpleName();
    private LocalMapsRepository localMapsRepository;


    public void init()
    {
        Log.d(TAG, "init : initializing " );
        if(localMapFiles != null)
        {
            return;
        }
        localMapsRepository = LocalMapsRepository.getInstance();
        localMapFiles = localMapsRepository.getLocalFiles();
    }


    public LiveData<ArrayList<File>> getLocalMapFiles()
    {
        return localMapFiles;
    }

    public void deleteLocalMapFile(int position)
    {
        localMapsRepository.deleteLocalMapFile(position);
    }

    public void chooseMapAt(int position)
    {
        localMapsRepository.chooseMapAt(position);
    }
}
