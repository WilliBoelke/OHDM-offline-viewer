package de.htwBerlin.ois.FileStructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RemoteFileTest
{

    //------------Testing Objects------------

    private RemoteFile remoteFile1;
    private RemoteFile remoteFile2;
    private RemoteFile remoteFile3;

    private final String FILE_ONE_NAME = "Berlin";
    private final String FILE_TWO_NAME = "Hamburg";
    private final String FILE_THREE_NAME = "Leipzig";

    private final String FILE_ONE_DATE = "11-1-2020";
    private final String FILE_TWO_DATE = "12-2-2013";
    private final String FILE_THREE_DATE = "13-3-2042";

    private final long FILE_ONE_SIZE = 2134214l;
    private final long FILE_TWO_SIZE = 2312l;
    private final long FILE_THREE_SIZE = 21344l;


    //------------Setup------------

    @BeforeEach
    public void setUp()
    {
        remoteFile1 = new RemoteFile(FILE_ONE_NAME, FILE_ONE_SIZE, FILE_ONE_DATE);
        remoteFile2 = new RemoteFile(FILE_TWO_NAME, FILE_TWO_SIZE, FILE_TWO_DATE);
        remoteFile3 = new RemoteFile(FILE_THREE_NAME, FILE_THREE_SIZE, FILE_THREE_DATE);
    }

    public void tearDown()
    {
    }


    //------------fileName Test------------

    @Test
    public void filenameTest()
    {
        assertTrue(remoteFile1.getFilename().equals(FILE_ONE_NAME));
        assertTrue(remoteFile2.getFilename().equals(FILE_TWO_NAME));
        assertTrue(remoteFile3.getFilename().equals(FILE_THREE_NAME));

        remoteFile1.setFilename(FILE_TWO_NAME);
        remoteFile2.setFilename(FILE_ONE_NAME);

        assertTrue(remoteFile1.getFilename().equals(FILE_TWO_NAME));
        assertTrue(remoteFile2.getFilename().equals(FILE_ONE_NAME));
    }


    @Test
    public void filenameTrimTest()
    {
        assertTrue(remoteFile1.getFilename().equals(FILE_ONE_NAME));
        assertTrue(remoteFile2.getFilename().equals(FILE_TWO_NAME));
        assertTrue(remoteFile3.getFilename().equals(FILE_THREE_NAME));

        remoteFile1.setFilename(FILE_TWO_NAME + "  ");
        remoteFile2.setFilename(" " + FILE_ONE_NAME);

        assertTrue(remoteFile1.getFilename().equals(FILE_TWO_NAME));
        assertTrue(remoteFile2.getFilename().equals(FILE_ONE_NAME));
    }


    @Test
    public void filenameTrimInConstructorTest()
    {
        remoteFile1 = new RemoteFile(FILE_ONE_NAME + "  ", FILE_ONE_SIZE, "11-1-2020");
        assertTrue(remoteFile1.getFilename().equals(FILE_ONE_NAME));
    }


    //------------creationDate Test------------

    @Test
    public void creationDateTest()
    {
        assertTrue(remoteFile1.getCreationDate().equals(FILE_ONE_DATE));
        assertTrue(remoteFile2.getCreationDate().equals(FILE_TWO_DATE));
        assertTrue(remoteFile3.getCreationDate().equals(FILE_THREE_DATE));

        remoteFile1.setCreationDate(FILE_TWO_DATE);
        remoteFile2.setCreationDate(FILE_ONE_DATE);

        assertTrue(remoteFile1.getCreationDate().equals(FILE_TWO_DATE));
        assertTrue(remoteFile2.getCreationDate().equals(FILE_ONE_DATE));
    }


    //------------size Test------------

    @Test
    public void sizeTest()
    {
        assertTrue(remoteFile1.getFileSize() == FILE_ONE_SIZE);
        assertTrue(remoteFile2.getFileSize() == FILE_TWO_SIZE);
        assertTrue(remoteFile3.getFileSize() == FILE_THREE_SIZE);

        remoteFile1.setFileSize(FILE_TWO_SIZE);
        remoteFile2.setFileSize(FILE_ONE_SIZE);

        assertTrue(remoteFile1.getFileSize() == FILE_TWO_SIZE);
        assertTrue(remoteFile2.getFileSize()== FILE_ONE_SIZE);
    }


}