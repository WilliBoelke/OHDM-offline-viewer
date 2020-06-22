package de.htwBerlin.ois.models.repositories.localRepositories;

import android.content.Context;
import android.content.SharedPreferences;

import de.htwBerlin.ois.views.fragments.FragmentOptions;

import static de.htwBerlin.ois.views.fragments.FragmentOptions.SERVER_ID;

/**
 * This class is used to get all necessary values
 * (for example from the shared preferences) and make them available in the whole app
 * <p>
 * Examples: users request id
 *
 * @author WilliBoelke
 */
public class RuntimeVariables
{
    private static RuntimeVariables instance;
    private Context context;

    public static RuntimeVariables Instance()
    {
        if (instance == null)
        {
            instance = new RuntimeVariables();
        }
        return instance;
    }

    public void init(Context context)
    {
        this.context = context;
    }

    public String getUserID()
    {
        SharedPreferences prefs = context.getSharedPreferences(FragmentOptions.SETTINGS_SHARED_PREFERENCES, 0);
        return  prefs.getString(SERVER_ID, "");
    }
}
