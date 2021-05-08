package de.htwBerlin.ois.views.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import de.htwBerlin.ois.R;
import de.htwBerlin.ois.viewModels.ViewModelOptions;
import de.htwBerlin.ois.views.mainActivity.MainActivity;

import static de.htwBerlin.ois.model.repositories.localRepositories.Variables.REQUEST_CODE_ACCESS_LOCATION_PERMISSION;
import static de.htwBerlin.ois.model.repositories.localRepositories.Variables.REQUEST_CODE_WRITE_STORAGE_PERMISSION;

/**
 * Fragment to show the applications settings
 *
 * @author WilliBoelke
 */
public class FragmentOptions extends Fragment
{

    //------------Static Variables------------

    /**
     * Fragment ID used to identify the fragment
     * (for example by putting the ID into the Intent extra )
     */
    public static String ID = "Options";


    //------------Constructors------------

    public FragmentOptions()
    {
        // doesn't do anything special
    }


    //------------Instance Variables------------

    /**
     * Log tag
     */
    private final String TAG = getClass().getSimpleName();
    /**
     * the view
     */
    private View view;
    /**
     * The ViewModel of this Fragment
     */
    private ViewModelOptions viewModel;


    //------------Activity/Fragment Lifecycle------------

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
        setHasOptionsMenu(true);
        viewModel = ViewModelProviders.of(this).get(ViewModelOptions.class);
        viewModel.init();
        this.setUpDarkModeToggle();
        this.setupAllowAccessLocationToggle();
        this.setupAllowWriteLocalStorageToggle();
        this.setupIDView();
    }


    //------------Setup Views------------

    /**
     * Setup the allowWriteLocalStorage Switch
     */
    private void setupAllowWriteLocalStorageToggle()
    {
        Switch allowWriteLocalStorageToggle = view.findViewById(R.id.allow_write_storage_switch);
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
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
                    requestPermissions(params, REQUEST_CODE_WRITE_STORAGE_PERMISSION);
                }
                else
                {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getActivity().getPackageName(), null));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Setup the allowAccessLocation Switch
     */
    private void setupAllowAccessLocationToggle()
    {
        Switch allowLocationToggle = view.findViewById(R.id.allow_access_location_switch);
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
                    requestPermissions(params, REQUEST_CODE_ACCESS_LOCATION_PERMISSION);
                }
                else
                {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getActivity().getPackageName(), null));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
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

        Switch darkModeToggle = view.findViewById(R.id.dark_mode_switch);

        //Set the switch to "checked" in chase the darkmode is enabled
        if (viewModel.darkModeEnabled() == true)
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
                    viewModel.enableDarkMode();
                    Log.v(TAG, "Setup DarkMode toggle: Mode changed to dark ");
                    reset();
                }
                else
                {
                    viewModel.disableDarkMode();
                    Log.v(TAG, "Setup DarkMode toggle: Mode changed to light ");
                    reset();
                }
            }
        });
    }

    private void setupIDView()
    {
        TextView idTextView = view.findViewById(R.id.your_id_tv);
        idTextView.setText(viewModel.getUserID());
    }


    //------------Request------------

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case REQUEST_CODE_ACCESS_LOCATION_PERMISSION:
            {
                Toast.makeText(getActivity().getApplicationContext(), "Location access granted", Toast.LENGTH_SHORT).show();
            }
            break;
            case REQUEST_CODE_WRITE_STORAGE_PERMISSION:
            {
                Toast.makeText(getActivity().getApplicationContext(), "Allowed to access local storage", Toast.LENGTH_SHORT).show();
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    //------------Others------------

    /**
     * reloads the MainActivity  in order to enable/disable the darkmode
     */
    private void reset()
    {
        Log.v(TAG, "Reset Activity ");
        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        intent.putExtra("Fragment", ID);
        startActivity(intent);
    }


    //------------Toolbar Menu------------

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.actionbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.ab_menu_about:
                //no implemented here,
                return false;
            case R.id.ab_menu_faq:
                //no implemented here,
                return false;
            case R.id.ab_menu_settings:
                //no implemented here
                return false;
            case R.id.ab_menu_search:

        }
        return super.onOptionsItemSelected(item);
    }
}
