package de.htwBerlin.ois.viewModels;

import androidx.lifecycle.ViewModel;

import de.htwBerlin.ois.model.repositories.localRepositories.UserPreferences;

/**
 * ViewModel for the {@link de.htwBerlin.ois.views.fragments.FragmentOptions}
 * Communicates with {@link de.htwBerlin.ois.model.repositories.localRepositories.UserPreferences}
 * to save and retrieve user settings
 *
 * @author WilliBoelke
 */
public class ViewModelOptions extends ViewModel
{

    private UserPreferences prefs;

    public void init()
    {
        prefs = UserPreferences.getInstance();
    }


    public String getUserID()
    {
        return prefs.getUserID();
    }

    public boolean darkModeEnabled()
    {
        return prefs.isDarkModeEnabled();
    }

    public void enableDarkMode()
    {
        prefs.enableDarkmode(true);
    }

    public void disableDarkMode()
    {
        prefs.enableDarkmode(false);
    }
}
