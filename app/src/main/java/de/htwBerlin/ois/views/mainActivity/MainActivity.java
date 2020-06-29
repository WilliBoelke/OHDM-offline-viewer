package de.htwBerlin.ois.views.mainActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.htwBerlin.ois.R;
import de.htwBerlin.ois.model.repositories.localRepositories.MapFileSingleton;
import de.htwBerlin.ois.model.repositories.localRepositories.UserPreferences;
import de.htwBerlin.ois.serverCommunication.HttpClient;
import de.htwBerlin.ois.serverCommunication.SftpClient;
import de.htwBerlin.ois.viewModels.ViewModelMainActivity;
import de.htwBerlin.ois.views.factory.FragmentFactory;
import de.htwBerlin.ois.views.fragments.FragmentAbout;
import de.htwBerlin.ois.views.fragments.FragmentDownloadCenterAll;
import de.htwBerlin.ois.views.fragments.FragmentFAQ;
import de.htwBerlin.ois.views.fragments.FragmentHome;
import de.htwBerlin.ois.views.fragments.FragmentNavigation;
import de.htwBerlin.ois.views.fragments.FragmentOptions;
import de.htwBerlin.ois.views.fragments.FragmentRequestStatus;

import static de.htwBerlin.ois.model.repositories.localRepositories.UserPreferences.SETTINGS_SHARED_PREFERENCES;

/**
 * @author WilliBoelke
 */
public class MainActivity extends AppCompatActivity
{

    //------------Static Variables------------

    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;


    //------------Instance Variables------------

    private String TAG = getClass().getSimpleName();
    private ViewModelMainActivity viewModel;


    //------------Activity/Fragment Lifecycle------------


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Initializing the view model
        Log.d(TAG, "onActivityCreated : initializing ViewModel");
        viewModel = ViewModelProviders.of(this).get(ViewModelMainActivity.class);

        //Setting the FragmentFactory
        getSupportFragmentManager().setFragmentFactory(new FragmentFactory(new SftpClient(), new HttpClient()));

        //Initializing the USerPreferences with Application context
        UserPreferences.getInstance();
        UserPreferences.getInstance().init(getApplicationContext().getSharedPreferences(SETTINGS_SHARED_PREFERENCES, 0));

        //Setting tthe app heme and contentView
        Log.d(TAG, "onCreate : setting app theme...");
        if (viewModel.isDarkModeEnabled())
        {
            setTheme(R.style.DarkTheme);
            Log.d(TAG, "onCreate :  app theme DARK");
        }
        else
        {
            setTheme(R.style.LightTheme);
            Log.d(TAG, "onCreate :  app theme LIGHT");
        }
        setContentView(R.layout.activity_main);

        //Setup Views

        //Open the correct fragment
        Intent intent = getIntent();
        Class frag = viewModel.getCorrectFragment(intent, savedInstanceState);
        if (frag != null) // Else the kast fragment will come into view again
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag, new Bundle()).addToBackStack(null).commit();
        }

        //creating the OHDM dir
        viewModel.createOhdmDirectory();

        //Getting a User ID from the server
        viewModel.httpGetIdFromServer();

        //Checking permissions
        this.checkPermissions();

        this.setupBottomNav();
    }


    //------------Setup Views------------

    private void setupBottomNav()
    {
        // setting up the BottomNavigationView with Listener
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_view);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    //------------Permissions------------

    /**
     * Checks necessary permissions
     * Source https://programtalk.com/vs/osmdroid/osmdroid-forge-app/src/main/java/org/osmdroid/forge/app/MainActivity.java/
     */
    public void checkPermissions()
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(params, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
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
                viewModel.createOhdmDirectory();
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    //------------Toolbar Menu------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.ab_menu_about:
                Log.d(TAG, "Options Menu : replacing current fragment with FragmentAbout");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentAbout.class, new Bundle()).addToBackStack(null).commit();
                break;
            case R.id.ab_menu_faq:
                Log.d(TAG, "Options Menu : replacing current fragment with FragmentFAQ");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentFAQ.class, new Bundle()).addToBackStack(null).commit();
                break;
            case R.id.ab_menu_settings:
                Log.d(TAG, "Options Menu : replacing current fragment with FragmentOptions");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentOptions.class, new Bundle()).addToBackStack(null).commit();
                break;
            case R.id.ab_menu_search:
                //no implemented here, to e implemented in fragments
                return false;
        }
        return super.onOptionsItemSelected(item);
    }


    //------------Bottom Navigation ------------

    /**
     * Bottom Nav Listener
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            Class selectedFragment = null;
            // switch ... case to select the right Fragment to start
            switch (item.getItemId())
            {
                case R.id.nav_home:
                    selectedFragment = FragmentHome.class;
                    break;
                case R.id.nav_download:
                    selectedFragment = FragmentDownloadCenterAll.class;
                    break;
                case R.id.nav_request_status:
                    selectedFragment = FragmentRequestStatus.class;
                    break;
                case R.id.nav_navigation:
                    if (MapFileSingleton.getInstance().getFile() != null)
                    {
                        selectedFragment = FragmentNavigation.class;
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment, new Bundle()).addToBackStack(null).commit();
            }
            catch (NullPointerException e)
            {
                Log.e(TAG, "onNavigationItemSelected: Fragment was null ", e);
            }
            // return true to tell that everything did go right
            return true;
        }
    };

}
