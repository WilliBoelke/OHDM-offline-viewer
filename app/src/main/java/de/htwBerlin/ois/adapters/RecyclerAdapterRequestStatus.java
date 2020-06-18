package de.htwBerlin.ois.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.StringTokenizer;

import de.htwBerlin.ois.R;

public class RecyclerAdapterRequestStatus extends RecyclerView.Adapter<RecyclerAdapterRequestStatus.RequestStatusViewHolder> implements Filterable
{

    //------------Instance Variables------------

    /**
     * This list will be altered when the user searches for maps
     */
    private ArrayList<String> requests;

    /**
     * This list will always contain all requests
     * Its here as a backup for the requests list
     */
    private ArrayList<String> requestsBackup;

    /**
     * Resource id for the RecyclerItem layout
     */
    private int resource;

    /**
     * Context
     */
    private Context context;

    /**
     * The on itemClickListener
     */
    private RecyclerAdapterRequestStatus.OnItemClickListener onItemClickListener;

    private OnRecyclerItemButtonClicklistenner onButtonClickListener;


    //------------Constructors------------

    private Filter nameFilter = new Filter()
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            ArrayList<String> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(requestsBackup);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (String request : requestsBackup)
                {
                    if (request.toLowerCase().trim().contains(filterPattern))
                    {
                        filteredList.add(request);
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
            requests.clear();
            requests.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };


    //------------RecyclerViewAdapter Methods------------


    /**
     * Public constructor
     *
     * @param context
     */
    public RecyclerAdapterRequestStatus(Context context, ArrayList<String> requests, ArrayList<String> requestsBackup, int resource)
    {
        this.context = context;
        this.resource = resource;
        this.requests = requests;
        this.requestsBackup = requestsBackup;
    }


    //------------OnClickListener------------


    @Override
    public int getItemCount()
    {
        return requests.size();
    }


    /**
     * Setter for the implemented onItemClick method
     *
     * @param listener
     */
    public void setOnItemClickListener(RecyclerAdapterRequestStatus.OnItemClickListener listener)
    {
        this.onItemClickListener = listener;
    }

    public void setOnItemButtonClickListener(OnRecyclerItemButtonClicklistenner listener)
    {
        this.onButtonClickListener = listener;
    }


    //------------Filter (Name)------------

    @Override
    public Filter getFilter()
    {
        return nameFilter;
    }

    @NonNull
    @Override
    public RecyclerAdapterRequestStatus.RequestStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new RecyclerAdapterRequestStatus.RequestStatusViewHolder(view, this.onItemClickListener);
    }


    //------------View Holder------------

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterRequestStatus.RequestStatusViewHolder requestStatusViewHolder, final int position)
    {
        String currentRequest = this.requests.get(position);
        StringTokenizer st = new StringTokenizer(currentRequest, " ");
        requestStatusViewHolder.nameTextView.setText(st.nextToken());
        String status = st.nextToken();
        requestStatusViewHolder.statusTextView.setText(status);
        requestStatusViewHolder.timeTextView.setText(st.nextToken());

        switch (status)
        {
            case "DONE":
                requestStatusViewHolder.statusInfoTextView.setText(R.string.status_info_done);
                break;
            case "ERROR":
                requestStatusViewHolder.statusInfoTextView.setText(R.string.status_info_error);
                break;
            case "DOWNLOADING":
                requestStatusViewHolder.statusInfoTextView.setText(R.string.status_info_downloading);
                break;
            case "CONVERTING":
                requestStatusViewHolder.statusInfoTextView.setText(R.string.status_info_converting);
                break;
            case "REQUESTED":
                requestStatusViewHolder.statusInfoTextView.setText(R.string.status_info_requesting);
                break;
        }
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

    protected static class RequestStatusViewHolder extends RecyclerView.ViewHolder
    {

        public TextView nameTextView;
        public TextView statusTextView;
        public TextView timeTextView;
        public TextView statusInfoTextView;

        public RequestStatusViewHolder(@NonNull View itemView, final RecyclerAdapterRequestStatus.OnItemClickListener listener)
        {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.request_name_tv);
            statusTextView = itemView.findViewById(R.id.request_status_tv);
            timeTextView = itemView.findViewById(R.id.passed_time_tv);
            statusInfoTextView = itemView.findViewById(R.id.request_status_information);

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
