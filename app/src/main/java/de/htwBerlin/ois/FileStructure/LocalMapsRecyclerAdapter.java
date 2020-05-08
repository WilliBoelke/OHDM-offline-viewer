package de.htwBerlin.ois.FileStructure;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import de.htwBerlin.ois.R;


public class LocalMapsRecyclerAdapter extends RecyclerView.Adapter<LocalMapsRecyclerAdapter.LocalMapsViewHolder>
{
    private ArrayList<File> mapArrayList;
    private Context context;
    private int ressource;
    private LocalMapsRecyclerAdapter.OnItemClickListener onItemClickListener;

    public LocalMapsRecyclerAdapter(Context context, ArrayList<File> ohdmFiles, int ressource)
    {
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
        localMapsViewHolder.sizeTextView.setText(Long.toString(currentMapFile.length()));
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

    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }


    protected static class LocalMapsViewHolder extends RecyclerView.ViewHolder
    {

        public TextView nameTextView;
        public TextView sizeTextView;
        public TextView dateTextView;

        public LocalMapsViewHolder(@NonNull View itemView, final LocalMapsRecyclerAdapter.OnItemClickListener listener)
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
