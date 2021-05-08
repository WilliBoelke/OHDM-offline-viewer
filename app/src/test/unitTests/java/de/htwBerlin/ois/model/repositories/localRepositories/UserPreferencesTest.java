package de.htwBerlin.ois.model.repositories.localRepositories;

import android.content.SharedPreferences;

import org.junit.Test;

import de.htwBerlin.ois.model.repositories.localRepositories.UserPreferences;

import static de.htwBerlin.ois.model.repositories.localRepositories.UserPreferences.DARK_MODE;
import static de.htwBerlin.ois.model.repositories.localRepositories.UserPreferences.USER_ID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserPreferencesTest
{
    private SharedPreferences mockPrefs = mock(SharedPreferences.class);
    private SharedPreferences.Editor mockEdit = mock(SharedPreferences.Editor.class, RETURNS_DEEP_STUBS);
    private UserPreferences prefs;


    @Test
    public void initTest()
    {
        prefs = UserPreferences.getInstance();
        prefs.init(mockPrefs);
        verify(mockPrefs).getBoolean(DARK_MODE, false);
        verify(mockPrefs).getString(USER_ID, null);
    }

    @Test
    public void darkModeEnabledTest()
    {
        when(mockPrefs.getBoolean(DARK_MODE, false)).thenReturn(true);
        ;
        prefs = UserPreferences.getInstance();
        prefs.init(mockPrefs);
        assertTrue(prefs.isDarkModeEnabled());
    }

    @Test
    public void darkModeDisabledTest()
    {
        when(mockPrefs.getBoolean(DARK_MODE, false)).thenReturn(false);
        prefs = UserPreferences.getInstance();
        prefs.init(mockPrefs);
        assertTrue(prefs.isDarkModeEnabled() == false);
    }
    @Test
    public void enableDarkModeTest()
    {
        UserPreferences prefs = UserPreferences.getInstance();
        prefs.init(mockPrefs);
        when(mockPrefs.edit()).thenReturn(mockEdit);
        prefs.enableDarkmode(true);
        // TODO verify(mockEdit).putBoolean(any(), any());
        assertTrue(prefs.isDarkModeEnabled());
        prefs.enableDarkmode(false);
        // TODO verify(mockEdit).putBoolean(any(), any());
        assertFalse(prefs.isDarkModeEnabled());
    }

    @Test
    public void getIdTest()
    {
        when(mockPrefs.getString(USER_ID, null)).thenReturn("user12id");
        UserPreferences prefs = UserPreferences.getInstance();
        prefs.init(mockPrefs);
        assertTrue(prefs.getUserID().equals("user12id"));
    }

    @Test
    public void setIdTest()
    {
        when(mockPrefs.getString(USER_ID, null)).thenReturn("user12id");
        UserPreferences prefs = UserPreferences.getInstance();
        prefs.init(mockPrefs);
        when(mockPrefs.edit()).thenReturn(mockEdit);
        prefs.setUserId("newuserID123");
        assertTrue(prefs.getUserID().equals("newuserID123"));
    }

}
