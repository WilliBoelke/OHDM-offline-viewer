package de.htwBerlin.ois.model.serverCommunication;


import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import de.htwBerlin.ois.models.fileStructure.RemoteDirectory;
import de.htwBerlin.ois.models.fileStructure.RemoteFile;
import de.htwBerlin.ois.serverCommunication.SftpClient;
import de.htwBerlin.ois.serverCommunication.Variables;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SftpClientTest
{

    Session mockSession = Mockito.mock(Session.class);
    ChannelSftp mockChannel = Mockito.mock(ChannelSftp.class);
    SftpClient testSftpClient;
    private String serverOutput = "[drwxr-xr-x   8 s0569194 s0569194     4096 Jun  8 15:05 ., " + "-rw-r--r--   1 s0569194 s0569194   815311 Jun  8 12:26 testRequest.map, " + "drwxr-xr-x   2 s0569194 s0569194     4096 Jun  4 11:36 EastGermany, " + "drwxr-xr-x   2 s0569194 s0569194     4096 Jun  4 11:41 last_requests, " + "drwxr-xr-x   2 s0569194 s0569194     4096 Jun  4 12:02 WestGermany," + " drwxr-xr-x   2 s0569194 s0569194     4096 Jun  4 11:43 most_recent, " + "-rw-r--r--   2 s0569194 s0569194     4096 Jun  4 11:45 berlin.map, " + "drwxr-xr-x   2 s0569194 s0569194     4096 Jun  4 11:51 SouthGermany, " + "-rw-r--r--   1 s0569194 s0569194  4718669 Jun  4 11:35 ohdm.map]";

    @BeforeEach
    public void setup()
    {
        when(mockSession.isConnected()).thenReturn(true);
        when(mockChannel.isConnected()).thenReturn(true);
        this.testSftpClient = new SftpClient();
        testSftpClient.insertMockObjects(mockSession, mockChannel);
    }


    @Test
    public void connectTest()
    {

    }

    @Test
    public void isConnectedTrueTest()
    {

        assertTrue(testSftpClient.isConnected());
    }

    @Test
    public void isConnectedFalseTest()
    {
        when(mockSession.isConnected()).thenReturn(false);
        when(mockChannel.isConnected()).thenReturn(false);

        assertFalse(testSftpClient.isConnected());
    }

    @Test
    public void isConnectedPartiallyFalse1Test()
    {
        when(mockSession.isConnected()).thenReturn(false);
        when(mockChannel.isConnected()).thenReturn(true);

        assertFalse(testSftpClient.isConnected());
    }

    @Test
    public void isConnectedPartiallyFalse2Test()
    {
        when(mockSession.isConnected()).thenReturn(true);
        when(mockChannel.isConnected()).thenReturn(false);

        assertFalse(testSftpClient.isConnected());
    }

    @Test
    public void closeConnectionVerifyMethodCalls()
    {
        testSftpClient.closeConnection();
        verify(mockChannel).exit();
        verify(mockSession).disconnect();
    }

    @Test
    public void getFileListTestSite() throws SftpException, IOException
    {
        Vector mockVec = Mockito.mock(Vector.class);
        Mockito.when(mockVec.toString()).thenReturn(serverOutput);

        Mockito.when(mockChannel.ls("path")).thenReturn(mockVec);
        ArrayList<RemoteFile> result = testSftpClient.getFileList("path");

        // There are two "Files" in the serverOutput  (the ones without a d as first char)
        assertEquals(3, result.size());
    }

    @Test
    public void getFileListTestCheckNamesTest() throws SftpException, IOException
    {
        Vector mockVec = Mockito.mock(Vector.class);
        Mockito.when(mockVec.toString()).thenReturn(serverOutput);

        Mockito.when(mockChannel.ls("path")).thenReturn(mockVec);
        ArrayList<RemoteFile> result = testSftpClient.getFileList("path");

        // Check if the ame sof the 3 files are correct
        assertEquals("testRequest.map", result.get(0).getFilename());
        assertEquals("berlin.map", result.get(1).getFilename());
        assertEquals("ohdm.map", result.get(2).getFilename());
    }


    @Test
    public void getFileListTestCheckPathsTest() throws SftpException, IOException
    {
        Vector mockVec = Mockito.mock(Vector.class);
        Mockito.when(mockVec.toString()).thenReturn(serverOutput);

        Mockito.when(mockChannel.ls("path/subdir/subsubdir")).thenReturn(mockVec);
        ArrayList<RemoteFile> result = testSftpClient.getFileList("path/subdir/subsubdir");

        // Chac if the ame sof the 3 files are correct
        assertEquals("path/subdir/subsubdir", result.get(0).getPath());
        assertEquals("path/subdir/subsubdir", result.get(1).getPath());
        assertEquals("path/subdir/subsubdir", result.get(2).getPath());
    }


    @Test
    public void getDirListTestSize() throws SftpException, IOException
    {
        Vector mockVec = Mockito.mock(Vector.class);
        Mockito.when(mockVec.toString()).thenReturn(serverOutput);

        Mockito.when(mockChannel.ls("path")).thenReturn(mockVec);
        ArrayList<RemoteDirectory> result = testSftpClient.getDirList("path");

        // There are two "Dirs" in the serverOutput  (the ones with a d as first char)
        assertEquals(5, result.size());
    }

    @Test
    public void getDirListTestCheckNamesTest() throws SftpException, IOException
    {
        Vector mockVec = Mockito.mock(Vector.class);
        Mockito.when(mockVec.toString()).thenReturn(serverOutput);

        // i am using the FTP_ROO_PORT here, because its filtered in the RemoteDirectory.setFileName
        Mockito.when(mockChannel.ls(Variables.FTP_ROOT_DIRECTORY)).thenReturn(mockVec);
        ArrayList<RemoteDirectory> result = testSftpClient.getDirList(Variables.FTP_ROOT_DIRECTORY);

        // Check if the ame sof the 3 files are correct
        assertEquals("EastGermany", result.get(0).getFilename());
        assertEquals("WestGermany", result.get(2).getFilename());
        assertEquals("SouthGermany", result.get(4).getFilename());
        //Special attention to them -- the "_" should be replaced with a space in their set name method
        assertEquals("last requests", result.get(1).getFilename());
        assertEquals("most recent", result.get(3).getFilename());
    }


    @Test
    public void getDirListTestCheckPathsTest() throws SftpException, IOException
    {
        Vector mockVec = Mockito.mock(Vector.class);
        Mockito.when(mockVec.toString()).thenReturn(serverOutput);

        Mockito.when(mockChannel.ls("path/subdir/subsubdir")).thenReturn(mockVec);
        ArrayList<RemoteDirectory> result = testSftpClient.getDirList("path/subdir/subsubdir");

        // Chac if the ame sof the 3 files are correct
        assertEquals("path/subdir/subsubdir/EastGermany/", result.get(0).getPath());
        assertEquals("path/subdir/subsubdir/last_requests/", result.get(1).getPath());
        assertEquals("path/subdir/subsubdir/WestGermany/", result.get(2).getPath());
    }


    @Test
    public void readDateTest() throws SftpException
    {
        String serverOutputDate = "[ drwxr-xr-x   2 s0569194 s0569194     4096 Jan  4 11:36 EastGermany, " + "drwxr-xr-x   2 s0569194 s0569194     4096 Feb  4 11:41 last_requests, " + "drwxr-xr-x   2 s0569194 s0569194     4096 Mar  4 12:02 WestGermany," + "drwxr-xr-x   2 s0569194 s0569194     4096 Apr  4 11:43 most_recent, " + "drwxr-xr-x   2 s0569194 s0569194     4096 May 4 11:45 berlin.map, " + "drwxr-xr-x   2 s0569194 s0569194     4096 Jun 4 11:51 SouthGermany, " + "drwxr-xr-x   2 s0569194 s0569194     4096 Jul  4 11:51 SouthGermany, " + "drwxr-xr-x   2 s0569194 s0569194     4096 Aug  4 11:51 SouthGermany, " + "drwxr-xr-x   2 s0569194 s0569194     4096 Sep 4 11:51 SouthGermany, " + "drwxr-xr-x   2 s0569194 s0569194     4096 Okt 4 11:51 SouthGermany, " + "drwxr-xr-x   2 s0569194 s0569194     4096 Nov 4 11:51 SouthGermany, " + "drwxr-xr-x   1 s0569194 s0569194     4718669 Dec  4 11:35 ohdm.map]";

        Vector mockVec = Mockito.mock(Vector.class);
        Mockito.when(mockVec.toString()).thenReturn(serverOutputDate);

        // i am using the FTP_ROO_PORT here, because its filtered in the RemoteDirectory.setFileName
        Mockito.when(mockChannel.ls(Variables.FTP_ROOT_DIRECTORY)).thenReturn(mockVec);
        ArrayList<RemoteDirectory> result = testSftpClient.getDirList(Variables.FTP_ROOT_DIRECTORY);

        assertEquals("2020-01-4", result.get(0).getCreationDate());
        assertEquals("2020-02-4", result.get(1).getCreationDate());
        assertEquals("2020-03-4", result.get(2).getCreationDate());
        assertEquals("2020-04-4", result.get(3).getCreationDate());
        assertEquals("2020-05-4", result.get(4).getCreationDate());
        assertEquals("2020-06-4", result.get(5).getCreationDate());
        assertEquals("2020-07-4", result.get(6).getCreationDate());
        assertEquals("2020-08-4", result.get(7).getCreationDate());
        assertEquals("2020-09-4", result.get(8).getCreationDate());
        assertEquals("2020-10-4", result.get(9).getCreationDate());
        assertEquals("2020-11-4", result.get(10).getCreationDate());
        assertEquals("2020-12-4", result.get(11).getCreationDate());

    }


    @Test
    public void getAllFileListTest() throws IOException, SftpException
    {

        serverOutput = "[drwxr-xr-x   8 s0569194 s0569194     4096 Jun  8 15:05 ., " + "-rw-r--r--   1 s0569194 s0569194   815311 Jun  8 12:26 testRequest.map, " + "drwxr-xr-x   2 s0569194 s0569194     4096 Jun  4 11:36 EastGermany, " + "-rw-r--r--   1 s0569194 s0569194  4718669 Jun  4 11:35 ohdm.map]";
        String serverOutputSubDir = "[drwxr-xr-x   8 s0569194 s0569194     4096 Jun  8 15:05 ., " + "-rw-r--r--   1 s0569194 s0569194  4718669 Jun  4 11:35 ohdm.map]";
        Vector mockVecRoot = Mockito.mock(Vector.class);
        Vector mockVecSubDir = Mockito.mock(Vector.class);
        Mockito.when(mockVecRoot.toString()).thenReturn(serverOutput);
        Mockito.when(mockVecSubDir.toString()).thenReturn(serverOutputSubDir);

        // i am using the FTP_ROO_PORT here, because its filtered in the RemoteDirectory.setFileName
        Mockito.when(mockChannel.ls(Variables.FTP_ROOT_DIRECTORY)).thenReturn(mockVecRoot);
        Mockito.when(mockChannel.ls(Variables.FTP_ROOT_DIRECTORY + "/" + "EastGermany" + "/")).thenReturn(mockVecSubDir);
        ArrayList<RemoteFile> result = testSftpClient.getAllFileList(Variables.FTP_ROOT_DIRECTORY);

        //Tere should be 2 Files in the RootDir and another one in the subDir -- makes 3
        assertEquals(3, result.size());
    }


    @Test
    public void getAllFileListTest2() throws IOException, SftpException
    {

        serverOutput = "[drwxr-xr-x   8 s0569194 s0569194     4096 Jun  8 15:05 ., " + "-rw-r--r--   1 s0569194 s0569194   815311 Jun  8 12:26 testRequest.map, " + "drwxr-xr-x   2 s0569194 s0569194     4096 Jun  4 11:36 EastGermany, " + "-rw-r--r--   1 s0569194 s0569194  4718669 Jun  4 11:35 ohdm.map]";
        String serverOutputSubDir = "[drwxr-xr-x   8 s0569194 s0569194     4096 Jun  8 15:05 ., " + "-rw-r--r--   1 s0569194 s0569194  4718669 Jun  4 11:35 hamburg.map]";
        Vector mockVecRoot = Mockito.mock(Vector.class);
        Vector mockVecSubDir = Mockito.mock(Vector.class);
        Mockito.when(mockVecRoot.toString()).thenReturn(serverOutput);
        Mockito.when(mockVecSubDir.toString()).thenReturn(serverOutputSubDir);

        // i am using the FTP_ROO_PORT here, because its filtered in the RemoteDirectory.setFileName
        Mockito.when(mockChannel.ls(Variables.FTP_ROOT_DIRECTORY)).thenReturn(mockVecRoot);
        Mockito.when(mockChannel.ls(Variables.FTP_ROOT_DIRECTORY + "/" + "EastGermany" + "/")).thenReturn(mockVecSubDir);
        ArrayList<RemoteFile> result = testSftpClient.getAllFileList(Variables.FTP_ROOT_DIRECTORY);

        //Tere should be 2 Files in the RootDir and another one in the subDir -- makes 3
        assertEquals("testRequest.map", result.get(0).getFilename());
        assertEquals("ohdm.map", result.get(1).getFilename());
        assertEquals("hamburg.map", result.get(2).getFilename());
    }

}