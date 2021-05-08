package de.htwBerlin.ois.model.repositories.localRepositories;

import android.content.Context;
import android.content.SharedPreferences;

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
    private boolean DarkMode;
    private String ID;
    private SharedPreferences prefs;


    public static UserPreferences getInstance()
    {
        if (instance == null)
        {
            instance = new UserPreferences();
        }
        return instance;
    }


    public void init( SharedPreferences prefs)
    {
        this.prefs = prefs;
        ID = prefs.getString(USER_ID, null);
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
        prefs.edit().putString(USER_ID, id).commit();
    }

    public void enableDarkmode(Boolean toggle)
    {
        DarkMode = toggle;
        prefs.edit().putBoolean(DARK_MODE, toggle).commit();
    }
}
