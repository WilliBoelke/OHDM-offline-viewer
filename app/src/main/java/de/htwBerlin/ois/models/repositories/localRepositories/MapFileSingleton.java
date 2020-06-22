package de.htwBerlin.ois.models.repositories.localRepositories;

import java.io.File;

/**
 * Singleton to save which Map file the user selected in the HomeFragment (currentMap)
 */
public class MapFileSingleton
{

    private static MapFileSingleton mapFile = null;
    /*
    The selected map file
     */
    private File file;

    private MapFileSingleton()
    {

    }

    public static MapFileSingleton getInstance()
    {
        if (mapFile == null) mapFile = new MapFileSingleton();
        return mapFile;
    }

    /**
     * Getter for the Map file
     *
     * @return the selected map
     */
    public File getFile()
    {
        return file;
    }

    /**
     * Setter for the selected map file
     *
     * @param file, the selected map file
     */
    public void setFile(File file)
    {
        this.file = file;
    }
}
