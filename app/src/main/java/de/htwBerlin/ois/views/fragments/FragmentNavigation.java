package de.htwBerlin.ois.views.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.IMapViewPosition;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.reader.header.MapFileException;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.io.File;

import de.htwBerlin.ois.R;
import de.htwBerlin.ois.model.repositories.localRepositories.MapFileSingleton;
import de.htwBerlin.ois.viewModels.ViewModelNavigation;

/**
 * Fragment to display a the map file
 * from {@link MapFileSingleton}
 *
 * @author WilliBoelke
 */
public class FragmentNavigation extends Fragment
{

    //------------Instance Variables------------

    /**
     * Log tag
     */
    private final String TAG = this.getClass().getSimpleName();
    /**
     * The view
     */
    private View view;
    /**
     * The MapView
     */
    private MapView mapView;
    /**
     * The View model
     */
    private ViewModelNavigation viewModel;


    //------------Constructors------------

    public FragmentNavigation()
    {
        // doesn't do anything special
    }


    //------------Activity/Fragment Lifecycle------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ViewModelNavigation.class);
        view = inflater.inflate(R.layout.fragment_navigation, container, false);
        AndroidGraphicFactory.createInstance(getActivity().getApplication());
        setupLocateFab();
        setupMapView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        /*
         * Whenever your activity exits, some cleanup operations have to be performed lest your app
         * runs out of memory.
         */
        try
        {
            //This sometimes throws a NullPointer
            mapView.destroyAll();
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }

        AndroidGraphicFactory.clearResourceMemoryCache();
        super.onDestroy();
    }


    //------------Setup Views------------

    /**
     * Initializes the mapView
     */
    private void setupMapView()
    {
        Log.i(TAG, "setting up map");
        mapView = view.findViewById(R.id.map);

        mapView.setClickable(true);
        mapView.getMapScaleBar().setVisible(true);

        TileCache tileCache = AndroidUtil.createTileCache(getActivity().getApplicationContext(), "mapcache", mapView.getModel().displayModel.getTileSize(), 1f, mapView.getModel().frameBufferModel.getOverdrawFactor());

        File ohdmFile = MapFileSingleton.getInstance().getFile();
        MapDataStore mapDataStore = null;
        try
        {
            mapDataStore = new MapFile(ohdmFile);

            TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore, mapView.getModel().mapViewPosition, AndroidGraphicFactory.INSTANCE);
            tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);

            mapView.getLayerManager().getLayers().add(tileRendererLayer);
            mapView.setZoomLevel((byte) 12);

            Layers layers = mapView.getLayerManager().getLayers();
            IMapViewPosition position = mapView.getModel().mapViewPosition;

            TileRendererLayer trl = AndroidUtil.createTileRendererLayer(tileCache, position, mapDataStore, InternalRenderTheme.OSMARENDER, false, true, false);
            layers.add(trl);

            position = mapView.getModel().mapViewPosition;
            position.setCenter(trl.getMapDataStore().startPosition());

        }
        catch (MapFileException e)
        {
            Toast.makeText(getActivity().getApplicationContext(), "Your map file is invalid", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * On Click for the LocateFAb
     * Sets the map center to the last know position of the device
     */
    private void setupLocateFab()
    {
        FloatingActionButton locateFab = view.findViewById(R.id.locate_fab);
        locateFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    Location location = viewModel.getLastKnownLocation(getActivity());
                    try
                    {
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();
                        mapView.setCenter(new LatLong(latitude, longitude));
                        mapView.setZoomLevel((byte) 16);
                    }
                    catch (NullPointerException e)
                    {
                        Log.e(TAG, "Lat or Long was null");
                        Toast.makeText(getActivity().getApplicationContext(), "Location not found", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(), "We need access to your location", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
