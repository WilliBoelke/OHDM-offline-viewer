package de.htwBerlin.ois.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;

import de.htwBerlin.ois.FileStructure.SwipeCallbackLeft;
import de.htwBerlin.ois.FileStructure.MapFileSingleton;
import de.htwBerlin.ois.FileStructure.RecyclerAdapterLocalMaps;
import de.htwBerlin.ois.FileStructure.SwipeCallbackRight;
import de.htwBerlin.ois.FileStructure.RecyclerAdapterSwipeGestures;
import de.htwBerlin.ois.MainActivity.MainActivity;
import de.htwBerlin.ois.R;

/**
 * Represents the HOME Tab and thus, the starting point for the OHDM Offline Viewer application
 *
 * @author morelly_t1
 * @author WilliBoelke
 */
public class HomeFragment extends Fragment
{

    //------------Instance Variables------------

    /**
     * Log tag
     */
    private final String TAG = this.getClass().getSimpleName();
    /**
     * Set of the .map files ind the OHDM directory
     * Filled in {@link  #findMapFiles()}
     */
    private ArrayList<File> mapFiles;
    /**
     * The view
     */
    private View view;
    /**
     * The RecyclerAdapter
     */
    private RecyclerAdapterLocalMaps recyclerAdapter;
    /**
     * The RecyclerView
     */
    private RecyclerView localMapsRecyclerView;


    //------------Static Variables------------

    /**
     * Fragment ID used to identify the fragment
     * (for example by putting the ID into the Intent extra )
     */
    public static String ID = "Home";


    //------------Activity/Fragment Lifecycle------------

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
        mapFiles = findMapFiles();
        setHasOptionsMenu(true);
        setupRecycler();
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }


    //------------Setup Views------------

    private SwipeCallbackLeft swipeCallbackLeft = new SwipeCallbackLeft()
    {
        @Override
        public void onLeftSwipe(int position)
        {
            Log.i(TAG, "using " + mapFiles.get(position).getName() + " as mapfile");
            MapFileSingleton mapFile = MapFileSingleton.getInstance();
            mapFile.setFile(mapFiles.get(position));
            recyclerAdapter.notifyDataSetChanged();
        }
    };
    private SwipeCallbackRight swipeCallbackRight = new SwipeCallbackRight()
    {
        @Override
        public void onRightSwipe(int position)
        {
            mapFiles.get(position).delete();
            mapFiles.remove(position);
            recyclerAdapter.notifyDataSetChanged();
        }
    };


    //------------Others------------

    /**
     * Scans OHDM Directory for .map Files
     *
     * @return a set of .map files
     */
    private ArrayList<File> findMapFiles()
    {
        ArrayList<File> maps = new ArrayList<>();
        try
        {
            for (File mapFile : new File(MainActivity.MAP_FILE_PATH).listFiles())
            {
                if (mapFile.getName().endsWith(".map"))
                {
                    Log.i(TAG, "osmfile: " + mapFile.getName());
                    maps.add(mapFile);
                }
            }
        }
        catch (NullPointerException e)
        {
            Log.i(TAG, "No map files located.");
        }
        return maps;
    }


    //------------Toolbar Menu------------

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.actionbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.ab_menu_search).setVisible(true);
        setupSearchView((SearchView) searchItem.getActionView());
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


    //------------Swipe Gestures------------

    /**
     * Setup for the SearchView
     */
    private void setupSearchView(SearchView searchView)
    {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                try
                {
                    recyclerAdapter.getFilter().filter(newText);
                }
                catch (NullPointerException e)
                {
                    e.printStackTrace();
                }
                return false;
            }
        });

    }

    /**
     * Setup method for the RecyclerView
     */
    private void setupRecycler()
    {
        localMapsRecyclerView = view.findViewById(R.id.local_maps_recycler);

        if (!mapFiles.isEmpty())
        {
            //In case there are files in the local OHDM directory these two will be behind the recycler
            view.findViewById(R.id.content_card_home_info).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.ohdm_logo_iv).setVisibility(View.INVISIBLE);

            localMapsRecyclerView.setVisibility(View.VISIBLE);

            RecyclerView.LayoutManager recyclerLayoutManager = new LinearLayoutManager(this.getContext());
            recyclerAdapter = new RecyclerAdapterLocalMaps(this.getContext(), mapFiles, R.layout.recycler_item_vertical);

            //The itemTouchhelper for the swipe gestures on the recycler Items
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerAdapterSwipeGestures(recyclerAdapter, this.swipeCallbackRight, this.swipeCallbackLeft));


            //Putting everything together
            itemTouchHelper.attachToRecyclerView(localMapsRecyclerView);


            //Putting everything together
            localMapsRecyclerView.setLayoutManager(recyclerLayoutManager);
            localMapsRecyclerView.setAdapter(recyclerAdapter);
            recyclerAdapter.notifyDataSetChanged();
        }
    }
}
