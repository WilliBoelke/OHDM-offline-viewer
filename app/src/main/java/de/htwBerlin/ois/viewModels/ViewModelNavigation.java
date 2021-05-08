package de.htwBerlin.ois.viewModels;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

public class ViewModelNavigation extends ViewModel
{

    //------------Instance Variables------------

    private MutableLiveData<Integer> zoomLevel;


    //------------Constructors------------

    /**
     *
     */
    public void init()
    {
        zoomLevel = new MutableLiveData<>(20);
    }


    //------------Location------------

    /**
     * Gets the last know position of the device from the android LocationManager
     *
     * @return Location of the device
     */
    public Location getLastKnownLocation(Context context)
    {
        LocationManager mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers)
        {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
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


    //------------Zoom Level------------

    /**
     * Getter for teh zoomLevel LiveData which will be observed by {@link de.htwBerlin.ois.views.fragments.FragmentNavigation}
     */
    public LiveData<Integer> getZoomLevel()
    {
        return  zoomLevel;
    }

    /**
     * increments  the parameter zoomLevel by one and sets it as value of the LiveData
     * the parameter zoomLevel is needed because the MapsView has its of zoomLevel
     * which may have changed while the value of the LiveData stayed the same
     * @param zoomLevel
     */
    public void zoomIn(byte zoomLevel)
    {
        this.zoomLevel.setValue(zoomLevel  + 1);
    }

    /**
     * decrements the parameter zoomLevel by one and sets it as value of the LiveData
     * the parameter zoomLevel is needed because the MapsView has its of zoomLevel
     * which may have changed while the value of the LiveData stayed the same
     * @param zoomLevel
     */
    public void zoomOut(byte zoomLevel)
    {
        this.zoomLevel.setValue(  zoomLevel - 1);
    }

}
