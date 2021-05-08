package de.htwBerlin.ois.model.repositories.localRepositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.io.File;
import java.util.ArrayList;

import static de.htwBerlin.ois.model.repositories.localRepositories.Variables.MAP_FILE_PATH;


/**
 * Singleton which reads files from the OHDM Directory
 *
 * @author WilliBoelke
 */
public class LocalMapsRepository
{

    //------------Instance Variables------------

    private static LocalMapsRepository instance;
    private final String TAG = getClass().getSimpleName();
    private MutableLiveData<ArrayList<File>> data;
    private ArrayList<File> localMaps;


    //------------Constructors------------

    public static LocalMapsRepository getInstance()
    {
        if (instance == null)
        {
            instance = new LocalMapsRepository();
        }
        return instance;
    }

    /**
     * Private constructor
     */
    private LocalMapsRepository()
    {
        data = new MutableLiveData<>();
    }

    /**
     * Getter for the localMaps list inside a LiveData object
     * @param targetDir
     * @return
     */
    public MutableLiveData<ArrayList<File>> getLocalFiles(File targetDir)
    {
        readLocalMapFiles(targetDir);
        data.setValue(localMaps);

        return data;
    }

    /**
     * Scans OHDM Directory for .map Files
     *
     * @return a set of .map files
     */
    private void readLocalMapFiles(File targetDir)
    {
        localMaps = new ArrayList<>();
        try
        {
            for (File mapFile : targetDir.listFiles())
            {
                if (mapFile.getName().endsWith(".map"))
                {
                    Log.i(TAG, "osmfile: " + mapFile.getName());
                    localMaps.add(mapFile);
                }
            }
        }
        catch (NullPointerException e)
        {
            Log.i(TAG, "No map files located.");
        }
    }

    /**
     * Delete the File at "position"  from the OHDM directory and from the
     * localMaps ArrayList
     * @param position
     */
    public void deleteLocalMapFile(int position)
    {
        localMaps.get(position).delete();
        localMaps.remove(position);
        data.postValue(localMaps);
    }

    public void chooseMapAt(int position)
    {
        MapFileSingleton.getInstance().setFile(localMaps.get(position));
        data.postValue(localMaps);
    }
}
