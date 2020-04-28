package de.htwBerlin.ois.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.htwBerlin.ois.FileStructure.MapFileSingleton;
import de.htwBerlin.ois.MainActivityPackage.MainActivity;
import de.htwBerlin.ois.R;

/**
 * Represents the HOME Tab and thus, the starting point for the OHDM Offline Viewer application
 *
 * @author morelly_t1
 */
public class HomeFragment extends Fragment
{
    /**
     * Log tag
     */
    private final String TAG = this.getClass().getSimpleName();
    /**
     * Set of the .map files ind the OHDM directory
     * Filled in {@link  #findMapFiles()}
     */
    private Set<File> mapFiles;
    /**
     * Spinner to choose a .map file from
     */
    private Spinner spinnerMapFile;
    /**
     * Button to save the chosen .map file
     */
    private Button buttonSave;
    /**
     * The view
     */
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //inflating the view
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        //getting the views
        spinnerMapFile = view.findViewById(R.id.map_file_dropdown);
        buttonSave = view.findViewById(R.id.save_button);

        setupSaveButton();
        fillDropDownFiles();
    }

    /**
     * Scans OHDM Directory for .map Files
     *
     * @return a set of .map files
     */
    protected Set<File> findMapFiles()
    {
        Set<File> maps = new HashSet<>();
        try
        {
            for (File osmfile : new File(MainActivity.MAP_FILE_PATH).listFiles())
            {
                if (osmfile.getName().endsWith(".map"))
                {
                    Log.i(TAG, "osmfile: " + osmfile.getName());
                    maps.add(osmfile);
                }
            }
        }
        catch (NullPointerException e)
        {
            Log.i(TAG, "No map files located.");
        }
        return maps;
    }

    /**
     * Initializes Dropdown menu with .map-files
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_item, listSorted);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMapFile.setAdapter(dataAdapter);
    }

    /**
     * Setup the safeButton and its onClickListener
     */
    private void setupSaveButton()
    {
        buttonSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mapFiles.size() == 0)
                {
                    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity().getApplicationContext()).create();
                    alertDialog.setTitle("No Map Files found!");
                    alertDialog.setMessage("Either store map files in OHDM directory in internal Storage." + " Or Download available maps (see Tab \"Maps\").");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
                else
                {
                    for (File file : mapFiles)
                    {
                        if (file.getName().equals(spinnerMapFile.getSelectedItem().toString()))
                        {
                            Log.i(TAG, "User has choosen " + spinnerMapFile.getSelectedItem().toString());
                            Log.i(TAG, "using " + file.getName() + " as mapfile");
                            MapFileSingleton mapFile = MapFileSingleton.getInstance();
                            mapFile.setFile(file);
                        }
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onStart() {
        fillDropDownFiles();
        super.onStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        fillDropDownFiles();
        super.onResume();
    }

}
