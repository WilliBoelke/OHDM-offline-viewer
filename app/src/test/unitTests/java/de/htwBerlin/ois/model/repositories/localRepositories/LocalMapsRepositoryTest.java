package de.htwBerlin.ois.model.repositories.localRepositories;

import androidx.lifecycle.LiveData;


import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;



public class LocalMapsRepositoryTest
{
    private File mockFile = mock(File.class);
    private File[] localFileList;


    @BeforeEach
    public void setup()
    {
        localFileList = new File[5];
        File mockFileOne = Mockito.mock(File.class);
        File mockFileTwo = Mockito.mock(File.class);
        File mockFileThree = Mockito.mock(File.class);
        File mockFileFour= Mockito.mock(File.class);
        File mockFileFive= Mockito.mock(File.class);

        when(mockFileOne.getName()).thenReturn("FileOne.map");
        when(mockFileTwo.getName()).thenReturn("FileTwo.map");
        when(mockFileThree.getName()).thenReturn("FileThree.map");
        when(mockFileFour.getName()).thenReturn("FileFour.map");
        when(mockFileFive.getName()).thenReturn("FileFive.map");

        when(mockFileOne.length()).thenReturn(123l);
        when(mockFileTwo.length()).thenReturn(123l);
        when(mockFileThree.length()).thenReturn(123l);
        when(mockFileFour.length()).thenReturn(123l);
        when(mockFileFive.length()).thenReturn(123l);

        localFileList[0] = mockFileOne;
        localFileList[1] = mockFileTwo;
        localFileList[2] = mockFileThree;
        localFileList[3] = mockFileFour;
        localFileList[4] = mockFileFive;
        when(mockFile.listFiles()).thenReturn(localFileList);
    }

}