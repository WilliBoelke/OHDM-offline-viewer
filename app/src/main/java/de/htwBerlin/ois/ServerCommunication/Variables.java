package de.htwBerlin.ois.ServerCommunication;

/**
 * @author WilliBoelke
 */
public  class Variables
{

    private Variables()
    {
        //ot to be initialized
    }

    /**
     * The remote server ip
     * (For localhost in android emulator set to "10.0.2.2")
     */
    protected static final String SERVER_IP = "141.45.146.200";
    /**
     * The remote server port for HTTP requests
     */
    protected static final int HTTP_PORT = 5001;
    /**
     * the remote server port for ftp requests
     */
    protected static final int FTP_Port = 5000;
    /**
     * The username for ftp requests
     */
    protected static final String USER_NAME = "ohdmOffViewer";
    /**
     * The remote server user password
     */
    protected static final String USER_PASSWORD = "H!3r0glyph Sat3llite Era$er";

    public static final String MOST_RECENT_PATH = "/most_recent";

    public static final String LAST_REQUEST_PATH = "/last_request";
}
