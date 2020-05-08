package de.htwBerlin.ois.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.htwBerlin.ois.MainActivityPackage.MainActivity;
import de.htwBerlin.ois.R;

/**
 * Fragment to show the applications settings
 * @author willi
 */
public class OptionsFragment extends Fragment
{
    /**
     * Key to get the DarkMode boolean from the SharedPreferences
     */
    public static final String DARK_MODE = "darkmode_settings";
    /**
     * SharedPreferences name
     */
    public static final String SETTINGS_SHARED_PREFERENCES = "OHDMViewerSettings";
    /**
     * Log tag
     */
    private final String TAG = getClass().getSimpleName();
    /**
     * the view
     */
    private View view;
    /**
     * SharedPreferences to quickly store and access user settings
     */
    private SharedPreferences prefs ;
    private static final int ACCESS_LOCATION_PERMISSION = 34;
    private static final int WRITE_STORAGE_PERMISSION = 56;
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    //The DarkMode toggle
    private Switch darkModeToggle;
    private Switch allowLocationToggle;
    private Switch allowWriteLocalStorageToggle;

    /**
     * Empty Constructor
     */
    public OptionsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_options, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        prefs = getActivity().getApplicationContext().getSharedPreferences(SETTINGS_SHARED_PREFERENCES, 0);

        this.setUpDarkModeToggle();
        this.setupAllowAccessLocationToggle();
        this.setupAllowWriteLocalStorageToggle();
    }

    /**
     * Setup the allowWriteLocalStorage Switch
     */
    private void setupAllowWriteLocalStorageToggle()
    {
        allowWriteLocalStorageToggle = view.findViewById(R.id.allow_write_storage_switch);
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),  Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            allowWriteLocalStorageToggle.setChecked(true);
        }

        allowWriteLocalStorageToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    List<String> permissions = new ArrayList<>();
                    permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    String[] params = permissions.toArray(new String[permissions.size()]);
                    requestPermissions(params, WRITE_STORAGE_PERMISSION);
                }
                else
                {
                    //TODO withdraw permission
                }
            }
        });
    }

    /**
     * Setup the allowAccessLocation Switch
     */
    private void setupAllowAccessLocationToggle()
    {
        allowLocationToggle = view.findViewById(R.id.allow_access_location_switch);
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            allowLocationToggle.setChecked(true);
        }

        allowLocationToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    List<String> permissions = new ArrayList<>();
                    permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
                    String[] params = permissions.toArray(new String[permissions.size()]);
                    requestPermissions(params, ACCESS_LOCATION_PERMISSION);
                }
                else
                {
                    //TODO withdraw permission
                }
            }
        });
    }

    /**
     * Getting the Darkmode switch from the view and
     * setting a OnSwitchCheckedListener on it
     */
    private void setUpDarkModeToggle()
    {
        Log.v(TAG, "Setup DarkMode toggle");

        darkModeToggle = view.findViewById(R.id.dark_mode_switch);

        //Set the switch to "checked" in chase the darkmode is enabled
        if (prefs.getBoolean(DARK_MODE, false) == true)
        {
            Log.v(TAG, "Setup DarkMode toggle: DarkMode ON, toggle checked");
            darkModeToggle.setChecked(true);
        }

        //Setup the onCheckedChangeListener
        Log.v(TAG, "Setup DarkMode toggle: set onCheckedChangeListener");
        darkModeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    prefs.edit().putBoolean( DARK_MODE, true).commit();
                    Log.v(TAG, "Setup DarkMode toggle: Mode changed to dark ");
                    reset();
                }
                else
                {
                    prefs.edit().putBoolean( DARK_MODE, false).commit();
                    Log.v(TAG, "Setup DarkMode toggle: Mode changed to light ");
                    reset();
                }
            }
        });
    }

    /**
     * reloads the MainActivity  in order to enable/disable the darkmode
     */
    private void reset()
    {
        Log.v(TAG, "Reset Activity ");
        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        intent.putExtra("Fragment", 1);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case ACCESS_LOCATION_PERMISSION:
            {
                Toast.makeText(getActivity().getApplicationContext(), "Location access granted", Toast.LENGTH_SHORT).show();
            }
            break;
            case WRITE_STORAGE_PERMISSION:
            {
                Toast.makeText(getActivity().getApplicationContext(), "Allowed to access local storage", Toast.LENGTH_SHORT).show();
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}
