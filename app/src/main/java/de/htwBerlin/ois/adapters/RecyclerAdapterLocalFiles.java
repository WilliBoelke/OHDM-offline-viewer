package de.htwBerlin.ois.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

import de.htwBerlin.ois.R;
import de.htwBerlin.ois.repositories.cacheRepostitories.MapFileSingleton;

/**
 * @author WilliBoelke
 */
public class RecyclerAdapterLocalFiles extends RecyclerView.Adapter<RecyclerAdapterLocalFiles.LocalMapsViewHolder> implements Filterable
{

    //------------Instance Variables------------

    /**
     * Log tag
     */
    private final String TAG = getClass().getSimpleName();
    /**
     * The ArrayList to be displayed by the RecyclerView
     * <p>
     * This list may be altered when the user searches for maps
     */
    private ArrayList<File> mapArrayList;
    /**
     * This list serves as Backup for the mapArrayList in case
     * it was altered through the Search/Filter
     */
    private ArrayList<File> mapArrayListBackup;
    /**
     * Context
     */
    private Context context;
    /**
     * The resource  id of the Recycleritem layout
     */
    private int resource;
    /**
     * onClickListener
     * use the {@link this#setOnItemClickListener(OnItemClickListener)}
     * to implement this
     */
    private RecyclerAdapterLocalFiles.OnItemClickListener onItemClickListener;


    //------------Constructors------------
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


    //------------RecyclerViewAdapter Methods------------

    public RecyclerAdapterLocalFiles(Context context, ArrayList<File> ohdmFiles, int ressource)
    {
        this.mapArrayListBackup = new ArrayList<>(ohdmFiles); // need to be initialized like that
        this.resource = ressource;
        this.mapArrayList = ohdmFiles;
        this.context = context;
    }

    @NonNull
    @Override
    public LocalMapsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
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


    //------------OnItemClickListener------------

    @Override
    public int getItemCount()
    {
        return mapArrayList.size();
    }

    /**
     * Setter for the implemented onItemClick method
     *
     * @param listener
     */
    public void setOnItemClickListener(RecyclerAdapterLocalFiles.OnItemClickListener listener)
    {
        this.onItemClickListener = listener;
    }


    //------------Filter (Name)------------

    @Override
    public Filter getFilter()
    {
        return nameFilter;
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

    protected static class LocalMapsViewHolder extends RecyclerView.ViewHolder
    {

        public TextView nameTextView;
        public TextView sizeTextView;
        public TextView dateTextView;
        public ImageView currentMapIcon;

        public LocalMapsViewHolder(@NonNull View itemView, final RecyclerAdapterLocalFiles.OnItemClickListener listener)
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
