package de.htwBerlin.ois.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.htwBerlin.ois.FileStructure.OnRecyclerItemButtonClicklistenner;
import de.htwBerlin.ois.FileStructure.RecyclerAdapterRemoteFiles;
import de.htwBerlin.ois.FileStructure.RemoteDirectory;
import de.htwBerlin.ois.FileStructure.RemoteFile;
import de.htwBerlin.ois.FileStructure.RemoteListsSingleton;
import de.htwBerlin.ois.R;
import de.htwBerlin.ois.ServerCommunication.AsyncResponse;
import de.htwBerlin.ois.ServerCommunication.FtpTaskFileDownloading;
import de.htwBerlin.ois.ServerCommunication.FtpTaskFileListing;

import static de.htwBerlin.ois.ServerCommunication.Variables.MOST_RECENT_PATH;

/**
 * This Activity represents a small map file download center
 * @author WilliBoelke
 */
public class FragmentDownloadCenterAll extends Fragment
{

    //------------Instance Variables------------

    /**
     * Log tag
     */
    private final String TAG = this.getClass().getSimpleName();
    /**
     * ArrayList of OHDMFiles, to be displayed in the RecyclerView
     * This list will be altered when the user uses the search function
     */
    private ArrayList<RemoteFile> allOhdmFiles;
    /**
     * ArrayList of RemoteFiles (.map),
     * This list will serve as backup when the
     * ohdmFiles list was altered
     */
    private ArrayList<RemoteFile> allOhdmFilesBackup;
    private ArrayList<RemoteFile> latestOhdmFiles;
    private ArrayList<RemoteFile> latestOhdmFilesBackup;
    private RecyclerAdapterRemoteFiles allRecyclerAdapter;
    private RecyclerAdapterRemoteFiles latestRecyclerAdapter;
    /**
     * The recyclerView
     */
    private RecyclerView allMapsRecyclerView;
    private RecyclerView latestMapsRecyclerView;
    /**
     * The view
     */
    private View view;
    /**
     * used in {@link this#changeVisibilities}
     * to define which views are visible/invisible
     * while trying to connect with the server
     */
    private final  byte STATE_CONNECTING = 1;
    /**
     * used in {@link this#changeVisibilities}
     * to define which views are visible/invisible
     * when the server hast responded
     */
    private final  byte STATE_NO_CONNECTION = 2;
    /**
     * used in {@link this#changeVisibilities}
     * to define which views are visible/invisible
     * when connected with the server / file listing was successful
     */
    private final  byte STATE_CONNECTED = 3;

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
        view = inflater.inflate(R.layout.fragment_map_download_all, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        allOhdmFiles = new ArrayList<>();
        allOhdmFilesBackup = new ArrayList<>();
        latestOhdmFiles = new ArrayList<>();
        latestOhdmFilesBackup = new ArrayList<>();
        setHasOptionsMenu(true);
        this.setupAllMapsRecyclerView();
        this.setupLatestMapsRecyclerView();
        this.setupLatestSearchView();
        this.setupAllSearchView();
        this.changeVisibilities(STATE_CONNECTING);
        this.setupFAB();
        this.setupSwipeToRefresh();
        this.setupButtonToCategories();
        this.restoreFiles();
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
        storeFiles();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }


    //------------Setup Views------------

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

    /**
     * Setup the AllMaps RecyclerView :
     *
     * Setup Swipe gestures
     * Setup onItemClickListener
     */
    private void setupAllMapsRecyclerView()
    {
        allMapsRecyclerView = view.findViewById(R.id.available_maps_recycler);

        allMapsRecyclerView.setVisibility(View.INVISIBLE);  // is invisible till the server responds

        RecyclerView.LayoutManager recyclerLayoutManager = new LinearLayoutManager(this.getContext());//layout manager vor vertical scrolling recycler

        //The recycler adapter
        allRecyclerAdapter = new RecyclerAdapterRemoteFiles(getActivity().getApplicationContext(), allOhdmFiles, allOhdmFilesBackup, R.layout.recycler_item_vertical);

        //OnClickListener for the button inside the RecyclerView item layout
        allRecyclerAdapter.setOnItemButtonClickListener(new OnRecyclerItemButtonClicklistenner()
        {
            @Override
            public void onButtonClick(int position)
            {
                Toast.makeText(getContext(), "Download started", Toast.LENGTH_SHORT).show();
                FtpTaskFileDownloading ftpTaskFileDownloading = new FtpTaskFileDownloading(getActivity().getApplicationContext(), "");
                ftpTaskFileDownloading.execute(allOhdmFiles.get(position));
                allRecyclerAdapter.notifyDataSetChanged();
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
        latestMapsRecyclerView = view.findViewById(R.id.latest_maps_recycler);

        latestMapsRecyclerView.setVisibility(View.INVISIBLE);  // is invisible till the server responds

        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this.getContext());//layout manager vor vertical scrolling recycler
        recyclerLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        //The recycler adapter
        latestRecyclerAdapter = new RecyclerAdapterRemoteFiles(getActivity().getApplicationContext(), latestOhdmFiles, latestOhdmFilesBackup, R.layout.recycler_item_horizonal);

        latestRecyclerAdapter.setOnItemButtonClickListener(new OnRecyclerItemButtonClicklistenner()
        {
            @Override
            public void onButtonClick(int position)
            {
                Toast.makeText(getContext(), "Download started", Toast.LENGTH_SHORT).show();
                FtpTaskFileDownloading ftpTaskFileDownloading = new FtpTaskFileDownloading(getActivity().getApplicationContext(), MOST_RECENT_PATH);
                ftpTaskFileDownloading.execute(latestOhdmFiles.get(position));
                allRecyclerAdapter.notifyDataSetChanged();
            }
        });

        //Putting everything together
        latestMapsRecyclerView.setLayoutManager(recyclerLayoutManager);
        latestMapsRecyclerView.setAdapter(latestRecyclerAdapter);
    }

    private void setupButtonToCategories()
    {
        Button button = view.findViewById(R.id.button_categories);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentDownloadCenterCategories()).addToBackStack(null).commit();
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
                latestRecyclerAdapter.getFilter().filter(newText);
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
                allRecyclerAdapter.getFilter().filter(newText);;
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
        SearchView searchView = view.findViewById(R.id.latest_sv);
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
                latestRecyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    /**
     * mode = true
     * Method will make the RecyclerView visible
     * and the connection indicators invisible
     * mode = false
     * well...the opposite
     * @param mode
     */
    private void changeVisibilities(byte mode)
    {
        switch( mode)
        {
            case STATE_CONNECTED:
                view.findViewById(R.id.connecting_tv).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.connecting_pb).setVisibility(View.INVISIBLE);

                view.findViewById(R.id.all_sv).setVisibility(View.VISIBLE);
                view.findViewById(R.id.all_tv).setVisibility(View.VISIBLE);
                view.findViewById(R.id.latest_sv).setVisibility(View.VISIBLE);
                view.findViewById(R.id.lates_tv).setVisibility(View.VISIBLE);
                allMapsRecyclerView.setVisibility(View.VISIBLE);
                latestMapsRecyclerView.setVisibility(View.VISIBLE);
            break;
            case STATE_CONNECTING:
                view.findViewById(R.id.connecting_tv).setVisibility(View.VISIBLE);
                view.findViewById(R.id.connecting_pb).setVisibility(View.VISIBLE);

                view.findViewById(R.id.all_sv).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.all_tv).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.latest_sv).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.lates_tv).setVisibility(View.INVISIBLE);
                allMapsRecyclerView.setVisibility(View.INVISIBLE);
                latestMapsRecyclerView.setVisibility(View.INVISIBLE);
                break;
            case STATE_NO_CONNECTION:
                view.findViewById(R.id.connecting_tv).setVisibility(View.VISIBLE);
                ((TextView) view.findViewById(R.id.connecting_tv)).setText("Coudnt make connection");

                view.findViewById(R.id.connecting_pb).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.all_tv).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.all_sv).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.latest_sv).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.lates_tv).setVisibility(View.INVISIBLE);
                allMapsRecyclerView.setVisibility(View.INVISIBLE);
                latestMapsRecyclerView.setVisibility(View.INVISIBLE);
                break;
        }
    }

    //------------FTP Listing------------


    /**
     * Initializes an FTP Request to list all files -> {@link FtpTaskFileListing}
     * <p>
     * Implements the {@link AsyncResponse} interface to add the retrieved
     * files to both the ohdmFiles and the ohdmFilesBackup list
     */
    private void FTPListAllFiles()
    {
        FtpTaskFileListing ftpTaskFileListing = new FtpTaskFileListing(getActivity(), "", new AsyncResponse()
        {
            @Override
            public void getOhdmFiles(ArrayList<RemoteFile> remoteFiles)
            {
                if (remoteFiles.size() > 0)
                {
                    Log.i(TAG, "received " + remoteFiles.size() + " files.");

                    allOhdmFiles.clear();
                    allOhdmFilesBackup.clear();
                    allOhdmFiles.addAll(remoteFiles);
                    allOhdmFilesBackup.addAll(remoteFiles);
                    allRecyclerAdapter.notifyDataSetChanged();
                    latestRecyclerAdapter.notifyDataSetChanged();
                    changeVisibilities(STATE_CONNECTED);
                }
                else // Server directory was empty or server hasn't responded
                {
                    view.findViewById(R.id.connecting_pb).setVisibility(View.INVISIBLE);
                    TextView tv = view.findViewById(R.id.connecting_tv);
                    tv.setText("Connection failed, try again later");
                }
            }

            @Override
            public void getRemoteDirectories(ArrayList<RemoteDirectory> dirs)
            {

            }
        });
        ftpTaskFileListing.execute();
    }

    /**
     * Initializes an FTP Request to list all files -> {@link FtpTaskFileListing}
     * <p>
     * Implements the {@link AsyncResponse} interface to add the retrieved
     * files to both the ohdmFiles and the ohdmFilesBackup list
     */
    private void FTPListLatestFiles()
    {
        FtpTaskFileListing ftpTaskFileListing = new FtpTaskFileListing(getActivity(), MOST_RECENT_PATH, new AsyncResponse()
        {
            @Override
            public void getOhdmFiles(ArrayList<RemoteFile> remoteFiles)
            {
                if (remoteFiles.size() > 0)
                {
                    Log.i(TAG, "received " + remoteFiles.size() + " files.");

                    latestOhdmFilesBackup.clear();
                    latestOhdmFiles.clear();
                    latestOhdmFiles.addAll(remoteFiles);
                    latestOhdmFilesBackup.addAll(remoteFiles);
                    latestRecyclerAdapter.notifyDataSetChanged();
                    changeVisibilities(STATE_CONNECTED);
                }
                else // Server directory was empty or server hasn't responded
                {
                    changeVisibilities(STATE_NO_CONNECTION);
                }
            }

            @Override
            public void getRemoteDirectories(ArrayList<RemoteDirectory> dirs)
            {

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
                FTPListAllFiles();
                FTPListLatestFiles();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    //------------Save/Restore Instance State------------

    private void storeFiles()
    {
        RemoteListsSingleton.getInstance().setAllMaps(this.allOhdmFilesBackup);
        RemoteListsSingleton.getInstance().setLatestMaps(this.latestOhdmFilesBackup);
    }

    private void restoreFiles()
    {
        if (RemoteListsSingleton.getInstance().getAllMaps().size() != 0)
        {
            allOhdmFiles.clear();
            allOhdmFilesBackup.clear();
            this.allOhdmFiles.addAll(RemoteListsSingleton.getInstance().getAllMaps());
            this.allOhdmFilesBackup.addAll(allOhdmFiles);
            changeVisibilities(STATE_CONNECTED);
            allRecyclerAdapter.notifyDataSetChanged();
        }
        else
        {
            FTPListAllFiles();
        }
        if (RemoteListsSingleton.getInstance().getLatestMaps().size() != 0)
        {
            latestOhdmFilesBackup.clear();
            latestOhdmFiles.clear();
            this.latestOhdmFiles.addAll(RemoteListsSingleton.getInstance().getLatestMaps());
            this.latestOhdmFilesBackup.addAll(latestOhdmFiles);
            changeVisibilities(STATE_CONNECTED);
            latestRecyclerAdapter.notifyDataSetChanged();
        }
        else
        {
            FTPListLatestFiles();
        }
    }
}