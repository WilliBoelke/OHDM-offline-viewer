package de.htwBerlin.ois.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import de.htwBerlin.ois.FTP.FtpEndpointSingleton;
import de.htwBerlin.ois.FileStructure.OhdmFile;
import de.htwBerlin.ois.R;

/**
 * This Activity represents a small map file download center
 */
public class MapDownloadFragment extends Fragment {

    private static final String TAG = "MapDownloadActivity";

    private static final String FTP_SERVER_IP = "";
    private static final Integer FTP_PORT = 21;
    private static final String FTP_USER = "";
    private static final String FTP_PASSWORD = "";

    private ListView listView;

    private ArrayList<OhdmFile> ohdmFiles;
    private FtpEndpointSingleton ftpEndpointSingleton;
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

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map_download, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ohdmFiles = new ArrayList<>();

        listView = view.findViewById(R.id.downladRecycler);

        initializeFTPSingleton();
        listFTPFiles();

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

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
