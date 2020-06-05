package de.htwBerlin.ois.ServerCommunication;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FtpClientTest
{

    FTPClient mockFTPClient = Mockito.mock(FTPClient.class);
    FtpClient ftpClient;

    @BeforeEach
    public void setup()
    {
        ftpClient = new FtpClient(mockFTPClient);
    }


    @Test
    public void connectTest()
    {
        //the FtpClient was already connected because i used the test constructor
        assertEquals(5, ftpClient.connect());
    }
}