package de.htwBerlin.ois.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.StringTokenizer;

import de.htwBerlin.ois.R;
import de.htwBerlin.ois.fileStructure.RemoteDirectory;
import de.htwBerlin.ois.fileStructure.RemoteFile;
import de.htwBerlin.ois.serverCommunication.AsyncResponse;
import de.htwBerlin.ois.serverCommunication.HttpClient;
import de.htwBerlin.ois.serverCommunication.HttpRequest;
import de.htwBerlin.ois.ui.recyclerAdapters.RecyclerAdapterRequestStatus;

import static de.htwBerlin.ois.serverCommunication.HttpRequest.REQUEST_TYP_STATUS_BY_ID;
import static de.htwBerlin.ois.ui.fragments.FragmentOptions.SERVER_ID;
import static de.htwBerlin.ois.ui.fragments.FragmentOptions.SETTINGS_SHARED_PREFERENCES;


public class FragmentRequestStatus extends Fragment
{

    //------------Instance Variables------------

    /**
     * Fragment ID used to identify the fragment
     * (for example by putting the ID into the Intent extra )
     */


    //------------Static Variables------------

    public static String ID = "RequestStatus";
    private View view;
    private ArrayList<String> requests;
    private ArrayList<String> requestsBackup;
    private RecyclerAdapterRequestStatus adapterRequestStatus;
    private HttpClient httpClient;


    //------------Constructors------------

    public FragmentRequestStatus(HttpClient httpClient)
    {
        this.httpClient = httpClient;
    }


    //------------Activity/Fragment Lifecycle------------

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_request_staus, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        requests = new ArrayList<>();
        requestsBackup = new ArrayList<>();
        setupRecyclerView();
        httpRequestStatus();
    }


    private void setupRecyclerView()
    {
        RecyclerView statusRecycler = view.findViewById(R.id.status_recycler_view);
        adapterRequestStatus = new RecyclerAdapterRequestStatus(getActivity(), requests, requestsBackup, R.layout.recycler_item_request_status);
        RecyclerView.LayoutManager recyclerLayoutManager = new LinearLayoutManager(getActivity());
        statusRecycler.setLayoutManager(recyclerLayoutManager);
        statusRecycler.setAdapter(adapterRequestStatus);
    }


    private void httpRequestStatus()
    {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setParams("id=" + getActivity().getSharedPreferences(SETTINGS_SHARED_PREFERENCES, 0).getString(SERVER_ID, null));
        httpRequest.setRequestType(REQUEST_TYP_STATUS_BY_ID);
        httpRequest.setHttpClient(httpClient);
        httpRequest.setAsyncResponse(new AsyncResponse()
        {

            @Override
            public void getRemoteFiles(ArrayList<RemoteFile> remoteFiles)
            {

            }

            @Override
            public void getRemoteDirectories(ArrayList<RemoteDirectory> remoteDirectories)
            {

            }

            @Override
            public void getHttpResponse(String response)
            {
                String formatted = response.replace("-", " ");
                formatted = formatted.replace("[", "");
                formatted = formatted.replace("]", "");
                formatted = formatted.replace("\"", "");
                StringTokenizer st = new StringTokenizer(formatted, ",");
                while (st.hasMoreTokens())
                {
                    requests.add(st.nextToken());
                }
                requestsBackup.addAll(requests);
                adapterRequestStatus.notifyDataSetChanged();
            }
        });

        httpRequest.execute();
    }


    //------------Swipe To Refresh------------

    private void setupSwipeToRefresh()
    {
        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                swipeRefreshLayout.setRefreshing(true);
                httpRequestStatus();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}