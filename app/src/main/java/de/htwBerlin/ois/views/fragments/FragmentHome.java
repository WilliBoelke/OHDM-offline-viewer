package de.htwBerlin.ois.views.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

import de.htwBerlin.ois.R;
import de.htwBerlin.ois.adapters.RecyclerAdapterLocalFiles;
import de.htwBerlin.ois.adapters.RecyclerAdapterSwipeGestures;
import de.htwBerlin.ois.adapters.SwipeCallbackLeft;
import de.htwBerlin.ois.adapters.SwipeCallbackRight;
import de.htwBerlin.ois.viewModels.FragmentHomeViewModel;

/**
 * Represents the HOME Tab and thus, the starting point for the OHDM Offline Viewer application
 *
 * @author morelly_t1
 * @author WilliBoelke
 */
public class FragmentHome extends Fragment
{

    //------------Instance Variables------------

    /**
     * The ViewModel of this Fragment
     */
    private FragmentHomeViewModel viewModel;
    /**
     * Fragment ID used to identify the fragment
     * (for example by putting the ID into the Intent extra )
     */
    public static String ID = "Home";
    /**
     * Log tag
     */
    private final String TAG = this.getClass().getSimpleName();
    /**
     * The view
     */
    private View view;
    /**
     * The RecyclerAdapter
     */
    private RecyclerAdapterLocalFiles recyclerAdapter;
    /**
     * The RecyclerView
     */
    private RecyclerView localMapsRecyclerView;


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
        setHasOptionsMenu(true);
        viewModel = ViewModelProviders.of(this).get(FragmentHomeViewModel.class);
        viewModel.init();
        //Views
        this.setupRecycler();
        //Observers
        this.onLocalMapsChangeObserver();
    }


    //------------Observers------------

    private void onLocalMapsChangeObserver()
    {
        viewModel.getLocalMapFiles().observe(this.getViewLifecycleOwner(), new Observer<ArrayList<File>>()
        {
            @Override
            public void onChanged(ArrayList<File> files)
            {
                recyclerAdapter.notifyDataSetChanged();
                if (files.size() == 0)
                {
                    localMapsRecyclerView.setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.content_card_home_info).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.ohdm_logo_iv).setVisibility(View.VISIBLE);
                }
                else
                {
                    localMapsRecyclerView.setVisibility(View.VISIBLE);
                    view.findViewById(R.id.content_card_home_info).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.ohdm_logo_iv).setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    //------------Setup Views------------

    /**
     * Setup method for the RecyclerView
     */
    private void setupRecycler()
    {
        localMapsRecyclerView = view.findViewById(R.id.local_maps_recycler);
        RecyclerView.LayoutManager recyclerLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerAdapter = new RecyclerAdapterLocalFiles(this.getContext(), viewModel.getLocalMapFiles().getValue(), R.layout.recycler_item_vertical);

        //The itemTouchhelper for the swipe gestures on the recycler Items
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerAdapterSwipeGestures(this.swipeCallbackRight, this.swipeCallbackLeft));

        //Putting everything together
        itemTouchHelper.attachToRecyclerView(localMapsRecyclerView);

        //Putting everything together
        localMapsRecyclerView.setLayoutManager(recyclerLayoutManager);
        localMapsRecyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
    }

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

    private SwipeCallbackLeft swipeCallbackLeft = new SwipeCallbackLeft()
    {
        @Override
        public void onLeftSwipe(int position)
        {
            viewModel.chooseMapAt(position);
    }
    };
    private SwipeCallbackRight swipeCallbackRight = new SwipeCallbackRight()
    {
        @Override
        public void onRightSwipe(int position)
        {
            viewModel.deleteLocalMapFile(position);
        }
    };

}
