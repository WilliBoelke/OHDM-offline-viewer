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
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import de.htwBerlin.ois.R;

import static android.support.constraint.Constraints.TAG;


public class LocalMapsRecyclerAdapter extends RecyclerView.Adapter<LocalMapsRecyclerAdapter.LocalMapsViewHolder> implements Filterable
{

    //------------Instance Variables------------

    private final String TAG = getClass().getSimpleName();
    private ArrayList<File> mapArrayList;
    private ArrayList<File> mapArrayListBackup;
    private Context context;
    private int ressource;
    private LocalMapsRecyclerAdapter.OnItemClickListener onItemClickListener;

    public LocalMapsRecyclerAdapter(Context context, ArrayList<File> ohdmFiles, int ressource)
    {
        this.mapArrayListBackup = new ArrayList<>(ohdmFiles); // need to be initialized like that
        this.ressource = ressource;
        this.mapArrayList = ohdmFiles;
        this.context = context;
    }

    @NonNull
    @Override
    public LocalMapsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(ressource, parent, false);
        LocalMapsViewHolder localMapsViewHolder = new LocalMapsViewHolder(view, this.onItemClickListener);
        return localMapsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LocalMapsViewHolder localMapsViewHolder, int position)
    {
        File currentMapFile = this.mapArrayList.get(position);
        String name = currentMapFile.getName();
        name = name.replace(".map", "");
        localMapsViewHolder.nameTextView.setText(name);
        localMapsViewHolder.sizeTextView.setText((int) (double) (currentMapFile.length() / 1024) + " KB");
        localMapsViewHolder.dateTextView.setText("11.11.1111");
        try
        {
            if (currentMapFile.getName().equals(MapFileSingleton.getInstance().getFile().getName()))
            {
                localMapsViewHolder.currentMapIcon.setVisibility(View.VISIBLE);
            }
            else
            {
                localMapsViewHolder.currentMapIcon.setVisibility(View.INVISIBLE);
            }
        }
        catch (NullPointerException e)
        {
            Log.e(TAG, "MapFileSingelton was null");
        }
    }

    @Override
    public int getItemCount()
    {
        return mapArrayList.size();
    }

    public void setOnItemClickListener(LocalMapsRecyclerAdapter.OnItemClickListener listener)
    {
        this.onItemClickListener = listener;
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
            ArrayList<File> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(mapArrayListBackup);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (File map : mapArrayListBackup)
                {
                    if (map.getName().toLowerCase().trim().contains(filterPattern))
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

    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }


    protected static class LocalMapsViewHolder extends RecyclerView.ViewHolder
    {

        public TextView nameTextView;
        public TextView sizeTextView;
        public TextView dateTextView;
        public ImageView currentMapIcon;

        public LocalMapsViewHolder(@NonNull View itemView, final LocalMapsRecyclerAdapter.OnItemClickListener listener)
        {
            super(itemView);
            sizeTextView = itemView.findViewById(R.id.map_size_tv);
            nameTextView = itemView.findViewById(R.id.map_name_tv);
            dateTextView = itemView.findViewById(R.id.date_of_creation_tv);
            currentMapIcon = itemView.findViewById(R.id.current_map_icon);


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
