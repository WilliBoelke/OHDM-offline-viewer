package de.htwBerlin.ois.models.repositories.localRepositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.io.File;
import java.util.ArrayList;

import static de.htwBerlin.ois.models.repositories.localRepositories.Variables.MAP_FILE_PATH;


/**
 * Singleton which reads files from the OHDM Directory
 *
 * @author WilliBoelke
 */
public class LocalMapsRepository
{
    private static LocalMapsRepository instance;
    private final String TAG = getClass().getSimpleName();
    MutableLiveData<ArrayList<File>> data;
    private ArrayList<File> localMaps;

    public static LocalMapsRepository getInstance()
    {
        if (instance == null)
        {
            instance = new LocalMapsRepository();
        }
        return instance;
    }

    private LocalMapsRepository()
    {
        data = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<File>> getLocalFiles()
    {
        readLocalMapFiles();
        data.setValue(localMaps);

        return data;
    }

    /**
     * Scans OHDM Directory for .map Files
     *
     * @return a set of .map files
     */
    private void readLocalMapFiles()
    {
        localMaps = new ArrayList<>();
        try
        {
            for (File mapFile : new File(MAP_FILE_PATH).listFiles())
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
