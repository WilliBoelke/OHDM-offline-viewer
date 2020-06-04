package de.htwBerlin.ois.ServerCommunication;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
        ftpClient.connect();
    }
}