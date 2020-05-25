package de.htwBerlin.ois.FileStructure;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * this singleton is used to store the lists
 * from the {@link de.htwBerlin.ois.Fragments.FragmentDownloadCenterAll}
 * and {@link de.htwBerlin.ois.Fragments.FragmentDownloadCenterCategories}
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

    public ArrayList<RemoteFile> getLatestMaps()
    {
        return latestMaps;
    }

    public ArrayList<RemoteDirectory> getDirectories()
    {
        return directories;
    }

    public HashMap<String, ArrayList<RemoteFile>> getDirectoryContents()
    {
        return directoryContents;
    }

    public void setAllMaps(ArrayList<RemoteFile> allMaps)
    {
        this.allMaps.clear();
        this.allMaps = allMaps;
    }

    public void setLatestMaps(ArrayList<RemoteFile> latestMaps)
    {
        this.latestMaps.clear();
        this.latestMaps = latestMaps;
    }

    public void setDirectories(ArrayList<RemoteDirectory> directories)
    {
        this.directories.clear();
        this.directories = directories;
    }

    public void setDirectoryContents(HashMap<String, ArrayList<RemoteFile>> directoryContents )
    {
        this.directoryContents.clear();
        this.directoryContents = directoryContents;
    }
}
