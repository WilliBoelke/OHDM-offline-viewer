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

public class OhdmFileRecyclerAdapter extends RecyclerView.Adapter<OhdmFileRecyclerAdapter.OhdmFileViewHolder> implements Filterable
{
    /**
     * This list will be altered when the user searches for maps
     */
    private ArrayList<OhdmFile> mapArrayList;
    /**
     * This list will always contain all maps
     * Its here as a backup for the mapArrayList
     */
    private ArrayList<OhdmFile> mapArrayListBackup;
    private int ressource;
    private OnItemClickListener onItemClickListener;


    public OhdmFileRecyclerAdapter(Context context, ArrayList<OhdmFile> ohdmFiles, ArrayList<OhdmFile >mapArrayListBackup,  int ressource)
    {
        this.ressource = ressource;
        this.mapArrayList = ohdmFiles;
        Log.e(TAG, "Size... " + mapArrayList.size());
        this.mapArrayListBackup = mapArrayListBackup;// needs to be initialized like that so i dosnt point to the same list as mapArrayList
        Log.e(TAG, "Size... " + this.mapArrayListBackup.size());
    }

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
        OhdmFile currentOhdmFile = this.mapArrayList.get(position);
        String name = currentOhdmFile.getFilename();
        name = name.replace(".map", "");
        ohdmFileViewHolder.nameTextView.setText(name);
        ohdmFileViewHolder.sizeTextView.setText((int) (double) (currentOhdmFile.getFileSize() / 1024) + " KB");
        ohdmFileViewHolder.dateTextView.setText(currentOhdmFile.getCreationDate());
    }

    @Override
    public int getItemCount()
    {
        return mapArrayList.size();
    }

    public OhdmFile getFile(int position)
    {
        return this.mapArrayList.get(position);
    }

    public void setOnItemClickListener(OhdmFileRecyclerAdapter.OnItemClickListener listener)
    {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }

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
            Log.e(TAG, "Size--" + mapArrayListBackup.size());
            if (constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(mapArrayListBackup);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();
                Log.e(TAG, "Size _____" + mapArrayListBackup.size());
                for (OhdmFile map : mapArrayListBackup)
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
            mapArrayList.clear();
            mapArrayList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };


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