package de.htwBerlin.ois.FileStructure;

public class RemoteDirectory
{
    private String filename;
    private String path;
    private String creationDate;

    public RemoteDirectory(String path, String creationDate)
    {
        this.filename = path.replace("_", " ");
        this.path = path;
        this.creationDate = creationDate;
    }

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