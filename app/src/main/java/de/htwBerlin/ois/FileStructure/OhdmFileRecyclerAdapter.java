package de.htwBerlin.ois.FileStructure;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import de.htwBerlin.ois.R;

import static android.support.constraint.Constraints.TAG;

/**
 * The RecyclerViewAdapter for ohdmFiles (->means files from the FTP server)
 *
 * @author WilliBölke
 */
public class OhdmFileRecyclerAdapter extends RecyclerView.Adapter<OhdmFileRecyclerAdapter.OhdmFileViewHolder> implements Filterable
{

    //------------Instance Variables------------

    /**
     * This list will be altered when the user searches for maps
     */
    private ArrayList<OhdmFile> ohdmFiles;
    /**
     * This list will always contain all maps
     * Its here as a backup for the mapArrayList
     */
    private ArrayList<OhdmFile> ohdmFilesBackup;
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
     * @param ohdmFiles
     * @param mapArrayListBackup
     * @param ressource
     */
    public OhdmFileRecyclerAdapter(Context context, ArrayList<OhdmFile> ohdmFiles, ArrayList<OhdmFile> mapArrayListBackup, int ressource)
    {
        this.context = context;
        this.ressource = ressource;
        this.ohdmFiles = ohdmFiles;
        this.ohdmFilesBackup = mapArrayListBackup;
    }


    //------------RecyclerViewAdapter Methods------------

    @NonNull
    @Override
    public OhdmFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(ressource, parent, false);
        return new OhdmFileRecyclerAdapter.OhdmFileViewHolder(view, this.onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OhdmFileRecyclerAdapter.OhdmFileViewHolder ohdmFileViewHolder, int position)
    {
        OhdmFile currentOhdmFile = this.ohdmFiles.get(position);
        String name = currentOhdmFile.getFilename();
        name = name.replace(".map", "");
        ohdmFileViewHolder.nameTextView.setText(name);
        ohdmFileViewHolder.sizeTextView.setText((int) (double) (currentOhdmFile.getFileSize() / 1024) + " KB");
        ohdmFileViewHolder.dateTextView.setText(currentOhdmFile.getCreationDate());
    }

    @Override
    public int getItemCount()
    {
        return ohdmFiles.size();
    }


    //------------OnItemClickListener------------

    /**
     * Setter for the implemented onItemClick method
     *
     * @param listener
     */
    public void setOnItemClickListener(OhdmFileRecyclerAdapter.OnItemClickListener listener)
    {
        this.onItemClickListener = listener;
    }

    /**
     * An interface to define the
     * onItemClick method
     *
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
            ArrayList<OhdmFile> filteredList = new ArrayList<>();
            Log.e(TAG, "Size--" + ohdmFilesBackup.size());
            if (constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(ohdmFilesBackup);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (OhdmFile map : ohdmFilesBackup)
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


    protected static class OhdmFileViewHolder extends RecyclerView.ViewHolder
    {

        public TextView nameTextView;
        public TextView sizeTextView;
        public TextView dateTextView;

        public OhdmFileViewHolder(@NonNull View itemView, final OhdmFileRecyclerAdapter.OnItemClickListener listener)
        {
            super(itemView);
            sizeTextView = itemView.findViewById(R.id.map_size_tv);
            nameTextView = itemView.findViewById(R.id.map_name_tv);
            dateTextView = itemView.findViewById(R.id.date_of_creation_tv);

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