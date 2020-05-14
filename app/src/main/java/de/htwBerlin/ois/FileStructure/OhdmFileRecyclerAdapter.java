package de.htwBerlin.ois.FileStructure;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.htwBerlin.ois.R;

public class OhdmFileRecyclerAdapter extends RecyclerView.Adapter<OhdmFileRecyclerAdapter.OhdmFileViewHolder>
{
    private ArrayList<OhdmFile> mapArrayList;
    private Context context;
    private int ressource;
    private OnItemClickListener onItemClickListener;
    private ProgressBar progressBar;

    public OhdmFileRecyclerAdapter(Context context, ArrayList<OhdmFile> ohdmFiles, int ressource)
    {
        this.ressource = ressource;
        this.mapArrayList = ohdmFiles;
        this.context = context;
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
        ohdmFileViewHolder.nameTextView.setText(currentOhdmFile.getFilename());
        ohdmFileViewHolder.sizeTextView.setText(currentOhdmFile.getFileSize().toString());
        ohdmFileViewHolder.sizeTextView.setText(currentOhdmFile.getCreationDate());
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


    public void deleteTask(int position)
    {

        Toast.makeText(context, "Deleting " + getFile(position).getFilename(), Toast.LENGTH_SHORT).show();

        notifyDataSetChanged();
    }

    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }

    protected static class OhdmFileViewHolder extends RecyclerView.ViewHolder
    {

        public TextView nameTextView;
        public TextView sizeTextView;
        public TextView dateTextView;
        public ProgressBar progressBar;


        public OhdmFileViewHolder(@NonNull View itemView, final OhdmFileRecyclerAdapter.OnItemClickListener listener)
        {
            super(itemView);
            sizeTextView = itemView.findViewById(R.id.map_size_tv);
            nameTextView = itemView.findViewById(R.id.map_name_tv);
            dateTextView = itemView.findViewById(R.id.date_of_creation_tv);
            progressBar = itemView.findViewById(R.id.progressBar);


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