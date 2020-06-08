package de.htwBerlin.ois.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import de.htwBerlin.ois.FileStructure.RecyclerAdapterRemoteDirectories;
import de.htwBerlin.ois.FileStructure.RemoteDirectory;
import de.htwBerlin.ois.FileStructure.RemoteFile;
import de.htwBerlin.ois.FileStructure.RemoteListsSingleton;
import de.htwBerlin.ois.R;
import de.htwBerlin.ois.ServerCommunication.AsyncResponse;
import de.htwBerlin.ois.ServerCommunication.FtpTaskDirListing;

import static de.htwBerlin.ois.ServerCommunication.Variables.FTP_ROOT_DIRECTORY;


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
     * The list of directories from the FTP server
     */
    private ArrayList<RemoteDirectory> directoryList;
    /**
     * The RecyclerAdapter
     */
    private RecyclerAdapterRemoteDirectories recyclerViewAdapter;


    //------------Activity/Fragment Lifecycle------------
    /**
     * Code to be executed after the FtpTaskDirListing finished
     */
    private AsyncResponse asyncResponseDirListing = new AsyncResponse()
    {
        @Override
        public void getOhdmFiles(ArrayList<RemoteFile> remoteFiles)
        {

        }

        @Override
        public void getRemoteDirectories(ArrayList<RemoteDirectory> dirs)
        {
            if (dirs.size() != 0)
            {
                directoryList.clear();
                directoryList.addAll(dirs);
                recyclerViewAdapter.notifyDataSetChanged();
                changeVisibilities(STATE_CONNECTED);
            }
            else
            {
                changeVisibilities(STATE_NO_CONNECTION);
            }

        }
    };

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
        directoryList = new ArrayList<>();
        setHasOptionsMenu(true);
        this.setupDirRecycler();
        this.changeVisibilities(STATE_CONNECTING);
        this.setupToAllMapsButton();
        this.setupFAB();
        this.restoreFiles();
        this.setupSwipeToRefresh();
    }

    //------------Setup Views------------

    @Override
    public void onPause()
    {
        super.onPause();
        this.storeFiles();
    }

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
        recyclerViewAdapter = new RecyclerAdapterRemoteDirectories(getActivity().getApplicationContext(), directoryList, R.layout.recycler_item_directory);


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
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentDownloadCenterAll()).addToBackStack(null).commit();
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
                FTPGetDirectories();
                setupDirRecycler();
                swipeToRefreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * Setup the FloatingActionButton to replace this fragment with the
     * {@link FragmentrequestNewMap}
     */
    private void setupFAB()
    {
        FloatingActionButton requestNewMapFab = view.findViewById(R.id.request_new_map_fab);
        requestNewMapFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentrequestNewMap()).addToBackStack(null).commit();
            }
        });
    }

    private void FTPGetDirectories()
    {
        FtpTaskDirListing dirListing = new FtpTaskDirListing(getActivity(), FTP_ROOT_DIRECTORY, asyncResponseDirListing);
        dirListing.execute();
    }

    //------------Save/Restore Instance State------------

    private void storeFiles()
    {
        RemoteListsSingleton.getInstance().setDirectories(this.directoryList);
    }

    private void restoreFiles()
    {
        if (RemoteListsSingleton.getInstance().getDirectories().size() != 0)
        {
            this.directoryList.clear();
            this.directoryList.addAll(RemoteListsSingleton.getInstance().getDirectories());
            this.changeVisibilities(STATE_CONNECTED);
        }
        else
        {
            FTPGetDirectories();
            recyclerViewAdapter.notifyDataSetChanged();
        }

    }


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
}
