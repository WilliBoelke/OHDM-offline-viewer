package de.htwBerlin.ois.model.repositories.localRepositories;

import android.os.Environment;

/**
 * @author WilliBoelke
 */
public class Variables
{

    private Variables()
    {
        //ot to be initialized
    }

    public static final String MAP_FILE_PATH = Environment.getExternalStorageDirectory().toString() + "/OHDM";


    //------------Server------------

    public static final String MOST_RECENT_PATH = "/last_requests";
    /**
     * The (S)FTP root directory
     */
    public static final String FTP_ROOT_DIRECTORY = "/";
    /**
     * The remote server ip
     * (For localhost in android emulator set to "10.0.2.2")
     */
    public static final String SERVER_IP = "ohm.f4.htw-berlin.de";
    /**
     * The remote server port for HTTP requests
     */
    public static final int HTTP_PORT = 5001;
    /**
     * the remote server port for ftp requests
     */
    public static final int FTP_PORT = 5000;
    /**
     * the remote server port for sftp requests
     */
    public static final int SFTP_PORT = 5002;
    /**
     * The username for ftp requests
     */
    public static final String USER_NAME = "ohdmOffViewer";
    /**
     * The remote server user password
     */
    public static final String USER_PASSWORD = "H!3r0glyph Sat3llite Era$er";


    //------------Permissions------------

    public static final int REQUEST_CODE_ACCESS_LOCATION_PERMISSION = 34;
    public static final int REQUEST_CODE_WRITE_STORAGE_PERMISSION = 56;
    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

}
