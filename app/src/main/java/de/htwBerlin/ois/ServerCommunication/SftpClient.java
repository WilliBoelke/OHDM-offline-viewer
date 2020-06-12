package de.htwBerlin.ois.serverCommunication;


import android.util.Log;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.htwBerlin.ois.fileStructure.RemoteDirectory;
import de.htwBerlin.ois.fileStructure.RemoteFile;

import static de.htwBerlin.ois.serverCommunication.Variables.SERVER_IP;
import static de.htwBerlin.ois.serverCommunication.Variables.SFTP_PORT;
import static de.htwBerlin.ois.serverCommunication.Variables.USER_NAME;
import static de.htwBerlin.ois.serverCommunication.Variables.USER_PASSWORD;
import static de.htwBerlin.ois.ui.mainActivity.MainActivity.MAP_FILE_PATH;


public class SftpClient
{

    //------------Instance Variables------------

    private final String TAG = getClass().getSimpleName();
    private Session session;
    private ChannelSftp channel;
    private String output;
    private String[] usableOutput;


    //------------Connection------------

    private static String readDate(String[] currentSplit)
    {
        //Log.d(TAG, "readDate :  reading and formatting date ... ");
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

    public int connect()
    {
        try
        {
            Log.d(TAG, "connect :  trying to open SSh connection to " + SERVER_IP + ":" + SFTP_PORT + " ...");

            //Opens ssh connection
            session = (new JSch()).getSession(USER_NAME, SERVER_IP, SFTP_PORT);
            session.setPassword(USER_PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Log.d(TAG, "connect :  established SSH connection " + SERVER_IP + ":" + SFTP_PORT);
        }
        catch (JSchException ex)
        {
            Log.e(TAG, "connect :  failed to establish SSH connection " + SERVER_IP + ":" + SFTP_PORT + " maybe the loin credentials where wrong - check them in Variables");
            ex.printStackTrace();
            return 2;
        }
        try
        {
            //SFTP channel from ssh session
            Log.d(TAG, "connect :  trying to open  sftp channel .... ");
            channel = (ChannelSftp) session.openChannel("sftp");
            if (channel == null)
            {
                //Trying to close existing channel and try again
                channel.exit();
            }
            channel.connect();
        }
        catch (JSchException ex)
        {
            channel.exit();
            Log.e(TAG, "connect :  couldnt open the channel ");
            ex.printStackTrace();
            return 3;
        }
        Log.d(TAG, "connect :  Successfully connected and opened SFTP channel ");
        return 0;
    }

    public boolean isConnected()
    {
        return session.isConnected() && channel.isConnected();
    }


    //------------Processing Data From Server------------

    public void closeConnection()
    {
        Log.d(TAG, "closeConnection :  tring to close the connection .... ");
        try
        {
            channel.exit();
            session.disconnect();
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * We are getting a string from the Server and need to make it readable/ usable
     *
     * @param path
     * @throws SftpException
     */
    private void updateOutput(String path) throws SftpException
    {
        Log.d(TAG, "updateOutput :  updating output ... ");
        output = channel.ls(path).toString();
        usableOutput = analyseOutput();
        Log.d(TAG, "updateOutput :  output updated");
    }

    private String[] analyseOutput()
    {
        Log.d(TAG, "analyseOutput :  abalysing server output ... ");
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


    //------------Listing------------

    public ArrayList<RemoteFile> getAllFileList(String path) throws IOException
    {
        Log.d(TAG, " getAllFileList ");
        if (!this.isConnected())
        {
            Log.e(TAG, "getAllFileList : wasnt connected to server, call connect() first");
            return null;
        }
        Log.d(TAG, " getAllFileList : getting file list for " + path + " ...");
        ArrayList<RemoteFile> files = this.getFileList(path);
        ArrayList<RemoteDirectory> dirs = this.getDirList(path);

        Log.d(TAG, " getAllFileList : getting files :  ");
        for (RemoteDirectory d : dirs)
        {
            String subPath = d.getPath();
            ArrayList<RemoteFile> subFiles = getAllFileList(subPath);

            for (RemoteFile f : subFiles)
            {
                files.add(f);
                Log.d(TAG, " getAllFileList :  ----- " + f.getFilename());
            }
        }
        Log.d(TAG, " getAllFileList : got " + files.size() + "files from " + path);
        return files;
    }

    public ArrayList<RemoteDirectory> getDirList(String path)
    {
        Log.d(TAG, " getDirList ");
        if (!this.isConnected())
        {
            Log.e(TAG, "getDirList : wasnt connected to server, call connect() first");
            return null;
        }
        try
        {
            updateOutput(path);
        }
        catch (SftpException e)
        {
            Log.d(TAG, " getDirList : an error occurred while updating the output : cannot talk to Server anymore");
        }

        ArrayList<RemoteDirectory> remoteDirectories = new ArrayList<>();

        Log.d(TAG, " getDirList : Splitting output");
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
            {
                remoteDirectories.add(new RemoteDirectory(path + "/" + cleanedSplit[cleanedSplit.length - 1] + "/", readDate(cleanedSplit)));
            }
        }
        return remoteDirectories;
    }

    /**
     * @param path
     * @return
     * @throws IOException
     */
    public ArrayList<RemoteFile> getFileList(String path) throws IOException
    {
        Log.d(TAG, " getFileList ");
        if (!this.isConnected())
        {
            Log.e(TAG, "getFileList : wasnt connected to server, call connect() first");
            return null;
        }
        try
        {
            updateOutput(path);
        }
        catch (SftpException e)
        {
            Log.e(TAG, "getFileList : SftpException occurred");
        }
        ArrayList<RemoteFile> remoteFiles = new ArrayList<>();
        Log.d(TAG, "getFileList :  getting files from output ... ");
        Log.d(TAG, "getFileList :  splitting output ... ");
        for (String s : usableOutput)
        {
            String[] currentSplit = s.split(" ");
            Log.d(TAG, "getFileList :  currentSplit  =  " + currentSplit);

            Log.d(TAG, "getFileList :  currentSplit  =  ");
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
                remoteFiles.add(new RemoteFile(cleanedSplit[cleanedSplit.length - 1], path, Long.parseLong(cleanedSplit[4]), readDate(cleanedSplit)));
            }
        }
        return remoteFiles;
    }


    //------------Downloading------------

    public boolean downloadFile(String remoteFileName, String downloadPath)
    {
        if (!this.isConnected())
        {
            Log.e(TAG, "downloadFile : wasnt connected to server, call connect() first");
            return false;
        }
        try
        {
            channel.get(downloadPath + "/" + remoteFileName, MAP_FILE_PATH);
        }
        catch (SftpException e)
        {
            return false;
        }
        return true;
    }


    //------------Testing------------

    /**
     * To insert mock objects for testing
     *
     * @param mockSession
     * @param mockChannel
     */
    public void insertMockObjects(Session mockSession, ChannelSftp mockChannel)
    {
        this.session = mockSession;
        this.channel = mockChannel;
    }
}