package de.htwBerlin.ois.fileStructure;

import java.io.Serializable;

import static de.htwBerlin.ois.serverCommunication.Variables.FTP_ROOT_DIRECTORY;

/**
 * Class do describe a directory from the FTP Server
 *
 * @author WilliBoelke
 */
public class RemoteDirectory implements Serializable
{

    //------------Instance Variables------------

    private String filename;
    private String path;
    private String creationDate;


    //------------Constructors------------

    public RemoteDirectory(String path, String creationDate)
    {
        this.setPath(path);
        this.setCreationDate(creationDate);
    }


    //------------Setter------------

    public String getFilename()
    {
        return filename;
    }

    /**
     * Private because the name is generated from  the path
     *
     * @param path
     */
    private void setFilename(String path)
    {
        path = path.replace(FTP_ROOT_DIRECTORY, "");
        path = path.replace("/", "");
        path = path.replace("_", " ").trim();
        this.filename = path;
    }

    public String getPath()
    {
        return path;
    }


    //------------Getter------------

    public void setPath(String path)
    {
        this.path = path;
        this.setFilename(path);
    }

    public String getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(String creationDate)
    {
        this.creationDate = creationDate;
    }
}