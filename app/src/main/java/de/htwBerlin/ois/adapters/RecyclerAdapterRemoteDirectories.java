package de.htwBerlin.ois.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import de.htwBerlin.ois.R;
import de.htwBerlin.ois.models.fileStructure.RemoteDirectory;
import de.htwBerlin.ois.models.fileStructure.RemoteFile;
import de.htwBerlin.ois.views.fragments.FragmentDownloadCenterCategories;


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
    private final Context context;
    /**
     * Resource id for the RecyclerItem layout
     */
    private final int resource;
    /**
     * This list will be altered when the user searches for maps
     */
    private ArrayList<RemoteDirectory> directories;

    private HashMap<String, ArrayList<RemoteFile>> dirContents;
    private OnRecyclerItemDownloadButtonClick onButtonClickListener;


    //------------Constructors------------

    /**
     * Public constructor
     *
     * @param context
     * @param resource
     */
    public RecyclerAdapterRemoteDirectories(Context context, ArrayList<RemoteDirectory> directories, HashMap<String, ArrayList<RemoteFile>> dirContents, int resource)
    {
        this.context = context;
        this.resource = resource;
        this.setDirectories(directories);
        this.setDirectoryContents(dirContents);
    }


    //------------Setter------------

    /**
     * Should be used to refresh the displayed data when using
     * {@link androidx.lifecycle.LiveData} because i that case a simple
     * .notifyDataSetChanged wont work
     * @param directories
     */
    public void setDirectories(ArrayList<RemoteDirectory> directories)
    {
        this.directories = directories;
        this.notifyDataSetChanged();
    }

    /**
     * Should be used to refresh the displayed data when using
     * {@link androidx.lifecycle.LiveData} because i that case a simple
     * .notifyDataSetChanged wont work
     * @param dirContents
     */
    public void setDirectoryContents(HashMap<String, ArrayList<RemoteFile>> dirContents)
    {
        this.dirContents = dirContents;
        this.notifyDataSetChanged();
    }


    //------------RecyclerViewAdapter Methods------------

    @Override
    public int getItemCount()
    {
        return directories.size();
    }


    //------------OnClickListener------------

    public void setOnItemButtonClickListener(OnRecyclerItemDownloadButtonClick listener)
    {
        this.onButtonClickListener = listener;
    }


    //------------View Holder------------

    @Override
    public void onBindViewHolder(@NonNull final RecyclerAdapterRemoteDirectories.DirectoriesViewHolder directoriesViewHolder, final int position)
    {
        final RemoteDirectory currentDirectory = this.directories.get(position);

        directoriesViewHolder.nameTextView.setText(currentDirectory.getFilename());

        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(context);//layout manager vor vertical scrolling recycler
        recyclerLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        //The recycler adapter
        RecyclerAdapterRemoteFiles latestRecyclerAdapter = new RecyclerAdapterRemoteFiles(dirContents.get(currentDirectory.getPath()), R.layout.recycler_item_horizonal);

        //on button click listener
        latestRecyclerAdapter.setOnItemButtonClickListener(new OnRecyclerItemDownloadButtonClick()
        {
            @Override
            public void onButtonClick(RemoteFile remoteFile)
            {
                Toast.makeText(context, "Download started", Toast.LENGTH_SHORT).show();
                onButtonClickListener.onButtonClick(remoteFile);
            }
        });


        //Putting everything together
        directoriesViewHolder.dirContentRecycler.setLayoutManager(recyclerLayoutManager);
        directoriesViewHolder.dirContentRecycler.setAdapter(latestRecyclerAdapter);
    }

    protected static class DirectoriesViewHolder extends RecyclerView.ViewHolder
    {

        public TextView nameTextView;
        public ArrayList<RemoteFile> directoryContentBackup;
        public RecyclerView dirContentRecycler;

        public DirectoriesViewHolder(@NonNull View itemView)
        {
            super(itemView);
            directoryContentBackup = new ArrayList<>(); // the backup list is needed for the Search/Filter implementation in the RecyclerAdapterRemoteFiles
            nameTextView = itemView.findViewById(R.id.dir_name_tv);
            dirContentRecycler = itemView.findViewById(R.id.dir_content_recycler);
        }
    }

    @NonNull
    @Override
    public DirectoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new RecyclerAdapterRemoteDirectories.DirectoriesViewHolder(view);
    }

}