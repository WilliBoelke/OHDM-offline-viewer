package de.htwBerlin.ois.ui.recyclerAdapters;

import android.content.Context;
import android.util.Log;
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

import de.htwBerlin.ois.fileStructure.RemoteFile;
import de.htwBerlin.ois.R;


/**
 * The RecyclerViewAdapter for ohdmFiles (->means files from the FTP server)
 *
 * @author WilliBölke
 */
public class RecyclerAdapterRemoteFiles extends RecyclerView.Adapter<RecyclerAdapterRemoteFiles.OhdmFileViewHolder> implements Filterable
{

    //------------Instance Variables------------

    /**
     * This list will be altered when the user searches for maps
     */
    private ArrayList<RemoteFile> ohdmFiles;
    /**
     * This list will always contain all maps
     * Its here as a backup for the mapArrayList
     */
    private ArrayList<RemoteFile> ohdmFilesBackup;
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

    private OnRecyclerItemButtonClicklistenner onButtonClickListener;


    //------------Constructors------------


    /**
     * Public constructor
     *
     * @param context
     * @param ohdmFiles
     * @param mapArrayListBackup
     * @param ressource
     */
    public RecyclerAdapterRemoteFiles(Context context, ArrayList<RemoteFile> ohdmFiles, ArrayList<RemoteFile> mapArrayListBackup, int ressource)
    {
        this.context = context;
        this.ressource = ressource;
        this.ohdmFiles = ohdmFiles;
        this.ohdmFilesBackup = mapArrayListBackup;
    }


    //------------RecyclerViewAdapter Methods------------


    @Override
    public int getItemCount()
    {
        return ohdmFiles.size();
    }


    //------------OnClickListener------------


    /**
     * Setter for the implemented onItemClick method
     *
     * @param listener
     */
    public void setOnItemClickListener(RecyclerAdapterRemoteFiles.OnItemClickListener listener)
    {
        this.onItemClickListener = listener;
    }

    public void setOnItemButtonClickListener(OnRecyclerItemButtonClicklistenner listener)
    {
        this.onButtonClickListener = listener;
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
                filteredList.addAll(ohdmFilesBackup);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (RemoteFile map : ohdmFilesBackup)
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
            ohdmFiles.clear();
            ohdmFiles.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };


    //------------View Holder------------

    @NonNull
    @Override
    public OhdmFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(ressource, parent, false);
        return new RecyclerAdapterRemoteFiles.OhdmFileViewHolder(view, this.onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterRemoteFiles.OhdmFileViewHolder ohdmFileViewHolder, final int position)
    {
        RemoteFile currentOhdmFile = this.ohdmFiles.get(position);
        String name = currentOhdmFile.getFilename();
        name = name.replace(".map", "");
        ohdmFileViewHolder.nameTextView.setText(name);
        ohdmFileViewHolder.sizeTextView.setText((int) (double) (currentOhdmFile.getFileSize() / 1024) + " KB");
        ohdmFileViewHolder.dateTextView.setText(currentOhdmFile.getCreationDate());
        ohdmFileViewHolder.downloadbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onButtonClickListener.onButtonClick(position);
            }
        });
    }

    protected static class OhdmFileViewHolder extends RecyclerView.ViewHolder
    {

        public TextView nameTextView;
        public TextView sizeTextView;
        public TextView dateTextView;
        public Button downloadbutton;

        public OhdmFileViewHolder(@NonNull View itemView, final RecyclerAdapterRemoteFiles.OnItemClickListener listener)
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