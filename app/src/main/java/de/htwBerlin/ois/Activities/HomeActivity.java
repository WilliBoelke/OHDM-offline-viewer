package de.htwBerlin.ois.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.htwBerlin.ois.FileStructure.MapFileSingleton;
import de.htwBerlin.ois.R;

/**
 * Represents the HOME Tab and thus, the starting point for the OHDM Offline Viewer application
 *
 * @author morelly_t1
 */
public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private static final String MAP_FILE_PATH = Environment.getExternalStorageDirectory().toString() + "/OHDM";
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    private Set<File> mapFiles;

    @BindView(R.id.mapFileDropDown)
    Spinner spinnerMapFile;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottom_navigation;
    @BindView(R.id.buttonSave)
    Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        fillDropDownFiles();
        setUpBottomNavigation();
        checkPermissions();
    }

    /**
     * Creates OHDM Folder if not exists
     */
    private void createOhdmDirectory() {
        File dir = new File(MAP_FILE_PATH);
        boolean status = true;
        if (!dir.exists()) {
            status = dir.mkdirs();
            if (status)
                Toast.makeText(getApplicationContext(), "Created OHDM Directory.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "Couldn't create OHDM Directory.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Initializes Dropdown menu with .map-files
     */
    private void fillDropDownFiles() {
        mapFiles = findMapFiles();
        Log.i(TAG, "found " + mapFiles.size() + " .map-files");
        List<String> list = new ArrayList<String>();

        if (mapFiles.size() > 0) {
            for (File file : mapFiles) {
                list.add(file.getName());
                Log.i(TAG, "added " + file.getName() + " to dropdown");
            }
        }

        List<String> listSorted = list.stream().collect(Collectors.<String>toList());
        Collections.sort(listSorted, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, listSorted);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMapFile.setAdapter(dataAdapter);
    }

    /**
     * Scans OHDM Directory for .map Files
     *
     * @return
     */
    protected Set<File> findMapFiles() {
        Set<File> maps = new HashSet<>();
        try {
            for (File osmfile : new File(MAP_FILE_PATH).listFiles()) {
                if (osmfile.getName().endsWith(".map")) {
                    Log.i(TAG, "osmfile: " + osmfile.getName());
                    maps.add(osmfile);
                }
            }
        } catch (NullPointerException e) {
            Log.i(TAG, "No map files located.");
        }
        return maps;
    }

    /**
     * Sets Listener for Buttom Navigation Bar
     */
    private void setUpBottomNavigation() {
        bottom_navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_about:
                        Intent aboutIntent = new Intent(HomeActivity.this, AboutActivity.class);
                        startActivity(aboutIntent);
                        break;
                    case R.id.nav_navigation:
                        onClickSaveButton();
                        break;
                    case R.id.nav_download:
                        Intent downloadIntent = new Intent(HomeActivity.this, MapDownloadActivity.class);
                        startActivity(downloadIntent);
                        break;
                }
                return false;
            }
        });
    }

    /**
     * Checks necessary permissions
     * Source https://programtalk.com/vs/osmdroid/osmdroid-forge-app/src/main/java/org/osmdroid/forge/app/MainActivity.java/
     */
    private void checkPermissions() {
        List<String> permissions = new ArrayList<>();
        String message = "OHDM Offline Viewer permissions:";
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            message += "\nStorage access to store map Files.";
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            message += "\nLocation to show user location.";
        }
        if (!permissions.isEmpty()) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            String[] params = permissions.toArray(new String[permissions.size()]);
            requestPermissions(params, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        }
    }

    @Override
    protected void onStart() {
        fillDropDownFiles();
        super.onStart();
    }

    @Override
    protected void onResume() {
        fillDropDownFiles();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();

                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                Boolean location = perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                Boolean storage = perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

                if (location && storage) {
                    Toast.makeText(HomeActivity.this, "All permissions granted", Toast.LENGTH_SHORT).show();
                } else if (location) {
                    Toast.makeText(this, "Storage permission is required to store map files to reduce data usage and for offline usage.", Toast.LENGTH_LONG).show();
                } else if (storage) {
                    Toast.makeText(this, "Location permission is required to show the user's location on map.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(HomeActivity.this, "Storage permission is required to store map tiles to reduce data usage and for offline usage." +
                            "\nLocation permission is required to show the user's location on map.", Toast.LENGTH_SHORT).show();
                }
                createOhdmDirectory();
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @OnClick(R.id.buttonSave)
    public void onClickSaveButton() {
        if (mapFiles.size() == 0) {
            final AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
            alertDialog.setTitle("No Map Files found!");
            alertDialog.setMessage("Either store map files in OHDM directory in internal Storage." +
                    " Or Download available maps (see Tab \"Maps\").");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        } else {
            for (File file : mapFiles) {
                if (file.getName().equals(spinnerMapFile.getSelectedItem().toString())) {
                    Log.i(TAG, "User has choosen " + spinnerMapFile.getSelectedItem().toString());
                    Log.i(TAG, "using " + file.getName() + " as mapfile");
                    MapFileSingleton mapFile = MapFileSingleton.getInstance();
                    mapFile.setFile(file);
                }
            }
            Intent navigationIntent = new Intent(HomeActivity.this, NavigationActivity.class);
            startActivity(navigationIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }
}
