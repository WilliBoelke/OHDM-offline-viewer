package de.htwBerlin.ois.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

import de.htwBerlin.ois.R;
import de.htwBerlin.ois.adapters.OnRecyclerItemDownloadButtonClick;
import de.htwBerlin.ois.adapters.RecyclerAdapterRemoteDirectories;
import de.htwBerlin.ois.models.fileStructure.RemoteFile;
import de.htwBerlin.ois.serverCommunication.Client;
import de.htwBerlin.ois.viewModels.ViewModelMapDownloadCenterCategories;


public class FragmentDownloadCenterCategories extends FragmentWithServerConnection
{

    //------------Instance Variables------------

    /**
     * Log tag
     */
    private final String TAG = getClass().getSimpleName();
    /**
     * The View
     */
    private View view;
    /**
     * The RecyclerAdapter
     */
    private RecyclerAdapterRemoteDirectories recyclerViewAdapter;
    private ViewModelMapDownloadCenterCategories viewModel;
    private Client client;


    //------------Activity/Fragment Lifecycle------------


    public FragmentDownloadCenterCategories(Client client)
    {
        this.client = client;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        // inflating the view
        view = inflater.inflate(R.layout.fragment_map_download_categories, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ViewModelMapDownloadCenterCategories.class);
        viewModel.init();
        setHasOptionsMenu(true);

        //Setup Observers
        this.setupOnDirectoriesContentsChangeObserver();
        //Setup Views
        this.setupDirRecycler();
        this.setupToAllMapsButton();
        this.setupFAB();
        this.setupSwipeToRefresh();
        this.changeVisibilities(STATE_CONNECTING);
    }


    //------------Setup Observers------------

    private void setupOnDirectoriesContentsChangeObserver()
    {
        viewModel.getDirectoriesContents().observe(this.getViewLifecycleOwner(), new Observer<HashMap<String, ArrayList<RemoteFile>>>()
        {
            @Override
            public void onChanged(HashMap<String, ArrayList<RemoteFile>> dirContents)
            {
                if (dirContents.size() == 0)
                {
                    changeVisibilities(STATE_NO_CONNECTION);
                }
                else
                {
                    changeVisibilities(STATE_CONNECTED);
                    recyclerViewAdapter.setData(viewModel.getDirectories().getValue(), dirContents);
                }
            }
        });

    }


    //------------Setup Views------------

    /**
     * Setup for the directory recycler.
     * <p>
     * Each RecyclerItem represents a directory from the server,
     * Each RecyclerItem contains another recycler displaying the content of
     * he displayed Directory
     */
    private void setupDirRecycler()
    {
        RecyclerView dirRecycler = view.findViewById(R.id.directory_recycler);

        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this.getContext());

        //The recycler adapter
        recyclerViewAdapter = new RecyclerAdapterRemoteDirectories(getActivity().getApplicationContext(), viewModel.getDirectories().getValue(), viewModel.getDirectoriesContents().getValue(), R.layout.recycler_item_directory);
        recyclerViewAdapter.setOnItemButtonClickListener(new OnRecyclerItemDownloadButtonClick()
        {
            @Override
            public void onButtonClick(RemoteFile fileToDownload)
            {
                viewModel.downloadMap(fileToDownload);
            }
        });
        //Putting everything together
        dirRecycler.setLayoutManager(recyclerLayoutManager);
        dirRecycler.setAdapter(recyclerViewAdapter);
    }

    private void setupToAllMapsButton()
    {
        Button button = view.findViewById(R.id.button_all_maps);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentDownloadCenterAll.class, null).addToBackStack(null).commit();
            }
        });
    }


    private void setupSwipeToRefresh()
    {
        final SwipeRefreshLayout swipeToRefreshLayout = view.findViewById(R.id.swipe_to_refresh);
        swipeToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                swipeToRefreshLayout.setRefreshing(true);
                changeVisibilities(STATE_CONNECTING);
                viewModel.refresh();
                setupDirRecycler();
                swipeToRefreshLayout.setRefreshing(false);
            }
        });
    }

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


    //------------Save/Restore Instance State------------

    /**
     * private void storeFiles()
     * {
     * RemoteListsSingleton.getInstance().setDirectories(this.directoryList);
     * }
     * <p>
     * private void restoreFiles()
     * {
     * if (RemoteListsSingleton.getInstance().getDirectories().size() != 0)
     * {
     * this.directoryList.clear();
     * this.directoryList.addAll(RemoteListsSingleton.getInstance().getDirectories());
     * this.changeVisibilities(STATE_CONNECTED);
     * }
     * else
     * {
     * FTPGetDirectories();
     * recyclerViewAdapter.notifyDataSetChanged();
     * }
     * <p>
     * }
     **/


    //------------Fragment With Server Connection Methods------------
    @Override
    protected void onNoConnection()
    {
        view.findViewById(R.id.connecting_tv).setVisibility(View.VISIBLE);
        ((TextView) view.findViewById(R.id.connecting_tv)).setText("Coudnt make connection");

        view.findViewById(R.id.connecting_pb).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.directory_recycler).setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onConnecting()
    {
        view.findViewById(R.id.connecting_tv).setVisibility(View.VISIBLE);
        view.findViewById(R.id.connecting_pb).setVisibility(View.VISIBLE);

        view.findViewById(R.id.directory_recycler).setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onConnected()
    {
        view.findViewById(R.id.connecting_tv).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.connecting_pb).setVisibility(View.INVISIBLE);

        view.findViewById(R.id.directory_recycler).setVisibility(View.VISIBLE);

    }


    //------------Toolbar Menu------------

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.actionbar_menu, menu);
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
