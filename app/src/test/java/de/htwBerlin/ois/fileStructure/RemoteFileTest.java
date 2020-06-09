package de.htwBerlin.ois.fileStructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RemoteFileTest
{

    //------------Test Objects------------

    private final String FILE_ONE_NAME = "Berlin";
    private final String FILE_TWO_NAME = "Hamburg";
    private final String FILE_THREE_NAME = "Leipzig";
    private final String FILE_ONE_DATE = "11-1-2020";
    private final String FILE_TWO_DATE = "12-2-2013";
    private final String FILE_THREE_DATE = "13-3-2042";
    private final String FILE_ONE_PATH = "pathone";
    private final String FILE_TWO_PATH = "pathtwo";
    private final String FILE_THREE_PATH = "paththree";
    private final long FILE_ONE_SIZE = 2134214;
    private final long FILE_TWO_SIZE = 2312;
    private final long FILE_THREE_SIZE = 21344;
    private RemoteFile remoteFile1;
    private RemoteFile remoteFile2;
    private RemoteFile remoteFile3;


    //------------Setup------------

    @BeforeEach
    public void setUp()
    {
        remoteFile1 = new RemoteFile(FILE_ONE_NAME, FILE_ONE_PATH, FILE_ONE_SIZE, FILE_ONE_DATE);
        remoteFile2 = new RemoteFile(FILE_TWO_NAME, FILE_TWO_PATH, FILE_TWO_SIZE, FILE_TWO_DATE);
        remoteFile3 = new RemoteFile(FILE_THREE_NAME, FILE_THREE_PATH, FILE_THREE_SIZE, FILE_THREE_DATE);
    }


    //------------fileName Test------------

    @Test
    public void filenameTest()
    {
        assertEquals(remoteFile1.getFilename(), FILE_ONE_NAME);
        assertEquals(remoteFile2.getFilename(), FILE_TWO_NAME);
        assertEquals(remoteFile3.getFilename(), FILE_THREE_NAME);

        remoteFile1.setFilename(FILE_TWO_NAME);
        remoteFile2.setFilename(FILE_ONE_NAME);

        assertEquals(remoteFile1.getFilename(), FILE_TWO_NAME);
        assertEquals(remoteFile2.getFilename(), FILE_ONE_NAME);
    }

    @Test
    public void filenameTrimTest()
    {
        assertEquals(remoteFile1.getFilename(), FILE_ONE_NAME);
        assertEquals(remoteFile2.getFilename(), FILE_TWO_NAME);
        assertEquals(remoteFile3.getFilename(), FILE_THREE_NAME);

        remoteFile1.setFilename(FILE_TWO_NAME + "  ");
        remoteFile2.setFilename(" " + FILE_ONE_NAME);

        assertEquals(remoteFile1.getFilename(), FILE_TWO_NAME);
        assertEquals(remoteFile2.getFilename(), FILE_ONE_NAME);
    }

    @Test
    public void filenameTrimInConstructorTest()
    {
        remoteFile1 = new RemoteFile(FILE_ONE_NAME + "  ", FILE_ONE_PATH, FILE_ONE_SIZE, "11-1-2020");
        assertEquals(remoteFile1.getFilename(), FILE_ONE_NAME);
    }


    //------------creationDate Test------------

    @Test
    public void creationDateTest()
    {
        assertEquals(remoteFile1.getCreationDate(), FILE_ONE_DATE);
        assertEquals(remoteFile2.getCreationDate(), FILE_TWO_DATE);
        assertEquals(remoteFile3.getCreationDate(), FILE_THREE_DATE);

        remoteFile1.setCreationDate(FILE_TWO_DATE);
        remoteFile2.setCreationDate(FILE_ONE_DATE);

        assertEquals(remoteFile1.getCreationDate(), FILE_TWO_DATE);
        assertEquals(remoteFile2.getCreationDate(), FILE_ONE_DATE);
    }


    //------------Size Test------------

    @Test
    public void sizeTest()
    {
        assertEquals((long) remoteFile1.getFileSize(), FILE_ONE_SIZE);
        assertEquals((long) remoteFile2.getFileSize(), FILE_TWO_SIZE);
        assertEquals((long) remoteFile3.getFileSize(), FILE_THREE_SIZE);

        remoteFile1.setFileSize(FILE_TWO_SIZE);
        remoteFile2.setFileSize(FILE_ONE_SIZE);

        assertEquals((long) remoteFile1.getFileSize(), FILE_TWO_SIZE);
        assertEquals((long) remoteFile2.getFileSize(), FILE_ONE_SIZE);
    }


    //------------Size Test------------


    @Test
    public void pathTest()
    {
        assertEquals(remoteFile1.getPath(), FILE_ONE_PATH);
        assertEquals(remoteFile2.getPath(), FILE_TWO_PATH);
        assertEquals(remoteFile3.getPath(), FILE_THREE_PATH);

        remoteFile1.setPath(FILE_TWO_PATH);
        remoteFile2.setPath(FILE_ONE_PATH);

        assertEquals(remoteFile1.getPath(), FILE_TWO_PATH);
        assertEquals(remoteFile2.getPath(), FILE_ONE_PATH);
    }
}