package de.htwBerlin.ois.FileStructure;

public class OhdmFile {

    private String filename;
    private Long fileSize;
    private String creationDate;
    private boolean isDownloaded;

    public OhdmFile(String filename, Long fileSize, String creationDate, boolean isDownloaded) {
        this.filename = filename;
        this.fileSize = fileSize;
        this.creationDate = creationDate;
        this.isDownloaded = isDownloaded;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }

    @Override
    public String toString() {
        return "OhdmFile{" +
                "filename='" + filename + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", isDownloaded=" + isDownloaded +
                '}';
    }
}
