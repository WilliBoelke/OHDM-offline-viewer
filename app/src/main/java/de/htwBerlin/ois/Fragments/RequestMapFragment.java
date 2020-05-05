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
        this.setupDatePicker();


        name = view.findViewById(R.id.name_et);
    }



    /**
     * Setup for the date picker
     */
    private void setupDatePicker()
    {
        datePicker = view.findViewById(R.id.simpleDatePicker);
        //no calender view
        datePicker.setCalendarViewShown(false);

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