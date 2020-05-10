package de.htwBerlin.ois.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.htwBerlin.ois.R;

/**
 * Fragment showing about OHDM information
 * Can be reached by the user via the Toolbars 3-dod menu
 */
public class AboutFragment extends Fragment
{
    /**
     * Fragment ID used to identify the fragment
     * (for example by putting the ID into the Intent extra )
     */
    public static String ID = "About";

    /**
     * Log tag
     */
    private final String TAG = this.getClass().getSimpleName();
    /**
     * Fragments view
     */
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //inflating the view
        view = inflater.inflate(R.layout.fragment_about, container, false);
        return view;
    }


}
