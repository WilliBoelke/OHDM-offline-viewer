package de.htwBerlin.ois.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import de.htwBerlin.ois.R;

public class RequestMapFragment extends Fragment
{
    /**
     * Fragment ID used to identify the fragment
     * (for example by putting the ID into the Intent extra )
     */
    public static int ID = 3;

    /**
     * The View
     */
    private View view;
    /**
     * Date picker to let the user pick the date of the map
     */
    private DatePicker datePicker;

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

    private EditText name;


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

        datePicker = view.findViewById(R.id.simpleDatePicker);
        //no calender view
        datePicker.setCalendarViewShown(false);
        name = view.findViewById(R.id.name_et);

        coordx1 = view.findViewById(R.id.coord_x1_et);
        coordy1 = view.findViewById(R.id.coord_y1_et);
        coordx2 = view.findViewById(R.id.coord_x2_et);
        coordy2 = view.findViewById(R.id.coord_y2_et);
        coordx3 = view.findViewById(R.id.coord_x3_et);
        coordy3 = view.findViewById(R.id.coord_y3_et);
        coordx4 = view.findViewById(R.id.coord_x4_et);
        coordy4 = view.findViewById(R.id.coord_y4_et);
    }


    private String getCoordinatesAsString()
    {
        return null;
    }

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
}