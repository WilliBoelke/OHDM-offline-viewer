package de.htwBerlin.ois.Fragments;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.htwBerlin.ois.R;

public class RequestMapFragment extends Fragment
{
    /**
     * The View
     */
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_request_map, container, false);
        return view;
    }


}
