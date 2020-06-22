package de.htwBerlin.ois.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import de.htwBerlin.ois.R;
import de.htwBerlin.ois.serverCommunication.HttpClient;
import de.htwBerlin.ois.viewModels.ViewModelRequestNewMap;

/**
 * Fragment to allow the user to request a new map
 *
 * @author WilliBoelke
 */
public class FragmentRequestNewMap extends Fragment
{


    //------------Instance Variables------------

    private ViewModelRequestNewMap viewModel;
    private final String TAG = this.getClass().getSimpleName();
    /**
     * The View
     */
    private View view;
    /**
     * Date picker to let the user pick the date of the map
     */
    private DatePicker datePicker;
    private Button requestButton;
    private EditText latitudeMax;
    private EditText latitudeMin;
    private EditText longitudeMax;
    private EditText longitudeMin;
    private EditText name;
    private HttpClient httpClient;


    //------------Constructors------------

    public FragmentRequestNewMap(HttpClient httpClient)
    {
        this.httpClient = httpClient;
    }


    //------------Activity/Fragment Lifecycle------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_request_map, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d(TAG, "onActivityCreated : initializing ViewModel");
        viewModel = ViewModelProviders.of(this).get(ViewModelRequestNewMap.class);
        viewModel.init();
        //SetupViews
        datePicker = view.findViewById(R.id.date_picker);
        datePicker.setCalendarViewShown(false);        //no calender view
        name = view.findViewById(R.id.name_et);
        requestButton = view.findViewById(R.id.request_button);
        latitudeMax = view.findViewById(R.id.lat_max_et);
        latitudeMin = view.findViewById(R.id.lat_min_et);
        longitudeMax = view.findViewById(R.id.long_max_et);
        longitudeMin = view.findViewById(R.id.long_min_et);


        requestButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (viewModel.checkForNullName(getActivity(), name) == true && viewModel.checkForNullCoordinates(getActivity(), latitudeMax, latitudeMin, longitudeMin, longitudeMax) == true)
                {
                    String coords = viewModel.getCoordinatesAsString(latitudeMax, latitudeMin, longitudeMin, longitudeMax);
                    String date = viewModel.getDatePickerValuesAsString(datePicker);
                    String request = viewModel.buildParamsString(name.getText().toString(), coords, date);
                    viewModel.RequestMap(request);
                }
            }
        });
    }

    //------------Toolbar Menu------------

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.actionbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.ab_menu_about:
                //no implemented here,
                return false;
            case R.id.ab_menu_faq:
                //no implemented here,
                return false;
            case R.id.ab_menu_settings:
                //no implemented here
                return false;
            case R.id.ab_menu_search:

        }
        return super.onOptionsItemSelected(item);
    }

}