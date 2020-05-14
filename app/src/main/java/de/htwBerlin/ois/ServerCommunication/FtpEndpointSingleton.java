package de.htwBerlin.ois.ServerCommunication;

public class FtpEndpointSingleton
{

    private static FtpEndpointSingleton ftpEndpointSingleton = null;
    private String serverIp;
    private Integer serverPort;
    private String ftpUser;
    private String ftpPassword;

    private FtpEndpointSingleton()
    {

    }

    public static FtpEndpointSingleton getFtpEndpointSingleton()
    {
        return ftpEndpointSingleton;
    }

    public static void setFtpEndpointSingleton(FtpEndpointSingleton ftpEndpointSingleton)
    {
        FtpEndpointSingleton.ftpEndpointSingleton = ftpEndpointSingleton;
    }

    public static FtpEndpointSingleton getInstance()
    {
        if (ftpEndpointSingleton == null) ftpEndpointSingleton = new FtpEndpointSingleton();
        return ftpEndpointSingleton;
    }

    protected String getServerIp()
    {
        return serverIp;
    }

    public void setServerIp(String serverIp)
    {
        this.serverIp = serverIp;
    }

    protected Integer getServerPort()
    {
        return serverPort;
    }

    public void setServerPort(Integer serverPort)
    {
        this.serverPort = serverPort;
    }

    protected String getFtpUser()
    {
        return ftpUser;
    }

    public void setFtpUser(String ftpUser)
    {
        this.ftpUser = ftpUser;
    }

    protected String getFtpPassword()
    {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword)
    {
        this.ftpPassword = ftpPassword;
    }
}
