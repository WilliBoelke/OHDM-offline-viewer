package de.htwBerlin.ois.serverCommunication;

/**
 * @author WilliBoelke
 */
public class Variables
{

    public static final String MOST_RECENT_PATH = "/map/most_recent";
    /**
     * The (S)FTP root directory
     */
    public static final String FTP_ROOT_DIRECTORY = "/map";
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
    protected static final int FTP_PORT = 5000;
    /**
     * the remote server port for sftp requests
     */
    protected static final int SFTP_PORT = 5002;
    /**
     * The username for ftp requests
     */
    protected static final String USER_NAME = "ohdmOffViewer";
    /**
     * The remote server user password
     */
    protected static final String USER_PASSWORD = "H!3r0glyph Sat3llite Era$er";

    private Variables()
    {
        //ot to be initialized
    }
}
