package de.htwBerlin.ois.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import de.htwBerlin.ois.R;
import de.htwBerlin.ois.adapters.OnRecyclerItemDownloadButtonClick;
import de.htwBerlin.ois.adapters.RecyclerAdapterRemoteFiles;
import de.htwBerlin.ois.models.fileStructure.RemoteFile;
import de.htwBerlin.ois.serverCommunication.Client;
import de.htwBerlin.ois.viewModels.ViewModelDownloadCenterAll;

/**
 * This Activity represents a small map file download center
 *
 * @author WilliBoelke
 */
public class FragmentDownloadCenterAll extends FragmentWithServerConnection
{

    //------------Instance Variables------------

    /**
     * Fragment ID used to identify the fragment
     * (for example by putting the ID into the Intent extra )
     */
    public static String ID = "MapDownload";
    /**
     * Log tag
     */
    private final String TAG = this.getClass().getSimpleName();
    private RecyclerAdapterRemoteFiles allRecyclerAdapter;
    private RecyclerAdapterRemoteFiles mostRecentMapsRecyclerAdapter;
    /**
     * The ViewModel of this Fragment
     */
    private ViewModelDownloadCenterAll viewModel;
    /**
     * The recyclerView
     */
    private RecyclerView allMapsRecyclerView;
    private RecyclerView mostRecentMapsRecyclerView;
    private Client client;


    //------------Static Variables------------
    /**
     * The view
     */
    private View view;

    //------------Constructor------------

    public FragmentDownloadCenterAll(Client client)
    {
        this.client = client;
    }


    //------------Activity/Fragment Lifecycle------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        // inflating the view
        view = inflater.inflate(R.layout.fragment_map_download_all, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ViewModelDownloadCenterAll.class);
        viewModel.init();

        setHasOptionsMenu(true);

        // Setup Observers
        this.onAllMapsChangeObserver();
        this.onMostRecentMapsChangeObserver();

        // Setup Views
        this.setupAllMapsRecyclerView();
        this.setupLatestMapsRecyclerView();
        this.setupLatestSearchView();
        this.setupAllSearchView();
        this.setupFAB();
        this.setupSwipeToRefresh();
        this.setupButtonToCategories();
    }


    //------------Setup Observers------------

    private void onAllMapsChangeObserver()
    {
        viewModel.getAllMaps().observe(this.getViewLifecycleOwner(), new Observer<ArrayList<RemoteFile>>()
        {
            @Override
            public void onChanged(ArrayList<RemoteFile> remoteFiles)
            {
                if (remoteFiles.size() == 0)
                {
                    changeVisibilities(STATE_CONNECTING);
                }
                else
                {
                    changeVisibilities(STATE_CONNECTED);
                    allRecyclerAdapter.setData(remoteFiles);
                }
            }
        });
    }

    private void onMostRecentMapsChangeObserver()
    {
        viewModel.getMostRecentMaps().observe(this.getViewLifecycleOwner(), new Observer<ArrayList<RemoteFile>>()
        {
            @Override
            public void onChanged(ArrayList<RemoteFile> remoteFiles)
            {
                if (remoteFiles.size() == 0)
                {
                    changeVisibilities(STATE_CONNECTING);
                }
                else
                {
                    changeVisibilities(STATE_CONNECTED);
                    mostRecentMapsRecyclerAdapter.setData(remoteFiles);
                }
            }
        });
    }

    //------------Setup Views------------

    /**
     * Setup the FloatingActionButton to replace this fragment with the
     * {@link FragmentRequestNewMap}
     */
    private void setupFAB()
    {
        FloatingActionButton requestNewMapFab = view.findViewById(R.id.request_new_map_fab);
        requestNewMapFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentRequestNewMap.class, null).addToBackStack(null).commit();
            }
        });
    }

    /**
     * Setup the AllMaps RecyclerView :
     * <p>
     * Setup Swipe gestures
     * Setup onItemClickListener
     */
    private void setupAllMapsRecyclerView()
    {
        allMapsRecyclerView = view.findViewById(R.id.all_maps_recycler);
        allMapsRecyclerView.setVisibility(View.INVISIBLE);  // is invisible till the server responds
        RecyclerView.LayoutManager recyclerLayoutManager = new LinearLayoutManager(this.getContext());//layout manager vor vertical scrolling recycler

        //The recycler adapter
        allRecyclerAdapter = new RecyclerAdapterRemoteFiles( viewModel.getAllMaps().getValue(), R.layout.recycler_item_vertical);

        //OnClickListener for the button inside the RecyclerView item layout
        allRecyclerAdapter.setOnItemButtonClickListener(new OnRecyclerItemDownloadButtonClick()
        {
            @Override
            public void onButtonClick(RemoteFile fileToDownload)
            {
                Toast.makeText(getContext(), "Download started", Toast.LENGTH_SHORT).show();
                viewModel.downloadMap(fileToDownload);
            }
        });
        allMapsRecyclerView.setLayoutManager(recyclerLayoutManager);
        allMapsRecyclerView.setAdapter(allRecyclerAdapter);
    }

    /**
     * Setup the Latest RecyclerView :
     * <p>
     * Setup Swipe gestures
     * Setup onItemClickListener
     */
    private void setupLatestMapsRecyclerView()
    {
        mostRecentMapsRecyclerView = view.findViewById(R.id.most_recent_maps_recycler);

        mostRecentMapsRecyclerView.setVisibility(View.INVISIBLE);  // is invisible till the server responds

        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this.getContext());//layout manager vor vertical scrolling recycler
        recyclerLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        //The recycler adapter
        mostRecentMapsRecyclerAdapter = new RecyclerAdapterRemoteFiles(viewModel.getMostRecentMaps().getValue(), R.layout.recycler_item_horizonal);

        mostRecentMapsRecyclerAdapter.setOnItemButtonClickListener(new OnRecyclerItemDownloadButtonClick()
        {
            @Override
            public void onButtonClick(RemoteFile fileToDownload)
            {
                Toast.makeText(getContext(), "Download started", Toast.LENGTH_SHORT).show();
                viewModel.downloadMap(fileToDownload);
            }
        });

        //Putting everything together
        mostRecentMapsRecyclerView.setLayoutManager(recyclerLayoutManager);
        mostRecentMapsRecyclerView.setAdapter(mostRecentMapsRecyclerAdapter);
    }

    private void setupButtonToCategories()
    {
        Button button = view.findViewById(R.id.button_categories);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentDownloadCenterCategories.class, null).addToBackStack(null).commit();
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
                allRecyclerAdapter.getFilter().filter(newText);
                mostRecentMapsRecyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    /**
     * Setup the search view to use the nameFilter
     * only in the LatestMapsRecycler
     * implemented in {@link RecyclerAdapterRemoteFiles}
     */
    private void setupAllSearchView()
    {
        SearchView searchView = view.findViewById(R.id.all_sv);
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
                allRecyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    /**
     * Setup the search view to use the nameFilter
     * only in the LatestMapsRecycler
     * implemented in {@link RecyclerAdapterRemoteFiles}
     */
    private void setupLatestSearchView()
    {
        SearchView searchView = view.findViewById(R.id.recent_sv);
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
                mostRecentMapsRecyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });
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
                viewModel.refresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    //------------FragmentWithServerConnection Methods------------

    @Override
    protected void onNoConnection()
    {
        view.findViewById(R.id.connecting_tv).setVisibility(View.VISIBLE);
        ((TextView) view.findViewById(R.id.connecting_tv)).setText("Coudnt make connection");

        view.findViewById(R.id.connecting_pb).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.all_tv).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.all_sv).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.recent_sv).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.lates_tv).setVisibility(View.INVISIBLE);
        allMapsRecyclerView.setVisibility(View.INVISIBLE);
        mostRecentMapsRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onConnecting()
    {
        view.findViewById(R.id.connecting_tv).setVisibility(View.VISIBLE);
        view.findViewById(R.id.connecting_pb).setVisibility(View.VISIBLE);

        view.findViewById(R.id.all_sv).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.all_tv).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.recent_sv).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.lates_tv).setVisibility(View.INVISIBLE);
        allMapsRecyclerView.setVisibility(View.INVISIBLE);
        mostRecentMapsRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onConnected()
    {
        view.findViewById(R.id.connecting_tv).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.connecting_pb).setVisibility(View.INVISIBLE);

        view.findViewById(R.id.all_sv).setVisibility(View.VISIBLE);
        view.findViewById(R.id.all_tv).setVisibility(View.VISIBLE);
        view.findViewById(R.id.recent_sv).setVisibility(View.VISIBLE);
        view.findViewById(R.id.lates_tv).setVisibility(View.VISIBLE);
        allMapsRecyclerView.setVisibility(View.VISIBLE);
        mostRecentMapsRecyclerView.setVisibility(View.VISIBLE);
    }

}
