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

import java.util.ArrayList;

import de.htwBerlin.ois.ServerCommunication.AsyncResponse;
import de.htwBerlin.ois.ServerCommunication.FtpEndpointSingleton;
import de.htwBerlin.ois.ServerCommunication.FtpTaskFileDownloading;
import de.htwBerlin.ois.ServerCommunication.FtpTaskFileListing;
import de.htwBerlin.ois.FileStructure.LeftSwipeCallback;
import de.htwBerlin.ois.FileStructure.OhdmFile;
import de.htwBerlin.ois.FileStructure.OhdmFileRecyclerAdapter;
import de.htwBerlin.ois.FileStructure.RecyclerViewItemSwipeGestures;
import de.htwBerlin.ois.R;

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
     * FTP Server IP address
     */
    private static final String FTP_SERVER_IP = "10.0.2.2";
    /**
     * FTP Server port
     */
    private static final Integer FTP_PORT = 5000;
    /**
     * FTP Server username
     */
    private static final String FTP_USER = "ohdmOffViewer";
    /**
     * FTP Server password
     */
    private static final String FTP_PASSWORD = "H!3r0glyph Sat3llite Era$er";
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
    private ItemTouchHelper itemTouchHelper;
    private FtpEndpointSingleton ftpEndpointSingleton;
    private FloatingActionButton requestNewMapFab;
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
        this.initializeFTPSingleton();
        this.setupRecyclerView();
        this.listFTPFiles();
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
        ohdmFiles = new ArrayList<>();
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
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        recyclerAdapter = new OhdmFileRecyclerAdapter(getActivity().getApplicationContext(), ohdmFiles, R.layout.download_recycler_item);
        recyclerView.setAdapter(recyclerAdapter);

        FtpTaskFileListing ftpTaskFileListing = new FtpTaskFileListing(new AsyncResponse()
        {
            @Override
            public void getOhdmFiles(ArrayList<OhdmFile> files)
            {
                ohdmFiles.addAll(files);
                Log.i(TAG, "received " + files.size() + " files.");
                recyclerAdapter.notifyDataSetChanged();
            }
        }, getActivity());
        ftpTaskFileListing.execute();


        //FloatingActionButton

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

    /**
     * Initializes FTP Endpoint singleton
     */
    private void initializeFTPSingleton()
    {
        ftpEndpointSingleton = FtpEndpointSingleton.getInstance();
        ftpEndpointSingleton.setFtpPassword(FTP_PASSWORD);
        ftpEndpointSingleton.setFtpUser(FTP_USER);
        ftpEndpointSingleton.setServerIp(FTP_SERVER_IP);
        ftpEndpointSingleton.setServerPort(FTP_PORT);
    }

    /**
     * executes list files async task
     */
    private void listFTPFiles()
    {
        //ftpTaskFileListing.execute();
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
