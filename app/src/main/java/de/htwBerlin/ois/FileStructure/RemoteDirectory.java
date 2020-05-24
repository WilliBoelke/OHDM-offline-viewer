package de.htwBerlin.ois.FileStructure;

/**
 * Class do describe a directory from the FTP Server
 * @author  WilliBoelke
 */
public class RemoteDirectory
{

    //------------Instance Variables------------

    private String filename;
    private String path;
    private String creationDate;


    //------------Constructors------------

    public RemoteDirectory(String path, String creationDate)
    {
        this.filename = path.replace("_", " ");
        this.path = path;
        this.creationDate = creationDate;
    }


    //------------Setter------------

    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public void setCreationDate(String creationDate)
    {
        this.creationDate = creationDate;
    }


    //------------Getter------------

    public String getFilename()
    {
        return filename;
    }

    public String getPath()
    {
        return path;
    }

    public String getCreationDate()
    {
        return creationDate;
    }
}