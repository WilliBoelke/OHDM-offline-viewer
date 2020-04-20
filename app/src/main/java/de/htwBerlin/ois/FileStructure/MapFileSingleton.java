package de.htwBerlin.ois.FileStructure;

import java.io.File;

public class MapFileSingleton {

    private File file;
    private static MapFileSingleton mapFile = null;

    private MapFileSingleton(){

    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public static MapFileSingleton getInstance(){
        if (mapFile == null ) mapFile = new MapFileSingleton();
        return mapFile;
    }
}
