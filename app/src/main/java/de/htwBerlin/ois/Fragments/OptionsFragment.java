package de.htwBerlin.ois.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.htwBerlin.ois.R;

public class OptionsFragment extends Fragment {

    public OptionsFragment() {
        // Required empty public constructor
    }

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_options, container, false);

        // Inflate the layout for this fragment
        return view;
    }
}
