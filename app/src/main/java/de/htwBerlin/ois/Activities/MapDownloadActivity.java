package de.htwBerlin.ois.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.htwBerlin.ois.FTP.AsyncResponse;
import de.htwBerlin.ois.FTP.FtpEndpointSingleton;
import de.htwBerlin.ois.FTP.FtpTaskFileListing;
import de.htwBerlin.ois.FileStructure.OhdmFile;
import de.htwBerlin.ois.FileStructure.OhdmFileAdapter;
import de.htwBerlin.ois.R;

/**
 * This Activity represents a small map file download center
 */
public class MapDownloadActivity extends AppCompatActivity {

    private static final String TAG = "MapDownloadActivity";

    private static final String FTP_SERVER_IP = "";
    private static final Integer FTP_PORT = 21;
    private static final String FTP_USER = "";
    private static final String FTP_PASSWORD = "";

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottom_navigation;

    @BindView(R.id.listView)
    ListView listView;


    private ArrayList<OhdmFile> ohdmFiles;
    private FtpEndpointSingleton ftpEndpointSingleton;
    private FtpTaskFileListing ftpTaskFileListing = new FtpTaskFileListing(new AsyncResponse() {
        @Override
        public void getOhdmFiles(ArrayList<OhdmFile> files) {
            ohdmFiles.addAll(files);
            Log.i(TAG, "received " + files.size() + " files.");
            OhdmFileAdapter adapter = new OhdmFileAdapter(MapDownloadActivity.this, R.layout.adapter_view_layout, ohdmFiles);
            listView.setAdapter(adapter);
        }
    }, this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_download);
        ButterKnife.bind(this);

        ohdmFiles = new ArrayList<>();

        setUpBottomNavigation();
        initializeFTPSingleton();
        listFTPFiles();

    }

    /**
     * Sets Listener for Buttom Navigation Bar
     */
    private void setUpBottomNavigation() {

        Menu menu = bottom_navigation.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottom_navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_about:
                        Intent aboutIntent = new Intent(MapDownloadActivity.this, AboutActivity.class);
                        startActivity(aboutIntent);
                        break;
                    case R.id.nav_navigation:
                        Intent navigationIntent = new Intent(MapDownloadActivity.this, NavigationActivity.class);
                        startActivity(navigationIntent);
                        break;
                    case R.id.nav_home:
                        Intent startIntent = new Intent(MapDownloadActivity.this, HomeActivity.class);
                        startActivity(startIntent);
                        break;
                }
                return false;
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
        ftpTaskFileListing.execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
