package de.htwBerlin.ois.ServerCommunication;


import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SftpClientTest
{

    Session mockSession = Mockito.mock(Session.class);
    ChannelSftp mockChannel = Mockito.mock(ChannelSftp.class);
    SftpClient testSftpClient;

    @BeforeEach
    public void setup()
    {
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
        when(mockSession.isConnected()).thenReturn(true);
        when(mockChannel.isConnected()).thenReturn(true);

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




}