package de.htwBerlin.ois.FileStructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class RemoteListsSingletonTest
{
    private File mockFile1 = Mockito.mock(File.class);

    @BeforeEach
    public void setup()
    {
        MapFileSingleton.getInstance().setFile(mockFile1);
    }

    @Test
    public void test()
    {
        File mockFile2 = Mockito.mock(File.class);
        assertEquals(MapFileSingleton.getInstance().getFile(), mockFile1);
        assertFalse(MapFileSingleton.getInstance().getFile().equals(mockFile2));
    }
}