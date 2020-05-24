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

import de.htwBerlin.ois.R;
import de.htwBerlin.ois.ServerCommunication.AsyncResponse;
import de.htwBerlin.ois.ServerCommunication.FtpTaskFileDownloading;
import de.htwBerlin.ois.ServerCommunication.FtpTaskFileListing;

import static de.htwBerlin.ois.ServerCommunication.Variables.MOST_RECENT_PATH;

public class RecyclerViewAdapterDirectories extends RecyclerView.Adapter<RecyclerViewAdapterDirectories.DirectoriesViewHolder>
{

    //------------Instance Variables------------
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
    /**
     * The on itemClickListener
     */
    private OnItemClickListener onItemClickListener;


    //------------Constructors------------

    /**
     * Public constructor
     *
     * @param context
     * @param directoryList
     * @param ressource
     */
    public RecyclerViewAdapterDirectories(Context context, ArrayList<RemoteDirectory> directoryList, int ressource)
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
        return new RecyclerViewAdapterDirectories.DirectoriesViewHolder(view, this.onItemClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapterDirectories.DirectoriesViewHolder directoriesViewHolder, final int position)
    {
        final RemoteDirectory currentDirectory = this.directoryList.get(position);
        String name = currentDirectory.getFilename();
        name = name.replace("_", " ");
        directoriesViewHolder.nameTextView.setText(name);


        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(context);//layout manager vor vertical scrolling recycler
        recyclerLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        //The recycler adapter
       RecyclerAdapterOhdmMaps latestRecyclerAdapter = new RecyclerAdapterOhdmMaps(context,  directoriesViewHolder.directoryContent,  directoriesViewHolder.directoryContentBackup, R.layout.recycler_item_horizonal);

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

        getDirectoryFiles(currentDirectory.getPath(),directoriesViewHolder.directoryContent,  directoriesViewHolder.directoryContentBackup, latestRecyclerAdapter);
    }

    @Override
    public int getItemCount()
    {
        return directoryList.size();
    }


    //------------OnClickListener------------

    /**
     * Setter for the implemented onItemClick method
     *
     * @param listener
     */
    public void setOnItemClickListener(RecyclerViewAdapterDirectories.OnItemClickListener listener)
    {
        this.onItemClickListener = listener;
    }

    /**
     * An interface to define the
     * onItemClick method
     * <p>
     * can be implemented and set as on itemClickListener through the
     * {@link this#setOnItemClickListener} method
     */
    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }


    //------------View Holder------------

    protected static class DirectoriesViewHolder extends RecyclerView.ViewHolder
    {

        public TextView nameTextView;
        public ArrayList<RemoteFile> directoryContent;
        public ArrayList<RemoteFile> directoryContentBackup;
        public RecyclerView dirContentRecycler;

        public DirectoriesViewHolder(@NonNull View itemView, final RecyclerViewAdapterDirectories.OnItemClickListener listener)
        {
            super(itemView);
            directoryContent = new ArrayList<>();
            directoryContentBackup = new ArrayList<>();
            nameTextView = itemView.findViewById(R.id.dir_name_tv);
            dirContentRecycler = itemView.findViewById(R.id.dir_content_recycler);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (listener != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    private void getDirectoryFiles(String path, final ArrayList<RemoteFile> list, final ArrayList<RemoteFile> backup, final RecyclerAdapterOhdmMaps adapter)
    {
        FtpTaskFileListing ftpTaskFileListing = new FtpTaskFileListing(context, path, new AsyncResponse()
        {
            @Override
            public void getOhdmFiles(ArrayList<RemoteFile> remoteFiles)
            {
                if (remoteFiles.size() > 0)
                {
                    Log.i(TAG, "received " + remoteFiles.size() + " files.");

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

            }
        });
        ftpTaskFileListing.execute();
    }
}