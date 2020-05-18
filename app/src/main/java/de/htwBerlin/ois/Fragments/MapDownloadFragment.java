package de.htwBerlin.ois.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import de.htwBerlin.ois.FileStructure.LeftSwipeCallback;
import de.htwBerlin.ois.FileStructure.OhdmFile;
import de.htwBerlin.ois.FileStructure.OhdmFileRecyclerAdapter;
import de.htwBerlin.ois.FileStructure.RecyclerViewItemSwipeGestures;
import de.htwBerlin.ois.R;
import de.htwBerlin.ois.ServerCommunication.AsyncResponse;
import de.htwBerlin.ois.ServerCommunication.FtpTaskFileDownloading;
import de.htwBerlin.ois.ServerCommunication.FtpTaskFileListing;

import static java.util.Collections.addAll;

/**
 * This Activity represents a small map file download center
 */
public class MapDownloadFragment extends Fragment
{
    /**
     * Fragment ID used to identify the fragment
     * (for example by putting the ID into the Intent extra )
     */
    public static String ID = "MapDownload";
    /**
     * Log tag
     */
    private final String TAG = this.getClass().getSimpleName();
    /**
     * RecyclerView to display the downloadable maps
     */
    private RecyclerView recyclerView;
    /**
     * The RecyclerViews LayoutManager
     */
    private RecyclerView.LayoutManager recyclerLayoutManager;
    /**
     * The RecyclerAdapter
     */
    private OhdmFileRecyclerAdapter recyclerAdapter;
    /**
     * ArrayList of OHDMFiles, to be displayed in the RecyclerView
     */
    private ArrayList<OhdmFile> ohdmFiles;
    private ArrayList<OhdmFile>ohdmFilesBackup;
    private ItemTouchHelper itemTouchHelper;
    private FloatingActionButton requestNewMapFab;
    private SearchView searchView;
    /**
     * The view
     */
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        // inflating the view
        view = inflater.inflate(R.layout.fragment_map_download, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        this.setupSearchView();
        this.listFTPFiles();
        this.setupRecyclerView();
        this.setupFAB();

    }

    private void setupFAB()
    {
        requestNewMapFab = view.findViewById(R.id.request_new_map_fab);
        requestNewMapFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RequestMapFragment()).addToBackStack(null).commit();
            }
        });
    }

    private void setupSearchView()
    {
        searchView = view.findViewById(R.id.map_search_view);
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
                recyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    /**
     * Setup the RecyclerView :
     * <p>
     * Filling it with OHDMFiles
     * Setup Swipe gestures
     * Setup onItemClickListener
     */
    private void setupRecyclerView()
    {

        recyclerView = view.findViewById(R.id.available_maps_recycler);
        recyclerView.setVisibility(View.INVISIBLE);
        ohdmFiles = new ArrayList<>();
        ohdmFilesBackup = new ArrayList<>();
        recyclerLayoutManager = new LinearLayoutManager(this.getContext());
        itemTouchHelper = new ItemTouchHelper(new RecyclerViewItemSwipeGestures(recyclerAdapter, new LeftSwipeCallback()
        {
            @Override
            public void onLeftSwipe(int position)
            {
                FtpTaskFileDownloading ftpTaskFileDownloading = new FtpTaskFileDownloading(getActivity().getApplicationContext());
                ftpTaskFileDownloading.execute(ohdmFiles.get(position));
                recyclerAdapter.notifyDataSetChanged();
            }
        }));


        view.findViewById(R.id.connecting_tv).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.connecting_pb).setVisibility(View.INVISIBLE);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        recyclerAdapter = new OhdmFileRecyclerAdapter(getActivity().getApplicationContext(), ohdmFiles, ohdmFilesBackup, R.layout.download_recycler_item);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
    }

    private void listFTPFiles()
    {
        FtpTaskFileListing ftpTaskFileListing = new FtpTaskFileListing(new AsyncResponse()
        {
            @Override
            public void getOhdmFiles(ArrayList<OhdmFile> files)
            {
                if (files.size() > 0)
                {
                    Log.i(TAG, "received " + files.size() + " files.");
                    ohdmFiles.addAll(files);
                    ohdmFilesBackup.addAll(files);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerAdapter.notifyDataSetChanged();
                }
                else
                {
                    TextView tv = view.findViewById(R.id.connecting_tv);
                    tv.setText("Connection failed, try again later");
                }
            }
        }, getActivity());
        ftpTaskFileListing.execute();

    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

}
