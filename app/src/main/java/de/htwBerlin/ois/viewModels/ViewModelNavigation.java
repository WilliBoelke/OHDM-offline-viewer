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

    //------------Others------------


    MutableLiveData<Integer> zoomLevel;

    public void init()
    {
        zoomLevel.setValue(50);
    }

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

    public LiveData<Integer> getZoomLevel()
    {
        return zoomLevel;
    }
    public void zoomIn(int currentZoom)
    {
        zoomLevel.setValue(currentZoom - 1);
    }

    public void zoomOut(int currentZoom)
    {
        zoomLevel.setValue(currentZoom+1);
    }
}
