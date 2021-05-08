package de.htwBerlin.ois.model.models.fileStructure;


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


    public void setPath(String path)
    {
        this.path = path;
    }

    public void setFilename(String filename)
    {
        this.filename = filename.trim();
    }

    public void setFileSize(Long fileSize)
    {
        this.fileSize = fileSize;
    }

    public void setCreationDate(String creationDate)
    {
        this.creationDate = creationDate;
    }

    //------------Getter------------

    public String getCreationDate()
    {
        return creationDate;
    }

    public String getPath()
    {
        return this.path;
    }

    public String getFilename()
    {
        return filename;
    }

    public Long getFileSize()
    {
        return fileSize;
    }
}
