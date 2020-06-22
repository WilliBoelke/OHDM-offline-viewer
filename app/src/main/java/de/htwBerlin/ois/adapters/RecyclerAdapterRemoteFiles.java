package de.htwBerlin.ois.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.htwBerlin.ois.R;
import de.htwBerlin.ois.models.fileStructure.RemoteFile;


/**
 * RecyclerViewAdapter for remoteFiles (->means files from the S/FTP server)
 *
 * @author WilliBölke
 */
public class RecyclerAdapterRemoteFiles extends RecyclerView.Adapter<RecyclerAdapterRemoteFiles.RemoteFileFileViewHolder> implements Filterable
{

    //------------Instance Variables------------

    /**
     * This list will be altered when the user searches for maps
     */
    private ArrayList<RemoteFile> remoteFiles;
    /**
     * This list will always contain all maps
     * Its here as a backup for the mapArrayList
     */
    private ArrayList<RemoteFile> remoteFilesBackup;
    /**
     * Resource id for the RecyclerItem layout
     */
    private int resource;
    /**
     * The on itemClickListener
     */
    private OnItemClickListener onItemClickListener;
    /**
     * On click listener for the button inside the recycler view item
     */
    private OnRecyclerItemDownloadButtonClick onButtonClickListener;


    //------------Constructors------------

    /**
     * Public constructor
     *
     * @param remoteFiles
     * @param resource
     */
    public RecyclerAdapterRemoteFiles(ArrayList<RemoteFile> remoteFiles, int resource)
    {
        this.resource = resource;
        this.setData(remoteFiles);
    }


    //------------Setter------------

    /**
     * Should be used to refresh the displayed data when using
     * {@link androidx.lifecycle.LiveData} because i that case a simple
     * .notifyDataSetChanged wont work
     * @param remoteFiles
     */
    public void setData(ArrayList<RemoteFile> remoteFiles)
    {
        this.remoteFiles = remoteFiles;
        remoteFilesBackup = new ArrayList<>();
        this.remoteFilesBackup.addAll(remoteFiles);
        this.notifyDataSetChanged();
    }


    //------------OnClickListener------------

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

    /**
     * Setter for the implemented onItemClick method
     *
     * @param listener
     */
    public void setOnItemClickListener(RecyclerAdapterRemoteFiles.OnItemClickListener listener)
    {
        this.onItemClickListener = listener;
    }

    public void setOnItemButtonClickListener(OnRecyclerItemDownloadButtonClick listener)
    {
        this.onButtonClickListener = listener;
    }


    //------------RecyclerViewAdapter Methods------------

    @Override
    public int getItemCount()
    {
        return remoteFiles.size();
    }


    //------------Filter (Name)------------

    @Override
    public Filter getFilter()
    {
        return nameFilter;
    }

    private Filter nameFilter = new Filter()
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            ArrayList<RemoteFile> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(remoteFilesBackup);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (RemoteFile map : remoteFilesBackup)
                {
                    if (map.getFilename().toLowerCase().trim().contains(filterPattern))
                    {
                        filteredList.add(map);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            remoteFiles.clear();
            remoteFiles.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };


    //------------View Holder------------

    @NonNull
    @Override
    public RemoteFileFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new RemoteFileFileViewHolder(view, this.onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RemoteFileFileViewHolder ohdmFileViewHolder, final int position)
    {
        RemoteFile currentRemoteFileFile = this.remoteFiles.get(position);
        String name = currentRemoteFileFile.getFilename();
        name = name.replace(".map", "");
        ohdmFileViewHolder.nameTextView.setText(name);
        ohdmFileViewHolder.sizeTextView.setText((int) (double) (currentRemoteFileFile.getFileSize() / 1024) + " KB");
        ohdmFileViewHolder.dateTextView.setText(currentRemoteFileFile.getCreationDate());
        ohdmFileViewHolder.downloadbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onButtonClickListener.onButtonClick(currentRemoteFileFile);
            }
        });
    }

    protected static class RemoteFileFileViewHolder extends RecyclerView.ViewHolder
    {

        public TextView nameTextView;
        public TextView sizeTextView;
        public TextView dateTextView;
        public Button downloadbutton;

        public RemoteFileFileViewHolder(@NonNull View itemView, final RecyclerAdapterRemoteFiles.OnItemClickListener listener)
        {
            super(itemView);
            sizeTextView = itemView.findViewById(R.id.map_size_tv);
            nameTextView = itemView.findViewById(R.id.map_name_tv);
            dateTextView = itemView.findViewById(R.id.date_of_creation_tv);
            downloadbutton = itemView.findViewById(R.id.download_button);
            downloadbutton.setVisibility(View.VISIBLE);

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
}