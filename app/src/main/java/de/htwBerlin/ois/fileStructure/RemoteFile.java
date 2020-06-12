package de.htwBerlin.ois.fileStructure;


public class RemoteFile
{

    //------------Instance Variables------------

    private String filename;
    private Long fileSize;
    private String creationDate;
    private String path;


    //------------Constructors------------

    public RemoteFile(String filename, String path, Long fileSize, String creationDate)
    {
        this.setFilename(filename);
        this.setFileSize(fileSize);
        this.setPath(path);
        this.setCreationDate(creationDate);
    }


    //------------Setter------------

    public String getPath()
    {
        return this.path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public Long getFileSize()
    {
        return fileSize;
    }

    public void setFileSize(Long fileSize)
    {
        this.fileSize = fileSize;
    }

    //------------Getter------------

    public String getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(String creationDate)
    {
        this.creationDate = creationDate;
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        this.filename = filename.trim();
    }
}
