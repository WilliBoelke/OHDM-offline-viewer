package de.htwBerlin.ois.FTP;

import java.util.ArrayList;

import de.htwBerlin.ois.FileStructure.OhdmFile;

public interface AsyncResponse {

    void getOhdmFiles(ArrayList<OhdmFile> ohdmFiles);
}
