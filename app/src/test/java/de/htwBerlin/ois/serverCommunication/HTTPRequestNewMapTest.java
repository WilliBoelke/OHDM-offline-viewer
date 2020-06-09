package de.htwBerlin.ois.serverCommunication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.HttpURLConnection;

import static org.junit.Assert.assertEquals;

class HTTPRequestNewMapTest
{
    private HTTPRequestNewMap httpRequestNewMap;
    private HttpURLConnection mockHTTPConn = Mockito.mock(HttpURLConnection.class);
    ;
    private BufferedReader reader;
    private BufferedOutputStream out;
    PipedInputStream pipeInput;


    @BeforeEach
    public void setup() throws IOException
    {
        httpRequestNewMap = new HTTPRequestNewMap("date", "coords", "name");
        pipeInput = new PipedInputStream();
        reader = new BufferedReader(new InputStreamReader(pipeInput));
        out = new BufferedOutputStream(new PipedOutputStream(pipeInput));
    }


    @Test
    public void verifyMethodCalls() throws IOException
    {
        Mockito.when(mockHTTPConn.getOutputStream()).thenReturn(out);

        httpRequestNewMap.insertMockHTTPConnection(mockHTTPConn);
        httpRequestNewMap.doInBackground();

        Mockito.verify(mockHTTPConn).setReadTimeout(15000);
        Mockito.verify(mockHTTPConn).setConnectTimeout(15000);
        Mockito.verify(mockHTTPConn).setRequestMethod("POST");
        Mockito.verify(mockHTTPConn).setDoInput(true);
        Mockito.verify(mockHTTPConn).setDoOutput(true);
    }

    @Test
    public void verifyTransmittedRequestString() throws IOException
    {
        //With that the buildParamsString method is covered as well
        httpRequestNewMap.insertMockHTTPConnection(mockHTTPConn);
        Mockito.when(mockHTTPConn.getOutputStream()).thenReturn(out);
        httpRequestNewMap.doInBackground();
        String s = reader.readLine();
        assertEquals("name=name&coords=coords&date=date", s);
    }
}