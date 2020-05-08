package de.htwBerlin.ois.MainActivityPackage;

import android.Manifest;
import android.app.ActionBar;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.htwBerlin.ois.FileStructure.MapFileSingleton;
import de.htwBerlin.ois.Fragments.AboutFragment;
import de.htwBerlin.ois.Fragments.FAQFragment;
import de.htwBerlin.ois.Fragments.HomeFragment;
import de.htwBerlin.ois.Fragments.MapDownloadFragment;
import de.htwBerlin.ois.Fragments.NavigationFragment;
import de.htwBerlin.ois.Fragments.OptionsFragment;
import de.htwBerlin.ois.R;

public class MainActivity extends AppCompatActivity
{

    private BottomNavigationView bottomNav;
    private Fragment defaultFragment = new HomeFragment();

    public static final String MAP_FILE_PATH = Environment.getExternalStorageDirectory().toString() + "/OHDM";
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Get settings from SharedPrefs
        if (getApplicationContext().getSharedPreferences(OptionsFragment.SETTINGS_SHARED_PREFERENCES, 0).getBoolean(OptionsFragment.DARK_MODE, false) == true)
        {
            setTheme(R.style.DarkTheme);
        }
        else
        {
            setTheme(R.style.LightTheme);
        }

        setContentView(R.layout.activity_main);

        checkPermissions();
        createOhdmDirectory();


        // setting up the BottomNavigationView with Listener
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_view);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // giving first defaultFragment to the FragmentManager
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, defaultFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.ab_menu_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
                break;

            case R.id.ab_menu_faq:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FAQFragment()).commit();
                break;
            case R.id.ab_menu_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OptionsFragment()).commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Bottom Nav Listener
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            Fragment selectedFragment = null;
            // switch ... case to select the right Fragment to start
            switch (item.getItemId())
            {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_download:
                    selectedFragment = new MapDownloadFragment();
                    break;
                case R.id.nav_navigation:
                    if (MapFileSingleton.getInstance().getFile() != null)
                    {
                        selectedFragment = new NavigationFragment();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "You need to choose a map", Toast.LENGTH_LONG).show();
                    }
                    break;

                default:
                    return false;
            }

            // giving the FragmentManager the container and the fragment which should be loaded into view
            // ... also commit
            try
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
            catch (NullPointerException e)
            {
                Log.e("MainActivity_initFragment", "onNavigationItemSelected: Fragment was null ", e);
            }
            // return true to tell that everything did go right
            return true;
        }
    };

    /**
     * Creates OHDM Folder if not exists
     */
    private void createOhdmDirectory()
    {
        File dir = new File(MAP_FILE_PATH);
        boolean status = true;
        if (!dir.exists())
        {
            status = dir.mkdirs();
            if (status) Toast.makeText(this, "Created OHDM Directory.", Toast.LENGTH_SHORT).show();
            else Toast.makeText(this, "Couldn't create OHDM Directory.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Checks necessary permissions
     * Source https://programtalk.com/vs/osmdroid/osmdroid-forge-app/src/main/java/org/osmdroid/forge/app/MainActivity.java/
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissions()
    {
        List<String> permissions = new ArrayList<>();
        String message = "OHDM Offline Viewer permissions:";
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            message += "\nStorage access to store map Files.";
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            message += "\nLocation to show user location.";
        }
        if (!permissions.isEmpty())
        {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            String[] params = permissions.toArray(new String[permissions.size()]);
            requestPermissions(params, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<>();

                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                Boolean location = perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                Boolean storage = perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

                if (location && storage)
                {
                    Toast.makeText(this, "All permissions granted", Toast.LENGTH_SHORT).show();
                }
                else if (location)
                {
                    Toast.makeText(this, "Storage permission is required to store map files to reduce data usage and for offline usage.", Toast.LENGTH_LONG).show();
                }
                else if (storage)
                {
                    Toast.makeText(this, "Location permission is required to show the user's location on map.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(this, "Storage permission is required to store map tiles to reduce data usage and for offline usage." + "\nLocation permission is required to show the user's location on map.", Toast.LENGTH_SHORT).show();
                }
                createOhdmDirectory();
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
