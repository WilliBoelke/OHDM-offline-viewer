package de.htwBerlin.ois.model.serverCommunication;

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

import de.htwBerlin.ois.serverCommunication.HttpClient;

import static de.htwBerlin.ois.serverCommunication.HttpRequest.REQUEST_TYPE_MAP_REQUEST;
import static org.junit.Assert.assertEquals;

class HttpClientTest
{
    private PipedInputStream pipeInput;
    private BufferedReader reader;
    private BufferedOutputStream out;
    private HttpURLConnection mockHTTPConn;
    private HttpClient client;
    private String paramsString;

    @BeforeEach
    public void setup() throws IOException
    {
        mockHTTPConn = Mockito.mock(HttpURLConnection.class);
        client = new HttpClient();
        paramsString = "name=mapname&coords=13.005,15.123_13.005,15.123_13.005,15.123_13.005,15.123_13.005,15.123&date=2117-12-11&id=1JWd3wc";
        pipeInput = new PipedInputStream();
        reader = new BufferedReader(new InputStreamReader(pipeInput));
        out = new BufferedOutputStream(new PipedOutputStream(pipeInput));


    }

    @Test
    public void verifyMethodCalls() throws IOException
    {
        Mockito.when(mockHTTPConn.getOutputStream()).thenReturn(out);
        client.insertMockHTTPConnection(mockHTTPConn);
        client.connect(REQUEST_TYPE_MAP_REQUEST);
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
        Mockito.when(mockHTTPConn.getOutputStream()).thenReturn(out);
        client.insertMockHTTPConnection(mockHTTPConn);
        client.connect(REQUEST_TYPE_MAP_REQUEST);
        client.sendRequest(paramsString);
        String s = reader.readLine();
        assertEquals(paramsString, s);
    }

}








