package de.htwBerlin.ois.FileStructure;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.htwBerlin.ois.Fragments.FragmentDownloadCenterCategories;
import de.htwBerlin.ois.R;
import de.htwBerlin.ois.ServerCommunication.AsyncResponse;
import de.htwBerlin.ois.ServerCommunication.FtpTaskFileDownloading;
import de.htwBerlin.ois.ServerCommunication.FtpTaskFileListing;


/**
 * A RecyclerView Adapter to display complete FTP directories.
 * <p>
 * Each element of the Recycler stands for a single directory
 * Each element has a name (TextView) which displays the name of the remote directory
 * Each element contains another RecyclerView -linting the files (maps) within the directory {@link RecyclerAdapterRemoteFiles}
 * <p>
 * used in :
 *
 * @author WilliBoelke
 * @see FragmentDownloadCenterCategories
 */
public class RecyclerAdapterRemoteDirectories extends RecyclerView.Adapter<RecyclerAdapterRemoteDirectories.DirectoriesViewHolder>
{

    //------------Instance Variables------------

    /**
     * log tag
     */
    private final String TAG = getClass().getSimpleName();
    /**
     * This list will be altered when the user searches for maps
     */
    private ArrayList<RemoteDirectory> directoryList;
    /**
     * Resource id for the RecyclerItem layout
     */
    private int ressource;
    /**
     * Context
     */
    private Context context;


    //------------Constructors------------

    /**
     * Public constructor
     *
     * @param context
     * @param directoryList
     * @param ressource
     */
    public RecyclerAdapterRemoteDirectories(Context context, ArrayList<RemoteDirectory> directoryList, int ressource)
    {
        this.context = context;
        this.ressource = ressource;
        this.directoryList = directoryList;
    }


    //------------RecyclerViewAdapter Methods------------

    @NonNull
    @Override
    public DirectoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(ressource, parent, false);
        return new RecyclerAdapterRemoteDirectories.DirectoriesViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerAdapterRemoteDirectories.DirectoriesViewHolder directoriesViewHolder, final int position)
    {
        final RemoteDirectory currentDirectory = this.directoryList.get(position);


        directoriesViewHolder.nameTextView.setText(currentDirectory.getFilename());

        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(context);//layout manager vor vertical scrolling recycler
        recyclerLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        //The recycler adapter
        RecyclerAdapterRemoteFiles latestRecyclerAdapter = new RecyclerAdapterRemoteFiles(context, directoriesViewHolder.directoryContent, directoriesViewHolder.directoryContentBackup, R.layout.recycler_item_horizonal);

        //on button click listener
        latestRecyclerAdapter.setOnItemButtonClickListener(new OnRecyclerItemButtonClicklistenner()
        {
            @Override
            public void onButtonClick(int position)
            {
                Toast.makeText(context, "Download started", Toast.LENGTH_SHORT).show();
                FtpTaskFileDownloading ftpTaskFileDownloading = new FtpTaskFileDownloading(context.getApplicationContext(), currentDirectory.getPath());
                ftpTaskFileDownloading.execute(directoriesViewHolder.directoryContent.get(position));
            }
        });


        //Putting everything together
        directoriesViewHolder.dirContentRecycler.setLayoutManager(recyclerLayoutManager);
        directoriesViewHolder.dirContentRecycler.setAdapter(latestRecyclerAdapter);

        restoreFiles(currentDirectory.getPath(), directoriesViewHolder.directoryContent, directoriesViewHolder.directoryContentBackup, latestRecyclerAdapter);
    }

    @Override
    public int getItemCount()
    {
        return directoryList.size();
    }


    //------------View Holder------------

    /**
     * This method retrieves the content/files for a single Directory from the FTPServer
     *
     * @param path    path of directory
     * @param list    the list to be filled through the async response
     * @param backup  the backup list to be filled through the async response
     * @param adapter the adapter -to notify when the data changes
     */
    private void ftpGetDirectoryContent(final String path, final ArrayList<RemoteFile> list, final ArrayList<RemoteFile> backup, final RecyclerAdapterRemoteFiles adapter)
    {
        FtpTaskFileListing ftpTaskFileListing = new FtpTaskFileListing(context, path, new AsyncResponse()
        {
            @Override
            public void getOhdmFiles(ArrayList<RemoteFile> remoteFiles)
            {
                if (remoteFiles.size() > 0)
                {
                    Log.i(TAG, "received " + remoteFiles.size() + " files.");
                    RemoteListsSingleton.getInstance().getDirectoryContents().put(path, remoteFiles);
                    list.addAll(remoteFiles);
                    backup.addAll(remoteFiles);
                    adapter.notifyDataSetChanged();
                }
                else // Server directory was empty or server hasn't responded
                {

                }
            }

            @Override
            public void getRemoteDirectories(ArrayList<RemoteDirectory> dirs)
            {
                //No need to be implemented here
            }
        });
        ftpTaskFileListing.execute();
    }


    //------------FTP------------

    /**
     * Checks if the list is persisted in the {@link RemoteListsSingleton}
     * if so adds it to the items list and notifies the adapter
     * <p>
     * else it will start the FTP task to download the list from the server
     *
     * @param path
     * @param list
     * @param backup
     * @param adapter
     */
    private void restoreFiles(final String path, final ArrayList<RemoteFile> list, final ArrayList<RemoteFile> backup, final RecyclerAdapterRemoteFiles adapter)
    {
        try
        {
            if (RemoteListsSingleton.getInstance().getDirectoryContents().get(path).size() != 0)
            {
                list.clear();
                list.addAll(RemoteListsSingleton.getInstance().getDirectoryContents().get(path));
                backup.clear();
                backup.addAll(list);
                adapter.notifyDataSetChanged();
            }
            else
            {
                ftpGetDirectoryContent(path, list, backup, adapter);
            }
        }
        catch (NullPointerException e)
        {
            ftpGetDirectoryContent(path, list, backup, adapter);
        }


    }

    //------------Save/Restore Instance State------------

    protected static class DirectoriesViewHolder extends RecyclerView.ViewHolder
    {

        public TextView nameTextView;
        public ArrayList<RemoteFile> directoryContent;
        public ArrayList<RemoteFile> directoryContentBackup;
        public RecyclerView dirContentRecycler;

        public DirectoriesViewHolder(@NonNull View itemView)
        {
            super(itemView);
            directoryContent = new ArrayList<>();
            directoryContentBackup = new ArrayList<>(); // the backup list is needed for the Search/Filter implementation in the RecyclerAdapterRemoteFiles
            nameTextView = itemView.findViewById(R.id.dir_name_tv);
            dirContentRecycler = itemView.findViewById(R.id.dir_content_recycler);
        }
    }


}