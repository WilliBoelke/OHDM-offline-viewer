package de.htwBerlin.ois.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
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
import de.htwBerlin.ois.ui.recyclerAdapters.RecyclerAdapterRemoteFiles;
import de.htwBerlin.ois.ui.recyclerAdapters.RecyclerAdapterRequestStatus;

import static de.htwBerlin.ois.serverCommunication.HttpClient.RESPONSE_NO_CONNECTION;
import static de.htwBerlin.ois.serverCommunication.HttpClient.RESPONSE_NO_REQUESTS;
import static de.htwBerlin.ois.serverCommunication.HttpRequest.REQUEST_TYP_STATUS_BY_ID;
import static de.htwBerlin.ois.ui.fragments.FragmentOptions.SERVER_ID;
import static de.htwBerlin.ois.ui.fragments.FragmentOptions.SETTINGS_SHARED_PREFERENCES;


public class FragmentRequestStatus extends FragmentWithServerConnection
{

    //------------Instance Variables------------

    /**
     * Fragment ID used to identify the fragment
     * (for example by putting the ID into the Intent extra )
     */
    private View view;
    private ArrayList<String> requests;
    private ArrayList<String> requestsBackup;
    private RecyclerAdapterRequestStatus adapterRequestStatus;
    private HttpClient httpClient;
    private final String TAG = getClass().getSimpleName();
    private RecyclerView statusRecycler;


    //------------Static Variables------------

    public static String ID = "RequestStatus";


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
        view = inflater.inflate(R.layout.fragment_request_status, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        requests = new ArrayList<>();
        requestsBackup = new ArrayList<>();
        setupRecyclerView();
        changeVisibilities(STATE_CONNECTING);
        httpRequestStatus();
        setupSwipeToRefresh();
    }


    //------------Setup Views------------

    private void setupRecyclerView()
    {
        statusRecycler = view.findViewById(R.id.status_recycler_view);
        adapterRequestStatus = new RecyclerAdapterRequestStatus(getActivity(), requests, requestsBackup, R.layout.recycler_item_request_status);
        RecyclerView.LayoutManager recyclerLayoutManager = new LinearLayoutManager(getActivity());
        statusRecycler.setLayoutManager(recyclerLayoutManager);
        statusRecycler.setAdapter(adapterRequestStatus);
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
                changeVisibilities(STATE_CONNECTING);
                httpRequestStatus();
                adapterRequestStatus.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    /**
     * Setup the search view to use the nameFilter
     * implemented in {@link RecyclerAdapterRemoteFiles}
     */
    private void setupGeneralSearchView(SearchView searchView)
    {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                adapterRequestStatus.getFilter().filter(newText);
                ;
                return false;
            }
        });
    }

    //------------HTTP------------

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
                if (response.equals(RESPONSE_NO_CONNECTION))
                {
                    changeVisibilities(STATE_NO_CONNECTION);
                }
                else if (response.equals(RESPONSE_NO_REQUESTS))
                {
                    onUnknownId();
                }
                else
                {
                    String formatted = response.replace("-", " ");
                    formatted = formatted.replace("[", "");
                    formatted = formatted.replace("]", "");
                    formatted = formatted.replace("\"", "");
                    StringTokenizer st = new StringTokenizer(formatted, ",");
                    requests.clear();
                    requestsBackup.clear();
                    while (st.hasMoreTokens())
                    {
                        requests.add(st.nextToken());
                    }
                    requestsBackup.addAll(requests);
                    adapterRequestStatus.notifyDataSetChanged();
                    changeVisibilities(STATE_CONNECTED);
                }
            }
        });

        httpRequest.execute();
    }


    //------------Fragment With Server Connection Methods------------

    @Override
    protected void onNoConnection()
    {
        view.findViewById(R.id.connecting_tv).setVisibility(View.VISIBLE);
        ((TextView) view.findViewById(R.id.connecting_tv)).setText("Couldn't make connection");

        view.findViewById(R.id.connecting_pb).setVisibility(View.INVISIBLE);
        statusRecycler.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onConnecting()
    {
        view.findViewById(R.id.connecting_tv).setVisibility(View.VISIBLE);
        view.findViewById(R.id.connecting_pb).setVisibility(View.VISIBLE);

        statusRecycler.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onConnected()
    {
        view.findViewById(R.id.connecting_tv).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.connecting_pb).setVisibility(View.INVISIBLE);

        statusRecycler.setVisibility(View.VISIBLE);
    }

    private void onUnknownId()
    {
        view.findViewById(R.id.connecting_tv).setVisibility(View.VISIBLE);
        ((TextView) view.findViewById(R.id.connecting_tv)).setText("You haven't made any requests yet");

        view.findViewById(R.id.connecting_pb).setVisibility(View.INVISIBLE);
        statusRecycler.setVisibility(View.INVISIBLE);
    }


    //------------Toolbar Menu------------

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.actionbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.ab_menu_search).setVisible(true);
        setupGeneralSearchView((SearchView) searchItem.getActionView());
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.ab_menu_about:
                //no implemented here,
                return false;
            case R.id.ab_menu_faq:
                //no implemented here,
                return false;
            case R.id.ab_menu_settings:
                //no implemented here
                return false;
            case R.id.ab_menu_search:

        }
        return super.onOptionsItemSelected(item);
    }
}