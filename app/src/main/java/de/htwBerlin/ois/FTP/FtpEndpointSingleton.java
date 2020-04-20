package de.htwBerlin.ois.FTP;

public class FtpEndpointSingleton {

    private String serverIp;
    private Integer serverPort;
    private String ftpUser;
    private String ftpPassword;
    private static FtpEndpointSingleton ftpEndpointSingleton = null;

    private FtpEndpointSingleton(){

    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getFtpUser() {
        return ftpUser;
    }

    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public static FtpEndpointSingleton getFtpEndpointSingleton() {
        return ftpEndpointSingleton;
    }

    public static void setFtpEndpointSingleton(FtpEndpointSingleton ftpEndpointSingleton) {
        FtpEndpointSingleton.ftpEndpointSingleton = ftpEndpointSingleton;
    }

    public static FtpEndpointSingleton getInstance(){
        if (ftpEndpointSingleton == null ) ftpEndpointSingleton = new FtpEndpointSingleton();
        return ftpEndpointSingleton;
    }
}
