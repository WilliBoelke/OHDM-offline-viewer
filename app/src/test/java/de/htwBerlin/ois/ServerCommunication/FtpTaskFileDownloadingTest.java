package de.htwBerlin.ois.ServerCommunication;

import android.content.Context;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;

import de.htwBerlin.ois.FileStructure.RemoteDirectory;
import de.htwBerlin.ois.FileStructure.RemoteFile;

class FtpTaskFileDownloadingTest
{
    private FtpTaskFileDownloading fileDownloadingTest;
    private FtpClient mockFtpClient = Mockito.mock(FtpClient.class);
    private RemoteFile mockFile1;
    private Context context = Mockito.mock(Context.class);
    private RemoteFile[] files;

    AsyncResponse asyncResponse = new AsyncResponse()
    {
        @Override
        public void getOhdmFiles(ArrayList<RemoteFile> remoteFiles)
        {

        }

        @Override
        public void getRemoteDirectories(ArrayList<RemoteDirectory> remoteDirectories)
        {

        }
    };

    @BeforeEach
    public void setup()
    {
        fileDownloadingTest = new FtpTaskFileDownloading(context);
        fileDownloadingTest.insertMockFtpClient(mockFtpClient);

        mockFile1 = Mockito.mock(RemoteFile.class);
        Mockito.when(mockFile1.getFilename()).thenReturn("mockFile1");
        Mockito.when(mockFile1.getPath()).thenReturn("mockFile1");

        files = new RemoteFile[1];
        files[0] = mockFile1;
    }


    @Test
    public void verifyMethodCalls() throws IOException
    {


        Mockito.when(mockFtpClient.connect()).thenReturn(1);
        fileDownloadingTest.doInBackground(files);

        Mockito.verify(mockFtpClient).connect();
        Mockito.verify(mockFile1).getFilename();
        Mockito.verify(mockFile1).getPath();
        Mockito.verify(mockFtpClient).downloadFile(mockFile1.getFilename(), mockFile1.getPath());

        Mockito.verify(mockFtpClient).closeConnection();
    }

    @Test
    public void ioException() throws IOException
    {
        // Should be cached
        Mockito.when(mockFtpClient.downloadFile(Mockito.anyString(), Mockito.anyString())).thenThrow(new IOException());

        fileDownloadingTest.insertMockFtpClient(mockFtpClient);
        fileDownloadingTest.doInBackground(files);
    }
}