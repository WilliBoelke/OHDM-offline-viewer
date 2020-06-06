package de.htwBerlin.ois.ServerCommunication;

import android.content.Context;

import org.apache.commons.net.ftp.FTPFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.htwBerlin.ois.FileStructure.RemoteDirectory;
import de.htwBerlin.ois.FileStructure.RemoteFile;

import static org.junit.Assert.assertEquals;

/**
 * Since the AsyncTask itself is already tested by google
 * - and therefore should run as expected its only necessary to test the implemented business logic
 * namely the code  executed in .doingInBackground() and .onPostExecute()
 */
class FtpTaskDirListingTest
{

    FtpTaskDirListing dirListingTest;
    FtpClient mockFtpClient = Mockito.mock(FtpClient.class);

    Context context = Mockito.mock(Context.class);

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
        dirListingTest = new FtpTaskDirListing(context, "path", asyncResponse);
        dirListingTest.insertMockFtpClient(mockFtpClient);
    }


    @Test
    public void verifyMethodCalls() throws IOException
    {

        FTPFile mockFile1 = Mockito.mock(FTPFile.class);
        FTPFile mockFile2 = Mockito.mock(FTPFile.class);
        Mockito.when(mockFile1.getName()).thenReturn("mockFile1");
        Mockito.when(mockFile1.getTimestamp()).thenReturn(Calendar.getInstance(Locale.GERMANY));
        Mockito.when(mockFile2.getName()).thenReturn("mockFile2");
        Mockito.when(mockFile2.getTimestamp()).thenReturn(Calendar.getInstance(Locale.GERMANY));

        FTPFile[] files = new FTPFile[2];
        files[0] = mockFile1;
        files[1] = mockFile2;


        Mockito.when(mockFtpClient.connect()).thenReturn(1);
        Mockito.when(mockFtpClient.getDirList("path")).thenReturn(files);
        dirListingTest.doInBackground();

        Mockito.verify(mockFtpClient).connect();
        Mockito.verify(mockFtpClient).getDirList("path");

        Mockito.verify(mockFile1).getName();
        Mockito.verify(mockFile1).getTimestamp();

        Mockito.verify(mockFile2).getName();
        Mockito.verify(mockFile2).getTimestamp();

        Mockito.verify(mockFtpClient).closeConnection();
    }


    @Test
    public void nullPointerException() throws IOException
    {
        // Should be cached
        Mockito.when(mockFtpClient.getDirList("path")).thenThrow(new NullPointerException());

        dirListingTest.insertMockFtpClient(mockFtpClient);
        dirListingTest.doInBackground();

        //The list should b empty but initialized
        assertEquals(0, dirListingTest.getResultList().size());
    }

    @Test
    public void ioException() throws IOException
    {
        // Should be cached
        Mockito.when(mockFtpClient.getDirList("path")).thenThrow(new IOException());

        dirListingTest.insertMockFtpClient(mockFtpClient);
        dirListingTest.doInBackground();

        //The list should b empty but initialized
        assertEquals(0, dirListingTest.getResultList().size());
    }


    @Test
    public void verifyValidResult() throws IOException
    {
        FTPFile mockFile1 = Mockito.mock(FTPFile.class);
        FTPFile mockFile2 = Mockito.mock(FTPFile.class);
        Mockito.when(mockFile1.getName()).thenReturn("mockFile1");
        Mockito.when(mockFile1.getTimestamp()).thenReturn(Calendar.getInstance(Locale.GERMANY));
        Mockito.when(mockFile2.getName()).thenReturn("mockFile2");
        Mockito.when(mockFile2.getTimestamp()).thenReturn(Calendar.getInstance(Locale.GERMANY));

        FTPFile[] files = new FTPFile[2];
        files[0] = mockFile1;
        files[1] = mockFile2;


        Mockito.when(mockFtpClient.connect()).thenReturn(1);
        Mockito.when(mockFtpClient.getDirList("path")).thenReturn(files);
        dirListingTest.doInBackground();

        assertEquals(2, dirListingTest.getResultList().size());

        assertEquals(mockFile1.getName(), dirListingTest.getResultList().get(0).getFilename());
        assertEquals(mockFile2.getName(), dirListingTest.getResultList().get(1).getFilename());

    }
}

