package de.htwBerlin.ois.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.htwBerlin.ois.FileStructure.LeftSwipeCallback;
import de.htwBerlin.ois.FileStructure.OhdmFile;
import de.htwBerlin.ois.FileStructure.RecyclerAdapterOhdmMaps;
import de.htwBerlin.ois.FileStructure.RecyclerViewItemSwipeGestures;
import de.htwBerlin.ois.R;
import de.htwBerlin.ois.ServerCommunication.AsyncResponse;
import de.htwBerlin.ois.ServerCommunication.FtpTaskFileDownloading;
import de.htwBerlin.ois.ServerCommunication.FtpTaskFileListing;

/**
 * This Activity represents a small map file download center
 * @author WilliBoelke
 */
public class MapDownloadFragment extends Fragment
{

    //------------Instance Variables------------

    /**
     * Log tag
     */
    private final String TAG = this.getClass().getSimpleName();
    /**
     * The RecyclerViews LayoutManager
     */
    private RecyclerView.LayoutManager recyclerLayoutManager;
    /**
     * The RecyclerAdapter
     */
    private RecyclerAdapterOhdmMaps recyclerAdapter;
    /**
     * ArrayList of OHDMFiles, to be displayed in the RecyclerView
     * This list will be altered when the user uses the search function
     */
    private ArrayList<OhdmFile> ohdmFiles;
    /**
     * ArrayList of OHDMFiles,
     * This list will serve as backup when the
     * ohdmFiles list was altered
     */
    private ArrayList<OhdmFile>ohdmFilesBackup;
    /**
     * The recyclerView
     */
    private RecyclerView recyclerView;
    /**
     * The view
     */
    private View view;


    //------------Static Variables------------

    /**
     * Fragment ID used to identify the fragment
     * (for example by putting the ID into the Intent extra )
     */
    public static String ID = "MapDownload";


    //------------Activity/Fragment Lifecycle------------

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
        ohdmFiles = new ArrayList<>();
        ohdmFilesBackup = new ArrayList<>();
        setHasOptionsMenu(true);
        this.listFTPFiles();
        this.setupRecyclerView();
        this.setupFAB();

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


    //------------Setup Views------------

    /**
     * Setup the FloatingActionButton to replace this fragment with the
     * {@link RequestMapFragment}
     */
    private void setupFAB()
    {
        FloatingActionButton requestNewMapFab = view.findViewById(R.id.request_new_map_fab);
        requestNewMapFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RequestMapFragment()).addToBackStack(null).commit();
            }
        });
    }

    /**
     * Setup the search view to use the nameFilter
     * implemented in {@link RecyclerAdapterOhdmMaps}
     */
    private void setupSearchView(SearchView searchView)
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
                recyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    /**
     * Setup the RecyclerView :
     *
     * Setup Swipe gestures
     * Setup onItemClickListener
     */
    private void setupRecyclerView()
    {
        recyclerView = view.findViewById(R.id.available_maps_recycler);

        recyclerView.setVisibility(View.INVISIBLE);  // is invisible till the server responds

        recyclerLayoutManager = new LinearLayoutManager(this.getContext());//layout manager vor vertical scrolling recycler

        //The itemTouchhelper for the swipe gestures on the recycler Items
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewItemSwipeGestures(recyclerAdapter, new LeftSwipeCallback()
        {
            @Override
            public void onLeftSwipe(int position)
            {
                FtpTaskFileDownloading ftpTaskFileDownloading = new FtpTaskFileDownloading(getActivity().getApplicationContext());
                ftpTaskFileDownloading.execute(ohdmFiles.get(position));
                recyclerAdapter.notifyDataSetChanged();
            }
        }));

        //The recycler adapter
        recyclerAdapter = new RecyclerAdapterOhdmMaps(getActivity().getApplicationContext(), ohdmFiles, ohdmFilesBackup, R.layout.download_recycler_item);

        //Putting everything together
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    /**
     * Initializes an FTP Request to list all files -> {@link FtpTaskFileListing}
     * <p>
     * Implements the {@link AsyncResponse} interface to add the retrieved
     * files to both the ohdmFiles and the ohdmFilesBackup list
     */
    private void listFTPFiles()
    {
        FtpTaskFileListing ftpTaskFileListing = new FtpTaskFileListing(getActivity(), "", new AsyncResponse()
        {
            @Override
            public void getOhdmFiles(ArrayList<OhdmFile> files)
            {
                if (files.size() > 0)
                {
                    Log.i(TAG, "received " + files.size() + " files.");

                    ohdmFiles.addAll(files);
                    ohdmFilesBackup.addAll(files);

                    view.findViewById(R.id.connecting_tv).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.connecting_pb).setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);

                    recyclerAdapter.notifyDataSetChanged();
                }
                else // Server directory was empty or server hasn't responded
                {
                    view.findViewById(R.id.connecting_pb).setVisibility(View.INVISIBLE);
                    TextView tv = view.findViewById(R.id.connecting_tv);
                    tv.setText("Connection failed, try again later");
                }
            }
        });
        ftpTaskFileListing.execute();
    }


    //------------Toolbar Menu------------

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.actionbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.ab_menu_search).setVisible(true);
        setupSearchView((SearchView) searchItem.getActionView());
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
