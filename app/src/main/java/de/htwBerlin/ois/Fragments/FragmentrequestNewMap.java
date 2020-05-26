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
    /**
     * Coordinate pair 1 / upper left corner
     */
    private EditText coordx1;
    private EditText coordy1;
    /**
     * Coordinate pair 2 / upper right corner
     */
    private EditText coordx2;
    private EditText coordy2;
    /**
     * Coordinate pair 3 / bottom right corner
     */
    private EditText coordx3;
    private EditText coordy3;
    /**
     * Coordinate pair 4 / bottom left corner
     */
    private EditText coordx4;
    private EditText coordy4;


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
        coordx1 = view.findViewById(R.id.coord_x1_et);
        coordy1 = view.findViewById(R.id.coord_y1_et);
        coordx2 = view.findViewById(R.id.coord_x2_et);
        coordy2 = view.findViewById(R.id.coord_y2_et);
        coordx3 = view.findViewById(R.id.coord_x3_et);
        coordy3 = view.findViewById(R.id.coord_y3_et);
        coordx4 = view.findViewById(R.id.coord_x4_et);
        coordy4 = view.findViewById(R.id.coord_y4_et);


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
        stringBuilder.append(coordx1.getText().toString().trim());
        stringBuilder.append(",");
        stringBuilder.append(coordy1.getText().toString().trim());
        stringBuilder.append("_");
        stringBuilder.append(coordx2.getText().toString().trim());
        stringBuilder.append(",");
        stringBuilder.append(coordy2.getText().toString().trim());
        stringBuilder.append("_");
        stringBuilder.append(coordx3.getText().toString().trim());
        stringBuilder.append(",");
        stringBuilder.append(coordy3.getText().toString().trim());
        stringBuilder.append("_");
        stringBuilder.append(coordx4.getText().toString().trim());
        stringBuilder.append(",");
        stringBuilder.append(coordy4.getText().toString().trim());
        stringBuilder.append("_");
        stringBuilder.append(coordx1.getText().toString().trim());
        stringBuilder.append(",");
        stringBuilder.append(coordy1.getText().toString().trim());
        stringBuilder.append("_");
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
        if (coordx1.getText().toString().length() == 0)
        {
            Toast.makeText(getActivity(), "Please insert the Coordinate X1 ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (coordy1.getText().toString().length() == 0)
        {
            Toast.makeText(getActivity(), "Please insert the Coordinate Y1 ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (coordx2.getText().toString().length() == 0)
        {
            Toast.makeText(getActivity(), "Please insert the Coordinate X2 ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (coordy2.getText().toString().length() == 0)
        {
            Toast.makeText(getActivity(), "Please insert the Coordinate Y2", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (coordx3.getText().toString().length() == 0)
        {
            Toast.makeText(getActivity(), "Please insert the Coordinate X3 ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (coordy3.getText().toString().length() == 0)
        {
            Toast.makeText(getActivity(), "Please insert the Coordinate Y3 ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (coordx4.getText().toString().length() == 0)
        {
            Toast.makeText(getActivity(), "Please insert the Coordinate X4 ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (coordy4.getText().toString().length() == 0)
        {
            Toast.makeText(getActivity(), "Please insert the Coordinate Y4 ", Toast.LENGTH_SHORT).show();
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