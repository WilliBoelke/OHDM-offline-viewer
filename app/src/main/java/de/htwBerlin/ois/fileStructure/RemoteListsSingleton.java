package de.htwBerlin.ois.fileStructure;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * this singleton is used to store the lists
 * from the {@link de.htwBerlin.ois.ui.fragments.FragmentDownloadCenterAll}
 * and {@link de.htwBerlin.ois.ui.fragments.FragmentDownloadCenterCategories}
 * at runtime.
 * to minimize server requests
 */
public class RemoteListsSingleton
{

    private static ArrayList<RemoteFile> allMaps;
    private static ArrayList<RemoteFile> latestMaps;
    private static ArrayList<RemoteDirectory> directories;
    private static HashMap<String, ArrayList<RemoteFile>> directoryContents;
    private static RemoteListsSingleton instance = null;

    private RemoteListsSingleton()
    {

    }

    public static RemoteListsSingleton getInstance()
    {
        if (instance == null)
        {
            instance = new RemoteListsSingleton();
            latestMaps = new ArrayList<>();
            allMaps = new ArrayList<>();
            directories = new ArrayList<>();
            directoryContents = new HashMap<>();
        }
        return instance;
    }

    public ArrayList<RemoteFile> getAllMaps()
    {
        return allMaps;
    }

    public void setAllMaps(ArrayList<RemoteFile> allMaps)
    {
        RemoteListsSingleton.allMaps.clear();
        RemoteListsSingleton.allMaps.addAll(allMaps);
    }

    public ArrayList<RemoteFile> getLatestMaps()
    {
        return latestMaps;
    }

    public void setLatestMaps(ArrayList<RemoteFile> latestMaps)
    {
        RemoteListsSingleton.latestMaps.clear();
        RemoteListsSingleton.latestMaps.addAll(latestMaps);
    }

    public ArrayList<RemoteDirectory> getDirectories()
    {
        return directories;
    }

    public void setDirectories(ArrayList<RemoteDirectory> directories)
    {
        RemoteListsSingleton.directories.clear();
        RemoteListsSingleton.directories.addAll(directories);
    }

    public HashMap<String, ArrayList<RemoteFile>> getDirectoryContents()
    {
        return directoryContents;
    }

    public void setDirectoryContents(HashMap<String, ArrayList<RemoteFile>> directoryContents)
    {
        RemoteListsSingleton.directoryContents.clear();
        RemoteListsSingleton.directoryContents.putAll(directoryContents);
    }

    /**
     * Resets the instance
     * used for testing
     */
    public void resetInstance()
    {
        this.instance = null;
    }
}
