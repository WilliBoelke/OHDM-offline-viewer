package de.htwBerlin.ois.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import de.htwBerlin.ois.MainActivityPackage.MainActivity;
import de.htwBerlin.ois.R;

/**
 * This fragment represents a Options/ Settings  Page
 * @author willi
 */
public class OptionsFragment extends Fragment
{
    private View view;
    //LogTag String
    private static final String TAG = "NavigationActivity";
    //The Shared preferences
    private SharedPreferences prefs ;
    //String to get the DarkMode boolean from the SharedPreferences
    public static final String DARK_MODE = "darkmode_settings";
    //String to get the app settings SharedPreferences
    public static final String SETTINGS_SHARED_PREFERENCES = "OHDMViewerSettings";
    //The DarkMode toggle
    private Switch darkModeToggle;

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
        prefs = getActivity().getApplicationContext().getSharedPreferences(SETTINGS_SHARED_PREFERENCES, 0);

      this.setUpDarkModeToggle();

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
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
     * reloads the MainActivity to enable/disable the darkmode
     */
    private void reset()
    {
        Log.v(TAG, "Reset Activity ");
        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        intent.putExtra("Fragment", 1);
        startActivity(intent);
    }
}
