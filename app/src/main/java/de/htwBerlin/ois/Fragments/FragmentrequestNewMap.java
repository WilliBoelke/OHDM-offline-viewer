package de.htwBerlin.ois.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import de.htwBerlin.ois.R;
import de.htwBerlin.ois.ServerCommunication.HTTPRequestNewMap;

/**
 * Fragment to allow the user to request a new map
 *
 * @author WilliBoelke
 */
public class FragmentrequestNewMap extends Fragment
{
    //------------Instance Variables------------

    /**
     * Fragment ID used to identify the fragment
     * (for example by putting the ID into the Intent extra )
     */
    public static String ID = "RequestMap";
    /**
     * Log Tag
     */
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

    private EditText latitudeTop;
    private EditText latitudeBottom;
    private EditText longitudeRight;
    private EditText longitudeLeft;

    public static final int MIN_LATITUDE = -90;
    public static final int MAX_LATITUDE = 90;
    public static final int MAX_LONGITUDE = 180;
    public static final int MIN_LONGITUDE = -180;

    //------------Static Variables------------
    private EditText name;


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
        datePicker = view.findViewById(R.id.simpleDatePicker);
        //no calender view
        datePicker.setCalendarViewShown(false);
        name = view.findViewById(R.id.name_et);
        requestButton = view.findViewById(R.id.request_button);
        latitudeTop = view.findViewById(R.id.lat_top_et);
        latitudeBottom = view.findViewById(R.id.lat_bottom_et);
        longitudeRight = view.findViewById(R.id.long_right_et);
        longitudeLeft = view.findViewById(R.id.long_left_et);


        requestButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (checkForNullName() == true && checkForNullCoordinates() == true)
                {
                    HTTPRequestNewMap httpRequestNewMap = new HTTPRequestNewMap(getDatePickerValuesAsString(), getCoordinatesAsString(), name.getText().toString());
                    httpRequestNewMap.execute();
                }
            }
        });

    }


    //------------User Input------------


    /**
     * Building a formatted string (jjjj-mm-dd), as its needed by the server
     *
     * @return Date as string
     */
    private String getDatePickerValuesAsString()
    {
        StringBuilder stringBuilder = new StringBuilder();

        //Building a string in format JJJJJ-MM-TT
        stringBuilder.append(datePicker.getYear());
        stringBuilder.append("-");
        stringBuilder.append(datePicker.getMonth());
        stringBuilder.append("-");
        stringBuilder.append(datePicker.getDayOfMonth());

        return stringBuilder.toString();
    }

    private String getCoordinatesAsString()
    {
        Log.i(TAG, "getCoordinatesAsString: creating coordinates string ...");
        StringBuilder stringBuilder = new StringBuilder();
        Log.i(TAG, "getCoordinatesAsString: String created");
        Log.i(TAG, "getCoordinatesAsString: result: " + stringBuilder.toString());
        return stringBuilder.toString();
    }


    //------------Check  User Input------------

    /**
     * Check if a name was entered by the user
     *
     * @return
     */
    private boolean checkForNullName()
    {
        if (name.getText().toString().length() == 0)
        {
            Toast.makeText(getActivity().getApplicationContext(), "Please enter a name ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Check if the user entered all necessary coordinates
     *
     * @return
     */
    private boolean checkForNullCoordinates()
    {
        if (latitudeTop.getText().toString().length() == 0)
        {
            Toast.makeText(getActivity(), "Please insert a top latitude ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (latitudeBottom.getText().toString().length() == 0)
        {
            Toast.makeText(getActivity(), "Please insert the bottom latitude ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (longitudeRight.getText().toString().length() == 0)
        {
            Toast.makeText(getActivity(), "Please insert the right longitude ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (longitudeLeft.getText().toString().length() == 0)
        {
            Toast.makeText(getActivity(), "Please insert the left longitude", Toast.LENGTH_SHORT).show();
            return false;
        }

        double latTop = Double.parseDouble(latitudeTop.getText().toString());
        double latBottom = Double.parseDouble(latitudeBottom.getText().toString());
        double longLeft = Double.parseDouble(longitudeLeft.getText().toString());
        double longRight = Double.parseDouble(longitudeRight.getText().toString());

        if (latTop< MIN_LATITUDE || latTop > MAX_LATITUDE)
        {
            Toast.makeText(getActivity(), "You inserted a invalid top latitude ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (latBottom< MIN_LATITUDE || latBottom > MAX_LATITUDE)
        {
            Toast.makeText(getActivity(), "You inserted a invalid bottom latitude ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (longRight< MIN_LONGITUDE || longRight > MAX_LONGITUDE)
        {
            Toast.makeText(getActivity(), "You inserted a invalid right longitude ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (longLeft < MIN_LONGITUDE || longLeft > MAX_LONGITUDE)
        {
            Toast.makeText(getActivity(), "You inserted a invalid left longitude ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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