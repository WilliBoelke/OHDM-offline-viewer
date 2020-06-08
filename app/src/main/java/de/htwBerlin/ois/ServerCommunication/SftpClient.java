package de.htwBerlin.ois.ServerCommunication;


import android.util.Log;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.htwBerlin.ois.FileStructure.RemoteDirectory;
import de.htwBerlin.ois.FileStructure.RemoteFile;

import static de.htwBerlin.ois.ServerCommunication.Variables.SERVER_IP;
import static de.htwBerlin.ois.ServerCommunication.Variables.SFTP_PORT;
import static de.htwBerlin.ois.ServerCommunication.Variables.USER_NAME;
import static de.htwBerlin.ois.ServerCommunication.Variables.USER_PASSWORD;


public class SftpClient
{
    private Session session;
    private ChannelSftp channel;
    private final String TAG = getClass().getSimpleName();

    private String output;
    private String[] usableOutput;


    public int connect()
    {

        try
        {

            Log.d(TAG, "-------------------------------------------try to connect ");

            //Opens ssh connection
            session = (new JSch()).getSession(USER_NAME, SERVER_IP, SFTP_PORT);
            session.setPassword(USER_PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Log.d(TAG, "connected");
        }
        catch (JSchException ex)
        {
            Log.e(TAG, "-----------------ot connectedl---------------------");
            return 2;
            //Password oa. wrong
            //throw new IOException("Fehler beim SFTP-Connect mit '" + username + "' an '" + host + "': ", ex);
        }
        try
        {
            //SFTP channel from ssh session
            Log.e(TAG, "----------------- open channel---------------------");
            channel = (ChannelSftp) session.openChannel("sftp");
            if (channel == null)
            {
                //Trying to close existing channel and try again
                channel.exit();
                Log.e(TAG, "-----------------channel null---------------------");
                //throw new IOException( "Fehler beim Oeffnen des SFTP-Channel zur SFTP-Session mit '" + session.getUserName() + "' an '" + session.getHost() + "'. " );
            }
            channel.connect();
        }
        catch (JSchException ex)
        {
            channel.exit();
            Log.e(TAG, "-----------------channel null 2---------------------");
            return 3;
            //throw new IOException("Fehler beim Oeffnen des SFTP-Channel zur SFTP-Session mit '" + session.getUserName() + "' an '" + session.getHost() + "': ", ex);
        }
        Log.e(TAG, "-----------------success---------------------");
        return 0;
    }

    public boolean isConnected()
    {
        return session.isConnected() && channel.isConnected();
    }

    public void closeConnection()
    {
        try
        {
            channel.exit();
            session.disconnect();
        }
        catch (NullPointerException e)
        {
            return;
        }
        return;
    }


    /**
     * We getting a string from the Server and need to make it readable/ usable
     *
     * @param path
     * @throws SftpException
     */
    private void updateOutput(String path) throws SftpException
    {
        output = channel.ls(path).toString();
        usableOutput = analyseOutput();
    }

    private String[] analyseOutput()
    {
        String[] outputSplit;
        outputSplit = output.substring(1, output.length() - 1).split(",");
        for (int i = 0; i < outputSplit.length; i++)
        {
            outputSplit[i] = outputSplit[i].trim();

            if (outputSplit[i].endsWith(" ..") || outputSplit[i].endsWith(" ."))
                outputSplit[i] = null;
        }

        int counter = 0;
        for (int i = 0; i < outputSplit.length; i++)
        {
            if (!(outputSplit[i] == null))
            {
                outputSplit[counter] = outputSplit[i];
                counter++;
            }
        }

        String[] cleansedSplit = new String[outputSplit.length - (outputSplit.length - counter)];
        for (int i = 0; i < cleansedSplit.length; i++)
        {
            cleansedSplit[i] = outputSplit[i];
        }

        return cleansedSplit;
    }

    private static String readDate(String[] currentSplit)
    {
        int offset = 0;
        String date = "";
        try
        {
            date = Integer.parseInt(currentSplit[5]) + "-";
            offset++;
        }
        catch (NumberFormatException e)
        {
            date = new SimpleDateFormat("yyyy").format(System.currentTimeMillis()) + "-";
        }

        switch (currentSplit[5 + offset])
        {
            case "Jan":
                date += "01-";
                break;
            case "Feb":
                date += "02-";
                break;
            case "Mar":
                date += "03-";
                break;
            case "Apr":
                date += "04-";
                break;
            case "May":
                date += "05-";
                break;
            case "Jun":
                date += "06-";
                break;
            case "Jul":
                date += "07-";
                break;
            case "Aug":
                date += "08-";
                break;
            case "Sep":
                date += "09-";
                break;
            case "Okt":
                date += "10-";
                break;
            case "Nov":
                date += "11-";
                break;
            case "Dec":
                date += "12-";
                break;
        }

        try
        {
            date += Integer.parseInt(currentSplit[6 + offset]);
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        return date;
    }


    public ArrayList getAllFileList(String path) throws IOException
    {
        if (!session.isConnected())
        {
            //Log.e(TAG, "getAllFileList : wasnt connected to server, call connect() first");
            return null;
        }
        return null;
    }


    public boolean downloadFile(String remoteFileName, String downloadPath) throws IOException
    {
        try
        {
            channel.get(remoteFileName, downloadPath);
        }
        catch (SftpException e)
        {
            return false;
        }
        return true;
    }


    public RemoteDirectory[] getDirList(String path) throws IOException
    {
        try
        {
            updateOutput(path);
        }
        catch (SftpException e)
        {
            throw new IOException("cannot talk to Server anymore");
        }

        List<RemoteDirectory> remoteDirectories = new ArrayList<>();

        for (String s : usableOutput)
        {
            System.out.println(s);
        }

        for (String s : usableOutput)
        {
            String[] currentSplit = s.split(" ");

            int counter = 0;
            for (int i = 0; i < currentSplit.length; i++)
            {
                if (!(currentSplit[i].equals("")))
                {
                    currentSplit[counter] = currentSplit[i];
                    counter++;
                }
            }

            String[] cleanedSplit = new String[currentSplit.length - (currentSplit.length - counter)];
            for (int i = 0; i < cleanedSplit.length; i++)
            {
                cleanedSplit[i] = currentSplit[i];
            }

            if (currentSplit[0].startsWith("d"))
                remoteDirectories.add(new RemoteDirectory("" + cleanedSplit[cleanedSplit.length - 1], readDate(cleanedSplit)));

        }
        return remoteDirectories.toArray(new RemoteDirectory[remoteDirectories.size()]);
    }

    // path = "/"
    public RemoteFile[] getFileList(String path) throws IOException
    {
        try
        {
            updateOutput(path);
        }
        catch (SftpException e)
        {
            throw new IOException(e.toString());
        }
        List<RemoteFile> remoteFiles = new ArrayList<>();

        for (String s : usableOutput)
        {
            System.out.println(s);
        }

        for (String s : usableOutput)
        {
            String[] currentSplit = s.split(" ");

            int counter = 0;
            for (int i = 0; i < currentSplit.length; i++)
            {
                if (!(currentSplit[i].equals("")))
                {
                    currentSplit[counter] = currentSplit[i];
                    counter++;
                }
            }

            String[] cleanedSplit = new String[currentSplit.length - (currentSplit.length - counter)];
            for (int i = 0; i < cleanedSplit.length; i++)
            {
                cleanedSplit[i] = currentSplit[i];
            }

            if (currentSplit[0].startsWith("-"))
            {
                remoteFiles.add(new RemoteFile(cleanedSplit[cleanedSplit.length - 1], "", Long.parseLong(cleanedSplit[4]), readDate(cleanedSplit)));
            }
        }
        return remoteFiles.toArray(new RemoteFile[remoteFiles.size()]);
    }

}