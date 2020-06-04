package de.htwBerlin.ois.FileStructure;


public class RemoteFile
{

    //------------Instance Variables------------

    private String filename;
    private Long fileSize;
    private String creationDate;


    //------------Constructors------------

    public RemoteFile(String filename, Long fileSize, String creationDate)
    {
        this.setFilename(filename);
        this.setFileSize(fileSize);
        this.creationDate = creationDate;
    }


    //------------Setter------------

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


    public Long getFileSize()
    {
        return fileSize;
    }

    protected String getCreationDate()
    {
        return creationDate;
    }

    public String getFilename()
    {
        return filename;
    }
}
