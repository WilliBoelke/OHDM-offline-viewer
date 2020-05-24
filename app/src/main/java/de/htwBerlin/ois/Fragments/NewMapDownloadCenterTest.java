package de.htwBerlin.ois.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;

import de.htwBerlin.ois.FileStructure.RemoteDirectory;
import de.htwBerlin.ois.FileStructure.RemoteFile;
import de.htwBerlin.ois.FileStructure.RecyclerViewAdapterDirectories;
import de.htwBerlin.ois.R;
import de.htwBerlin.ois.ServerCommunication.AsyncResponse;
import de.htwBerlin.ois.ServerCommunication.FtpTaskDirListing;


public class NewMapDownloadCenterTest extends Fragment
{
    private View view;
    private ArrayList<RemoteDirectory> directoryList;
    private RecyclerViewAdapterDirectories recyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        // inflating the view
        view = inflater.inflate(R.layout.fragment_new_map_download_center_test, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        directoryList = new ArrayList<>();
        setHasOptionsMenu(true);
        this.getDirs();
        this.setupDirRecycler();
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
        recyclerViewAdapter = new RecyclerViewAdapterDirectories(getActivity().getApplicationContext(), directoryList, R.layout.fragment_map_doanload_horizontal_recycler);


        //Putting everything together
        dirRecycler.setLayoutManager(recyclerLayoutManager);
        dirRecycler.setAdapter(recyclerViewAdapter);
    }

    private void getDirs()
    {
        FtpTaskDirListing dirListing = new FtpTaskDirListing(getActivity(), "" , asyncResponseDirListing);
        dirListing.execute();
    }


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
            directoryList.addAll(dirs);
            recyclerViewAdapter.notifyDataSetChanged();
        }
    };
}
