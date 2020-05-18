package de.htwBerlin.ois.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.reader.header.MapFileException;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.io.File;
import java.util.List;

import de.htwBerlin.ois.FileStructure.MapFileSingleton;
import de.htwBerlin.ois.R;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Fragment to display a the map file
 * from {@link MapFileSingleton}
 *
 * @author WilliBoelke
 */
public class NavigationFragment extends Fragment
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


    //------------Static Variables------------

    /**
     * Fragment ID used to identify the fragment
     * (for example by putting the ID into the Intent extra )
     */
    public static String ID = "Navigation";


    //------------Activity/Fragment Lifecycle------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_navigation, container, false);
        AndroidGraphicFactory.createInstance(getActivity().getApplication());
        setupLocateFab();
        setupMap();
        return view;
    }


    //------------Setup Views------------

    /**
     * Initializes the mapView
     */
    private void setupMap()
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
            mapView.setCenter(new LatLong(52.517037, 13.38886));
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

                    Location location = getLastKnownLocation();
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


    //------------Others------------

    /**
     * Gets the last know position of the device from the android LocationManager
     *
     * @return Location of the device
     */
    private Location getLastKnownLocation()
    {
        LocationManager mLocationManager = (LocationManager) getActivity().getApplicationContext().getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers)
        {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                //just here to dismiss the warning/error ...checked it before
                return null;
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null)
            {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy())
            {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}
