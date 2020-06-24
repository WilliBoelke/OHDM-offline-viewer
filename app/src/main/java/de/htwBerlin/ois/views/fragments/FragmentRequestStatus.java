package de.htwBerlin.ois.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import de.htwBerlin.ois.R;
import de.htwBerlin.ois.adapters.RecyclerAdapterRemoteFiles;
import de.htwBerlin.ois.adapters.RecyclerAdapterRequestStatus;
import de.htwBerlin.ois.viewModels.ViewModelRequestStatus;


public class FragmentRequestStatus extends FragmentWithServerConnection
{

    //------------Instance Variables------------

    private View view;
    private RecyclerAdapterRequestStatus adapterRequestStatus;
    private final String TAG = getClass().getSimpleName();
    private RecyclerView statusRecycler;
    private ViewModelRequestStatus viewModel;


    //------------Activity/Fragment Lifecycle------------

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreate");
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_request_status, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d(TAG, "onActivityCreated : initializing ViewModel");
        viewModel = ViewModelProviders.of(this).get(ViewModelRequestStatus.class);
        viewModel.init();
        //Observer
        Log.d(TAG, "onActivityCreated : setup  observer");
        this.onRequestsChangeObserver();
        this.onConnectionChangeObservers();
        this.onResponseChangeObserver();
        //Views
        Log.d(TAG, "onActivityCreated : setup views");
        this.setupRecyclerView();
        this.setupSwipeToRefresh();
        viewModel.refresh();
    }


    //------------Observers------------

    private void onResponseChangeObserver()
    {
        viewModel.getResponse().observe(this.getViewLifecycleOwner(), new Observer<String>()
        {
            @Override
            public void onChanged(String response)
            {
                Log.d(TAG, "onResponseChangeObserver : called with Response = " + response);
                viewModel.processString(response);
            }
        });
    }

    private void onRequestsChangeObserver()
    {
        viewModel.getRequests().observe(this.getViewLifecycleOwner(), new Observer<ArrayList<String>>()
        {
            @Override
            public void onChanged(ArrayList<String> strings)
            {
                Log.d(TAG, "onRequestsChangeObserver : called with = " + strings.toString());
                adapterRequestStatus.setData(strings);
            }
        });
    }

    private void onConnectionChangeObservers()
    {
        viewModel.getNoConnectionBoolean().observe(this.getViewLifecycleOwner(), new Observer<Boolean>()
        {

            @Override
            public void onChanged(Boolean bool)
            {

                changeVisibilities(STATE_NO_CONNECTION);
                Log.d(TAG, "onConnectionChangeObservers :  noConnection called");
            }
        });

        viewModel.getNoRequestsForIdBoolean().observe(this.getViewLifecycleOwner(), new Observer<Boolean>()
        {

            @Override
            public void onChanged(Boolean bool)
            {
                onUnknownId();
                Log.d(TAG, "onConnectionChangeObservers :  noRequests called");
            }
        });

        viewModel.getRequestReceivedBoolean().observe(this.getViewLifecycleOwner(), new Observer<Boolean>()
        {
            @Override
            public void onChanged(Boolean bool)
            {
                changeVisibilities(STATE_CONNECTED);
                Log.d(TAG, "onConnectionChangeObservers :  requestsReceived called");
            }
        });

        viewModel.getConnectionBoolean().observe(this.getViewLifecycleOwner(), new Observer<Boolean>()
        {
            @Override
            public void onChanged(Boolean bool)
            {
                changeVisibilities(STATE_CONNECTING);
                Log.d(TAG, "onConnectionChangeObservers :  requestsReceived called");
            }
        });
    }


    //------------Setup Views------------

    private void setupRecyclerView()
    {
        statusRecycler = view.findViewById(R.id.status_recycler_view);
        adapterRequestStatus = new RecyclerAdapterRequestStatus(viewModel.getRequests().getValue(), R.layout.recycler_item_request_status);
        RecyclerView.LayoutManager recyclerLayoutManager = new LinearLayoutManager(getActivity());
        statusRecycler.setLayoutManager(recyclerLayoutManager);
        statusRecycler.setAdapter(adapterRequestStatus);
    }

    private void setupSwipeToRefresh()
    {
        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                swipeRefreshLayout.setRefreshing(true);
                viewModel.refresh();
                changeVisibilities(STATE_CONNECTING);
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
                return false;
            }
        });
    }


    //------------Fragment With Server Connection Methods------------

    @Override
    public void onNoConnection()
    {
        view.findViewById(R.id.connecting_tv).setVisibility(View.VISIBLE);
        ((TextView) view.findViewById(R.id.connecting_tv)).setText("Couldn't make connection");

        view.findViewById(R.id.connecting_pb).setVisibility(View.INVISIBLE);
        statusRecycler.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onConnecting()
    {
        view.findViewById(R.id.connecting_tv).setVisibility(View.VISIBLE);
        view.findViewById(R.id.connecting_pb).setVisibility(View.VISIBLE);

        statusRecycler.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onConnected()
    {
        Log.d(TAG, "onConnected:  change visibility ");
        view.findViewById(R.id.connecting_tv).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.connecting_pb).setVisibility(View.INVISIBLE);

        statusRecycler.setVisibility(View.VISIBLE);
    }

    public void onUnknownId()
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