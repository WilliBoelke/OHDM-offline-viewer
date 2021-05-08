package de.htwBerlin.ois.model.serverCommunication;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.htwBerlin.ois.model.models.fileStructure.RemoteDirectory;
import de.htwBerlin.ois.model.models.fileStructure.RemoteFile;
import de.htwBerlin.ois.serverCommunication.FtpClient;

import static de.htwBerlin.ois.model.repositories.localRepositories.Variables.USER_NAME;
import static de.htwBerlin.ois.model.repositories.localRepositories.Variables.USER_PASSWORD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FtpClientTest
{

    private FTPClient mockFTPClient = Mockito.mock(FTPClient.class);
    private FtpClient ftpClient;


    @BeforeEach
    public void setup()
    {
        ftpClient = new FtpClient(mockFTPClient);
    }


    //------------Connect Test------------

    @Test
    public void connectSuccessfulTest() throws IOException
    {
        Mockito.when(mockFTPClient.getReplyCode()).thenReturn(200);
        Mockito.when(mockFTPClient.login(USER_NAME, USER_PASSWORD)).thenReturn(true);

        //connect successful
        assertEquals(0, ftpClient.connect());
    }

    @Test
    public void connectWithRefusedConnection()
    {
        //Refused connection
        Mockito.when(mockFTPClient.getReplyCode()).thenReturn(500);
        int returnValue = ftpClient.connect();

        assertEquals(1, returnValue);
    }

    @Test
    public void connectWrongLoginCredentialsConnection() throws IOException
    {
        //Wrong or unaccepted credentials
        Mockito.when(mockFTPClient.getReplyCode()).thenReturn(200);
        Mockito.when(mockFTPClient.login(USER_NAME, USER_PASSWORD)).thenReturn(false);
        int returnValue = ftpClient.connect();

        assertEquals(2, returnValue);
    }

    @Test
    public void connectSocketExceptionTest() throws IOException
    {
        //SockedException occurs
        Mockito.when(mockFTPClient.getReplyCode()).thenReturn(200);
        Mockito.when(mockFTPClient.login(USER_NAME, USER_PASSWORD)).thenThrow(new SocketException());
        int returnValue = ftpClient.connect();

        assertEquals(3, returnValue);
    }

    @Test
    public void connectIOExceptionTest() throws IOException
    {
        //IOException occurs
        Mockito.when(mockFTPClient.getReplyCode()).thenReturn(200);
        Mockito.when(mockFTPClient.login(USER_NAME, USER_PASSWORD)).thenThrow(new IOException());
        int returnValue = ftpClient.connect();

        assertEquals(4, returnValue);
    }


    //------------isConnected Test------------

    @Test
    public void isConnectedTrueTest()
    {
        Mockito.when(mockFTPClient.isConnected()).thenReturn(true);
        assertTrue(ftpClient.isConnected());
    }

    @Test
    public void isConnectedFalseTest()
    {
        Mockito.when(mockFTPClient.isConnected()).thenReturn(false);
        assertFalse(ftpClient.isConnected());
    }

    //------------Listing Test------------

    @Test
    public void fileListingTest() throws IOException
    {
        FTPFile[] files = new FTPFile[2];
        FTPFile one = new FTPFile();
        one.setName("MapOne");
        one.setTimestamp(Calendar.getInstance(Locale.GERMANY));
        files[0] = one;
        FTPFile two = new FTPFile();
        two.setName("MaTwo");
        two.setTimestamp(Calendar.getInstance(Locale.GERMANY));
        files[1] = two;
        Mockito.when(mockFTPClient.listFiles("path")).thenReturn(files);
        Mockito.when(mockFTPClient.isConnected()).thenReturn(true);

        ArrayList<RemoteFile> returnFiles = ftpClient.getFileList("path");

        assertEquals(files.length, returnFiles.size());

        assertEquals(files[0].getName(), returnFiles.get(0).getFilename());
        assertEquals(files[1].getName(), returnFiles.get(1).getFilename());
    }

    @Test
    public void fileListingExceptionTest1() throws IOException
    {
        Mockito.when(mockFTPClient.listFiles("path")).thenThrow(new IOException());
        Mockito.when(mockFTPClient.isConnected()).thenReturn(true);

        assertThrows(IOException.class, new Executable()
        {
            @Override
            public void execute() throws Throwable
            {
                ftpClient.getFileList("path");
            }
        });
    }

    @Test
    public void dirListingTest() throws IOException
    {
        Mockito.when(mockFTPClient.isConnected()).thenReturn(true);
        FTPFile mockDir1 = Mockito.mock(FTPFile.class);
        Mockito.when(mockDir1.isDirectory()).thenReturn(true);
        Mockito.when(mockDir1.getName()).thenReturn("Dir1");
        Mockito.when(mockDir1.getTimestamp()).thenReturn( Calendar.getInstance(Locale.GERMANY));

        FTPFile mockDir2 = Mockito.mock(FTPFile.class);
        Mockito.when(mockDir2.isDirectory()).thenReturn(true);
        Mockito.when(mockDir2.getName()).thenReturn("Dir2");
        Mockito.when(mockDir2.getTimestamp()).thenReturn( Calendar.getInstance(Locale.GERMANY));

        FTPFile[] files = new FTPFile[3];
        files[0] = mockDir1;
        files[2] = mockDir2;

        FTPFile two = new FTPFile();
        two.setName("MapTwo");
        files[1] = two;
        Mockito.when(mockFTPClient.listFiles("path")).thenReturn(files);

        ArrayList<RemoteDirectory> returnFiles = ftpClient.getDirList("path");

        assertEquals(returnFiles.size(), 2);
        assertNotEquals(two.getName(), returnFiles.get(0).getFilename());
        assertNotEquals(two.getName(), returnFiles.get(1).getFilename());
    }

    @Test
    public void getAllFileListTest() throws IOException
    {

        Mockito.when(mockFTPClient.isConnected()).thenReturn(true);
        //Standard Dir
        FTPFile[] files = new FTPFile[3];

        //Sub Dir 1
        FTPFile mockDir1 = Mockito.mock(FTPFile.class);
        Mockito.when(mockDir1.isDirectory()).thenReturn(true);
        Mockito.when(mockDir1.getName()).thenReturn("Dir1");

        //Sub Dir 2
        FTPFile mockDir2 = Mockito.mock(FTPFile.class);
        Mockito.when(mockDir2.isDirectory()).thenReturn(true);
        Mockito.when(mockDir2.getName()).thenReturn("Dir2");

        //Files and dirs in standard dir
        files[0] = mockDir1;
        files[1] = mockDir2;

        FTPFile two = new FTPFile();
        two.setName("MapTwo");
        two.setSize(231);
        two.setTimestamp(Calendar.getInstance(Locale.GERMANY));
        files[2] = two;

        //Files in sub dir one
        FTPFile one = new FTPFile();
        one.setName("MapOne");
        one.setSize(231);
        one.setTimestamp(Calendar.getInstance(Locale.GERMANY));
        FTPFile[] filesInSubDirOne = new FTPFile[1];
        filesInSubDirOne[0] = one;

        //Files in sub dir twi
        FTPFile three = new FTPFile();
        three.setName("MapThree");
        three.setSize(231);
        three.setTimestamp(Calendar.getInstance(Locale.GERMANY));
        FTPFile[] filesInSubDirTwo = new FTPFile[1];
        filesInSubDirTwo[0] = three;

        //Returning the files in standar dir icluding the 2 sub dirs
        Mockito.when(mockFTPClient.listFiles("path")).thenReturn(files);
        //Returning the files in sub dir one
        Mockito.when(mockFTPClient.listFiles("path/Dir1")).thenReturn(filesInSubDirOne);
        //Returning the files in sub dir two
        Mockito.when(mockFTPClient.listFiles("path/Dir2")).thenReturn(filesInSubDirTwo);

        //There are altogether 3 map files
        assertEquals(ftpClient.getAllFileList("path").size(), 3);

        //These map files should not be the directories
        assertNotEquals(ftpClient.getAllFileList("path").get(0), mockDir1);
        assertNotEquals(ftpClient.getAllFileList("path").get(1), mockDir1);
        assertNotEquals(ftpClient.getAllFileList("path").get(2), mockDir1);

        //These map files should not be the directories
        assertNotEquals(ftpClient.getAllFileList("path").get(0), mockDir2);
        assertNotEquals(ftpClient.getAllFileList("path").get(1), mockDir2);
        assertNotEquals(ftpClient.getAllFileList("path").get(2), mockDir2);
    }
}