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

public class RecyclerViewAdapterDirectories extends RecyclerView.Adapter<RecyclerViewAdapterDirectories.DirectoriesViewHolder>
{

    //------------Instance Variables------------

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
    public void onBindViewHolder(@NonNull RecyclerViewAdapterDirectories.DirectoriesViewHolder directoriesViewHolder, final int position)
    {
        RemoteDirectory currentDirectory = this.directoryList.get(position);
        String name = currentDirectory.getFilename();
        name = name.replace("_", " ");
        directoriesViewHolder.nameTextView.setText(name);

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


        public DirectoriesViewHolder(@NonNull View itemView, final RecyclerViewAdapterDirectories.OnItemClickListener listener)
        {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.dir_name_tv);

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