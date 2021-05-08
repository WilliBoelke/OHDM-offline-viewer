package de.htwBerlin.ois.model.serverCommunication;

import android.content.Context;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;

import de.htwBerlin.ois.model.models.fileStructure.RemoteDirectory;
import de.htwBerlin.ois.model.models.fileStructure.RemoteFile;
import de.htwBerlin.ois.serverCommunication.AsyncResponse;
import de.htwBerlin.ois.serverCommunication.FtpTaskFileListing;
import de.htwBerlin.ois.serverCommunication.SftpClient;

import static org.junit.Assert.assertEquals;

public class FtpTaskFileListingTest
{

    AsyncResponse asyncResponse = new AsyncResponse()
    {
        @Override
        public void getRemoteFiles(ArrayList<RemoteFile> remoteFiles)
        {

        }

        @Override
        public void getRemoteDirectories(ArrayList<RemoteDirectory> remoteDirectories)
        {

        }

        @Override
        public void getHttpResponse(String response)
        {

        }
    };
    private FtpTaskFileListing fileListingTest;
    private SftpClient mockSftpClient = Mockito.mock(SftpClient.class);
    private Context context = Mockito.mock(Context.class);

    @BeforeEach
    public void setup()
    {
        fileListingTest = new FtpTaskFileListing( "path", mockSftpClient, false, asyncResponse);
    }


    @Test
    public void verifyMethodCalls() throws IOException
    {

        RemoteFile mockFile1 = Mockito.mock(RemoteFile.class);
        RemoteFile mockFile2 = Mockito.mock(RemoteFile.class);
        Mockito.when(mockFile1.getFilename()).thenReturn("mockDir1");
        Mockito.when(mockFile1.getCreationDate()).thenReturn("a date as strig");
        Mockito.when(mockFile2.getFilename()).thenReturn("mockDir2");
        Mockito.when(mockFile2.getCreationDate()).thenReturn("a date as strig");

        ArrayList<RemoteFile> files = new ArrayList<>();
        files.add(mockFile1);
        files.add(mockFile2);

        Mockito.when(mockSftpClient.connect()).thenReturn(1);
        Mockito.when(mockSftpClient.getFileList("path")).thenReturn(files);
        fileListingTest.doInBackground();
        Mockito.verify(mockSftpClient).connect();
        Mockito.verify(mockSftpClient).getFileList("path");
        Mockito.verify(mockSftpClient).closeConnection();
    }


    @Test
    public void nullPointerException() throws IOException
    {
        // Should be cached
        Mockito.when(mockSftpClient.getFileList("path")).thenThrow(new NullPointerException());

        fileListingTest.doInBackground();

        //The list should b empty but initialized
        assertEquals(0, fileListingTest.getResultList().size());
    }

    @Test
    public void ioException() throws IOException
    {
        // Should be cached
        Mockito.when(mockSftpClient.getFileList("path")).thenThrow(new IOException());
        
        fileListingTest.doInBackground();

        //The list should b empty but initialized
        assertEquals(0, fileListingTest.getResultList().size());
    }


    @Test
    public void verifyValidResult() throws IOException
    {
        RemoteFile mockFile1 = Mockito.mock(RemoteFile.class);
        RemoteFile mockFile2 = Mockito.mock(RemoteFile.class);
        Mockito.when(mockFile1.getFilename()).thenReturn("mockFile1");
        Mockito.when(mockFile1.getCreationDate()).thenReturn("a date as strig");
        Mockito.when(mockFile2.getFilename()).thenReturn("mockFile2");
        Mockito.when(mockFile2.getCreationDate()).thenReturn("a date as strig");

        ArrayList<RemoteFile> files = new ArrayList<>();
        files.add(mockFile1);
        files.add(mockFile2);


        Mockito.when(mockSftpClient.connect()).thenReturn(1);
        Mockito.when(mockSftpClient.getFileList("path")).thenReturn(files);
        fileListingTest.doInBackground();

        assertEquals(2, fileListingTest.getResultList().size());

        assertEquals(mockFile1.getFilename(), fileListingTest.getResultList().get(0).getFilename());
        assertEquals(mockFile2.getFilename(), fileListingTest.getResultList().get(1).getFilename());

    }
}