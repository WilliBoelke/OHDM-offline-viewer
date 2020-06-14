package de.htwBerlin.ois.serverCommunication;

import android.content.Context;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import de.htwBerlin.ois.fileStructure.RemoteFile;

class FtpTaskFileDownloadingTest
{
    private FtpTaskFileDownloading fileDownloadingTest;
    private SftpClient mockFtpClient = Mockito.mock(SftpClient.class);
    private RemoteFile mockFile1;
    private Context context = Mockito.mock(Context.class);
    private RemoteFile[] files;


    @BeforeEach
    public void setup()
    {
        fileDownloadingTest = new FtpTaskFileDownloading(context);
        fileDownloadingTest.insertMockSftpClient(mockFtpClient);

        mockFile1 = Mockito.mock(RemoteFile.class);
        Mockito.when(mockFile1.getFilename()).thenReturn("mockFile1");
        Mockito.when(mockFile1.getPath()).thenReturn("mockFile1");

        files = new RemoteFile[1];
        files[0] = mockFile1;
    }


    @Test
    public void verifyMethodCalls()
    {


        Mockito.when(mockFtpClient.connect()).thenReturn(1);
        fileDownloadingTest.doInBackground(files);

        Mockito.verify(mockFtpClient).connect();
        Mockito.verify(mockFile1).getFilename();
        Mockito.verify(mockFile1).getPath();
        Mockito.verify(mockFtpClient).downloadFile(mockFile1.getFilename(), mockFile1.getPath());

        Mockito.verify(mockFtpClient).closeConnection();
    }

}