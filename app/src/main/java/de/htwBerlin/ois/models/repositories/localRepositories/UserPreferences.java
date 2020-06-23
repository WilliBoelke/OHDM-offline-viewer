package de.htwBerlin.ois.models.repositories.localRepositories;

import android.content.Context;
import android.content.SharedPreferences;

import de.htwBerlin.ois.views.fragments.FragmentOptions;

/**
 * This class is used to get all necessary values
 * (for example from the shared preferences) and
 * make them available in the whole app
 * <p>
 * Examples: users request id
 *
 * @author WilliBoelke
 */
public class UserPreferences
{

    //------------Static Variables------------

    /**
     * SharedPreferences name
     */
    public static final String SETTINGS_SHARED_PREFERENCES = "OHDMViewerSettings";

    /**
     * Key to get the DarkMode boolean from the SharedPreferences
     */
    public static final String DARK_MODE = "darkmode_settings";
    /**
     * Key to get the User ID from the SharedPreferences
     */
    public static final String USER_ID = "server_id";

    private static UserPreferences instance;
    private Context context;
    private boolean DarkMode;
    private String ID;
    private    SharedPreferences prefs ;


    public static UserPreferences getInstance()
    {
        if (instance == null)
        {
            instance = new UserPreferences();
        }
        return instance;
    }


    public void init(Context context)
    {
        this.context = context;
         prefs = context.getSharedPreferences(SETTINGS_SHARED_PREFERENCES, 0);

        ID = prefs.getString(USER_ID, "");
        DarkMode = prefs.getBoolean(DARK_MODE, false);
    }

    public String getUserID()
    {
        return ID;
    }

    public boolean isDarkModeEnabled()
    {
        return DarkMode;
    }

    public void setUserId(String id)
    {
        ID = id;
        prefs.edit().putString(USER_ID, id);
        prefs.edit().commit();
    }

    public void enableDarkmode(Boolean toggle)
    {
        DarkMode = toggle ;
        prefs.edit().putBoolean(DARK_MODE, toggle);
        prefs.edit().commit();
    }
}
