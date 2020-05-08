package de.htwBerlin.ois.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import de.htwBerlin.ois.FTP.FtpEndpointSingleton;
import de.htwBerlin.ois.FileStructure.OhdmFile;
import de.htwBerlin.ois.FileStructure.OhdmFileRecyclerAdapter;
import de.htwBerlin.ois.FileStructure.OhdmFileSwipeToDownloadCallback;
import de.htwBerlin.ois.R;

/**
 * This Activity represents a small map file download center
 */
public class MapDownloadFragment extends Fragment {

    /**
     * FTP Server IP address
     */
    private static final String FTP_SERVER_IP = "";
    /**
     * FTP Server port
     */
    private static final Integer FTP_PORT = 21;
    /**
     * FTP Server username
     */
    private static final String FTP_USER = "";
    /**
     * FTP Server password
     */
    private static final String FTP_PASSWORD = "";
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.setupRecyclerView();
        this.initializeFTPSingleton();
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

        //ohdmFiles = new ArrayList<>();
        //listView = view.findViewById(R.id.downladRecycler);
        /* private FtpTaskFileListing ftpTaskFileListing = new FtpTaskFileListing(new AsyncResponse() {
        @Override
        public void getOhdmFiles(ArrayList<OhdmFile> files) {
            ohdmFiles.addAll(files);
            Log.i(TAG, "received " + files.size() + " files.");
            OhdmFileAdapter adapter = new OhdmFileAdapter(Database.mainContext.getApplicationContext(), R.layout.adapter_view_layout, ohdmFiles);
            listView.setAdapter(adapter);
        }
    }, this);
    */
        //TODO placeholder to be deleted
        {
            recyclerView = view.findViewById(R.id.available_maps_recycler);
            ohdmFiles = new ArrayList<OhdmFile>();
            Long size = new Long(122);
            OhdmFile one = new OhdmFile("Berlin", size, "12.12.3400", false);
            OhdmFile two = new OhdmFile("Frankfurt", size, "12.12.3400", true);
            OhdmFile three = new OhdmFile("Köln", size, "12.12.3400", true);
            OhdmFile four = new OhdmFile("München", size, "12.12.3400", false);
            ohdmFiles.add(one);
            ohdmFiles.add(two);
            ohdmFiles.add(three);
            ohdmFiles.add(four);
        }
        recyclerLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerAdapter = new OhdmFileRecyclerAdapter(this.getContext(), ohdmFiles, R.layout.download_recycler_item);
        itemTouchHelper = new ItemTouchHelper(new OhdmFileSwipeToDownloadCallback(recyclerAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();


        //FloatingActionButton

        requestNewMapFab = view.findViewById(R.id.request_new_map_fab);
        requestNewMapFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RequestMapFragment()).commit();
            }
        });
    }

    /**
     * Initializes FTP Endpoint singleton
     */
    private void initializeFTPSingleton() {
        ftpEndpointSingleton = FtpEndpointSingleton.getInstance();
        ftpEndpointSingleton.setFtpPassword(FTP_PASSWORD);
        ftpEndpointSingleton.setFtpUser(FTP_USER);
        ftpEndpointSingleton.setServerIp(FTP_SERVER_IP);
        ftpEndpointSingleton.setServerPort(FTP_PORT);
    }

    /**
     * executes list files async task
     */
    private void listFTPFiles() {
        //ftpTaskFileListing.execute();
    }


}
