package de.htwBerlin.ois.views.mainActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.htwBerlin.ois.R;
import de.htwBerlin.ois.models.repositories.localRepositories.UserPreferences;
import de.htwBerlin.ois.views.factory.FragmentFactory;
import de.htwBerlin.ois.models.repositories.localRepositories.MapFileSingleton;
import de.htwBerlin.ois.models.fileStructure.RemoteDirectory;
import de.htwBerlin.ois.models.fileStructure.RemoteFile;
import de.htwBerlin.ois.serverCommunication.AsyncResponse;
import de.htwBerlin.ois.serverCommunication.HttpClient;
import de.htwBerlin.ois.serverCommunication.HttpRequest;
import de.htwBerlin.ois.serverCommunication.SftpClient;
import de.htwBerlin.ois.views.fragments.FragmentAbout;
import de.htwBerlin.ois.views.fragments.FragmentDownloadCenterAll;
import de.htwBerlin.ois.views.fragments.FragmentFAQ;
import de.htwBerlin.ois.views.fragments.FragmentHome;
import de.htwBerlin.ois.views.fragments.FragmentNavigation;
import de.htwBerlin.ois.views.fragments.FragmentOptions;
import de.htwBerlin.ois.views.fragments.FragmentRequestStatus;

import static de.htwBerlin.ois.models.repositories.localRepositories.UserPreferences.SETTINGS_SHARED_PREFERENCES;
import static de.htwBerlin.ois.serverCommunication.HttpRequest.REQUEST_TYPE_ID;
import static de.htwBerlin.ois.models.repositories.localRepositories.Variables.MAP_FILE_PATH;

/**
 * @author WilliBoelke
 */
public class MainActivity extends AppCompatActivity
{

    //------------Static Variables------------

    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;


    //------------Instance Variables------------

    private String TAG = getClass().getSimpleName();


    //------------Activity/Fragment Lifecycle------------
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment, new Bundle()).addToBackStack("").commit();
            }
            catch (NullPointerException e)
            {
                Log.e(TAG, "onNavigationItemSelected: Fragment was null ", e);
            }
            // return true to tell that everything did go right
            return true;
        }
    };


    //------------Bottom Navigation ------------

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreate : setting app theme...");
        getSupportFragmentManager().setFragmentFactory(new FragmentFactory(new SftpClient(), new HttpClient()));
        UserPreferences.getInstance();
        UserPreferences.getInstance().init(getApplicationContext());
        super.onCreate(savedInstanceState);
        //Get settings from SharedPrefs
        if (UserPreferences.getInstance().isDarkModeEnabled())
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


        // setting up the BottomNavigationView with Listener
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_view);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //Open the correct fragment
        Intent intent = getIntent();
        if (intent.getStringExtra("Fragment") != null)
        {
            if (intent.getStringExtra("Fragment").equals(FragmentOptions.ID))
            {
                //if we came her from the reset method in the options fragment, we want the options fragment to appear again
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentOptions.class, new Bundle()).commit();
                intent.putExtra("Fragment", "");
            }
        }
        else
        {
            if (savedInstanceState == null)
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentHome.class, new Bundle()).addToBackStack(null).commit();
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkPermissions();
        }
        createOhdmDirectory();
        HttpGetIdFromServer();
    }


    //------------Permissions------------

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


    //------------Toolbar Menu------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.ab_menu_about:
                Log.d(TAG, "Options Menu : replacing current fragment with FragmentAbout");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentAbout.class, new Bundle()).addToBackStack(FragmentAbout.ID).commit();
                break;

            case R.id.ab_menu_faq:
                Log.d(TAG, "Options Menu : replacing current fragment with FragmentFAQ");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentFAQ.class, new Bundle()).addToBackStack(FragmentFAQ.ID).commit();
                break;
            case R.id.ab_menu_settings:
                Log.d(TAG, "Options Menu : replacing current fragment with FragmentOptions");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentOptions.class, new Bundle()).commit();
                break;
            case R.id.ab_menu_search:
                //no implemented here, to e implemented in fragments
                return false;
        }
        return super.onOptionsItemSelected(item);
    }


    //------------On First App Start------------

    /**
     * Creates OHDM Folder if not exists
     */
    private void createOhdmDirectory()
    {
        Log.d(TAG, "createOhdmDirectory : Creating OHDM directory...");
        File dir = new File(MAP_FILE_PATH);
        boolean status;
        if (!dir.exists())
        {
            status = dir.mkdirs();
            if (status) Toast.makeText(this, "Created OHDM Directory.", Toast.LENGTH_SHORT).show();
            else Toast.makeText(this, "Couldn't create OHDM Directory.", Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, "createOhdmDirectory : OHDM directory created");
    }

    /**
     * At the first start oft the app, i want to ge an ID
     * from the server, to identify the user and his requests,
     * but completely anonymous
     */
    private void HttpGetIdFromServer()
    {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(SETTINGS_SHARED_PREFERENCES, 0);
        if (UserPreferences.getInstance().getUserID() == null)
        {
            HttpRequest httpRequest = new HttpRequest();
            httpRequest.setRequestType(REQUEST_TYPE_ID);
            httpRequest.setHttpClient(new HttpClient());
            httpRequest.setAsyncResponse(new AsyncResponse()
            {
                @Override
                public void getRemoteFiles(ArrayList<RemoteFile> remoteFiles)
                {
                    //not needed here
                }

                @Override
                public void getRemoteDirectories(ArrayList<RemoteDirectory> remoteDirectories)
                {
                    //not needed here
                }

                @Override
                public void getHttpResponse(String response)
                {

                    if (response == null)
                    {
                        Toast.makeText(getApplicationContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Response = " + response, Toast.LENGTH_SHORT).show();
                        UserPreferences.getInstance().setUserId(response);
                        Log.d(TAG, "ID from server = " + response);
                    }

                }
            });

            httpRequest.execute();
        }
    }
}
