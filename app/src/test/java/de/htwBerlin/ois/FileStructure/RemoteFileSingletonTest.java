package de.htwBerlin.ois.FileStructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class RemoteFileSingletonTest
{
    //------------Test Objects------------

    private File mockFile1 = Mockito.mock(File.class);


    //------------Setup------------

    @BeforeEach
    public void setup()
    {
        MapFileSingleton.getInstance().setFile(mockFile1);
    }


    //------------Tests------------

    @Test
    public void test()
    {
        File mockFile2 = Mockito.mock(File.class);
        assertEquals(MapFileSingleton.getInstance().getFile(), mockFile1);
        assertFalse(MapFileSingleton.getInstance().getFile().equals(mockFile2));
    }
}