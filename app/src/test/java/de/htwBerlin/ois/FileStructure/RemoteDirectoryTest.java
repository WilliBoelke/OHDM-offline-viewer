package de.htwBerlin.ois.FileStructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RemoteDirectoryTest
{
    //------------Testing Objects------------

    private RemoteDirectory remoteDir1;
    private RemoteDirectory remoteDir2;
    private RemoteDirectory remoteDir3;

    private final String DIR_ONE_NAME = "Category One";
    private final String DIR_TWO_NAME = "Category Two";
    private final String DIR_THREE_NAME = "Category Three";

    private final String DIR_ONE_PATH = "Category_One";
    private final String DIR_TWO_PATH = "Category_Two";
    private final String DIR_THREE_PATH = "Category_Three";

    private final String DIR_ONE_DATE = "11-1-2020";
    private final String DIR_TWO_DATE = "12-2-2013";
    private final String DIR_THREE_DATE = "13-3-2042";


    //------------Setup------------

    @BeforeEach
    public void setUp()
    {
        remoteDir1 = new RemoteDirectory(DIR_ONE_PATH, DIR_ONE_DATE);
        remoteDir2 = new RemoteDirectory(DIR_TWO_PATH, DIR_TWO_DATE);
        remoteDir3 = new RemoteDirectory(DIR_THREE_PATH, DIR_THREE_DATE);
    }


    //------------dirName Test------------

    @Test
    public void nameTest()
    {
        //The displayed name of a dir is generated from the path
        //by replacing the "_" with a space
        assertEquals(remoteDir1.getFilename(), DIR_ONE_NAME);
        assertEquals(remoteDir2.getFilename(), DIR_TWO_NAME);
        assertEquals(remoteDir3.getFilename(), DIR_THREE_NAME);
    }

    @Test
    public void nameAndPathTrimTest()
    {
        // the name should be trimmed
        // the path should stay the same
        remoteDir1.setPath(DIR_ONE_PATH + "  ");
        assertEquals(remoteDir1.getFilename(), DIR_ONE_NAME);
        assertEquals(remoteDir1.getPath(), DIR_ONE_PATH+ "  ");
    }


    //------------dirPath Test------------

    @Test
    public void pathTest()
    {
        assertEquals(remoteDir1.getPath(), DIR_ONE_PATH);
        assertEquals(remoteDir2.getPath(), DIR_TWO_PATH);
        assertEquals(remoteDir3.getPath(), DIR_THREE_PATH);
    }


    //------------creationDate Test------------

    @Test
    public void dateTest()
    {
        assertEquals(remoteDir1.getCreationDate(), DIR_ONE_DATE);
        assertEquals(remoteDir2.getCreationDate(), DIR_TWO_DATE);
        assertEquals(remoteDir3.getCreationDate(), DIR_THREE_DATE);
    }

}