package de.htwBerlin.ois.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import de.htwBerlin.ois.MainActivityPackage.Database;
import de.htwBerlin.ois.MainActivityPackage.MainActivity;
import de.htwBerlin.ois.R;

public class OptionsFragment extends Fragment
{
    private Switch darkModeToggle;
    private View view;
    private SharedPreferences prefs ;
    public static final String DARK_MODE = "darkmode_settings";
    public static final String SETTINGS_SHARED_PREFERENCES = "OHDMViewerSettings";
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

    /**
     * Getting the Darkmode switch from the view and
     * setting a OnSwitchCheckedListener on it
     */
    private void setUpDarkModeToggle()
    {
        darkModeToggle = view.findViewById(R.id.dark_mode_switch);

        //Set the switch to "checked" in chase the darkmode is enabled
        if (prefs.getBoolean(DARK_MODE, false) == true)
        {
            darkModeToggle.setChecked(true);
        }

        //Setup the onCheckedChangeListener

        darkModeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    prefs.edit().putBoolean( DARK_MODE, true).commit();
                    reset();
                }
                else
                {
                    prefs.edit().putBoolean( DARK_MODE, false).commit();
                    reset();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * reloads the MainActivity to enable/disable the darkmode
     */
    private void reset()
    {
        Intent intent = new Intent(Database.mainContext, MainActivity.class);
        startActivity(intent);
    }
}
