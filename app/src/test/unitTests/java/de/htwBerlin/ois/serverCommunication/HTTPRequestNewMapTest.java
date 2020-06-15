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
import java.util.ArrayList;

import de.htwBerlin.ois.fileStructure.RemoteDirectory;
import de.htwBerlin.ois.fileStructure.RemoteFile;

import static de.htwBerlin.ois.serverCommunication.HttpRequest.REQUEST_TYPE_MAP_REQUEST;
import static org.junit.Assert.assertEquals;

class HTTPRequestNewMapTest
{
    PipedInputStream pipeInput;
    private HttpRequest httpRequestNewMap;
    ;
    private HttpURLConnection mockHTTPConn = Mockito.mock(HttpURLConnection.class);
    private BufferedReader reader;
    private BufferedOutputStream out;

    @BeforeEach
    public void setup() throws IOException
    {
        httpRequestNewMap = new HttpRequest(REQUEST_TYPE_MAP_REQUEST, "name=mapname&coords=13.005,15.123_13.005,15.123_13.005,15.123_13.005,15.123_13.005,15.123&date=2117-12-11&id=1JWd3wc", new AsyncResponse()
        {
            @Override
            public void getRemoteFiles(ArrayList<RemoteFile> remoteFiles)
            {

            }

            @Override
            public void getRemoteDirectories(ArrayList<RemoteDirectory> remoteDirectories)
            {

            }

            @Override
            public void getHttpResponse(String response)
            {

            }
        });
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
        assertEquals("name=mapname&coords=13.005,15.123_13.005,15.123_13.005,15.123_13.005,15.123_13.005,15.123&date=2117-12-11&id=1JWd3wc", s);
    }
}