package de.htwBerlin.ois.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import de.htwBerlin.ois.MainActivityPackage.Database;
import de.htwBerlin.ois.MainActivityPackage.MainActivity;
import de.htwBerlin.ois.R;

/**
 * Represents the HOME Tab and thus, the starting point for the OHDM Offline Viewer application
 *
 * @author morelly_t1
 */
public class HomeFragment extends Fragment {

    private Set<File> mapFiles;

    private static final String TAG = "HomeFragment";

    private Spinner spinnerMapFile;
    private Button buttonSave;

    private View view;

    // onCreateView is just here to create the ViewInstance

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }

    // on ActivityCreated can be seen as "onCreate"
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        spinnerMapFile = view.findViewById(R.id.mapFileDropDown);
        buttonSave = view.findViewById(R.id.buttonSave);

        //TODO : needs a new implementation
        //fillDropDownFiles();
    }

    /**
     * Scans OHDM Directory for .map Files
     *
     * @return
     */
    protected Set<File> findMapFiles() {
        Set<File> maps = new HashSet<>();
        try {
            for (File osmfile : new File(MainActivity.MAP_FILE_PATH).listFiles()) {
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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Database.mainContext, R.layout.spinner_item, listSorted);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMapFile.setAdapter(dataAdapter);
    }

    @Override
    public void onStart() {
        //fillDropDownFiles(); TODO
        super.onStart();
    }

    @Override
    public void onResume() {
        //fillDropDownFiles(); TODO
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /*
    @OnClick(R.id.buttonSave)
    public void onClickSaveButton() {
        if (mapFiles.size() == 0) {
            final AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
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
            Intent navigationIntent = new Intent(HomeFragment.this, NavigationActivity.class);
            startActivity(navigationIntent);
        }
    }
    */
}
