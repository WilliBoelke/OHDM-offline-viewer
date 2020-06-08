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
    private FtpTaskDirListing dirListingTest;
    private SftpClient mocksFtpClient = Mockito.mock(SftpClient.class);
    private Context context = Mockito.mock(Context.class);

    private AsyncResponse asyncResponse = new AsyncResponse()
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
        dirListingTest.insertMockFtpClient(mocksFtpClient);
    }



    @Test
    public void verifyMethodCalls() throws IOException
    {


        RemoteDirectory mockFile1 = Mockito.mock(RemoteDirectory.class);
        RemoteDirectory mockFile2 = Mockito.mock(RemoteDirectory.class);
        Mockito.when(mockFile1.getFilename()).thenReturn("mockDir1");
        Mockito.when(mockFile1.getCreationDate()).thenReturn("a date as strig");
        Mockito.when(mockFile2.getFilename()).thenReturn("mockDir2");
        Mockito.when(mockFile2.getCreationDate()).thenReturn("a date as strig");

        ArrayList<RemoteDirectory> files = new ArrayList<>();
        files.add(mockFile1);
        files.add(mockFile2);

        Mockito.when(mocksFtpClient.connect()).thenReturn(1);
        Mockito.when(mocksFtpClient.getDirList("path")).thenReturn(files);
        dirListingTest.doInBackground();

        Mockito.verify(mocksFtpClient).connect();
        Mockito.verify(mocksFtpClient).getDirList("path");
        Mockito.verify(mocksFtpClient).closeConnection();
    }


    @Test
    public void nullPointerException() throws IOException
    {
        // Should be cached
        Mockito.when(mocksFtpClient.getDirList("path")).thenThrow(new NullPointerException());

        dirListingTest.insertMockFtpClient(mocksFtpClient);
        dirListingTest.doInBackground();

        //The list should b empty but initialized
        assertEquals(0, dirListingTest.getResultList().size());
    }

    @Test
    public void ioException() throws IOException
    {
        // Should be cached
        Mockito.when(mocksFtpClient.getDirList("path")).thenThrow(new IOException());

        dirListingTest.insertMockFtpClient(mocksFtpClient);
        dirListingTest.doInBackground();

        //The list should b empty but initialized
        assertEquals(0, dirListingTest.getResultList().size());
    }


    @Test
    public void verifyValidResult() throws IOException
    {
        RemoteDirectory mockFile1 = Mockito.mock(RemoteDirectory.class);
        RemoteDirectory mockFile2 = Mockito.mock(RemoteDirectory.class);
        Mockito.when(mockFile1.getFilename()).thenReturn("mockDir1");
        Mockito.when(mockFile1.getCreationDate()).thenReturn("a date as strig");
        Mockito.when(mockFile2.getFilename()).thenReturn("mockDir2");
        Mockito.when(mockFile2.getCreationDate()).thenReturn("a date as strig");

        ArrayList<RemoteDirectory> files = new ArrayList<>();
        files.add(mockFile1);
        files.add(mockFile2);

        Mockito.when(mocksFtpClient.connect()).thenReturn(1);
        Mockito.when(mocksFtpClient.getDirList("path")).thenReturn(files);
        dirListingTest.doInBackground();

        assertEquals(2, dirListingTest.getResultList().size());

        assertEquals(mockFile1.getFilename(), dirListingTest.getResultList().get(0).getFilename());
        assertEquals(mockFile2.getFilename(), dirListingTest.getResultList().get(1).getFilename());

    }
}

