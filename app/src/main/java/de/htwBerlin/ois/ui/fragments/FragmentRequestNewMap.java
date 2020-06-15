package de.htwBerlin.ois.ui.fragments;

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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import de.htwBerlin.ois.R;
import de.htwBerlin.ois.fileStructure.RemoteDirectory;
import de.htwBerlin.ois.fileStructure.RemoteFile;
import de.htwBerlin.ois.serverCommunication.AsyncResponse;
import de.htwBerlin.ois.serverCommunication.HttpRequest;

/**
 * Fragment to allow the user to request a new map
 *
 * @author WilliBoelke
 */
public class FragmentRequestNewMap extends Fragment
{
    //------------Instance Variables------------

    public static final int MIN_LATITUDE = -180;
    public static final int MAX_LATITUDE = 180;
    public static final int MAX_LONGITUDE = 90;
    public static final int MIN_LONGITUDE = -90;
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
    private EditText longitudeTop;
    private EditText longitudeBottom;
    private EditText latitudeRight;
    private EditText latitudeLeft;
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
        datePicker = view.findViewById(R.id.date_picker);
        //no calender view
        datePicker.setCalendarViewShown(false);
        name = view.findViewById(R.id.name_et);
        requestButton = view.findViewById(R.id.request_button);
        longitudeTop = view.findViewById(R.id.long_top_et);
        longitudeBottom = view.findViewById(R.id.long_bottom_et);
        latitudeRight = view.findViewById(R.id.lat_right_et);
        latitudeLeft = view.findViewById(R.id.lat_left_et);


        requestButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (checkForNullName() == true && checkForNullCoordinates() == true)
                {
                    HttpRequest httpRequestNewMap = new HttpRequest(buildParamsString(),
                    new AsyncResponse()
                    {
                        @Override
                        public void getRemoteFiles(ArrayList<RemoteFile> remoteFiles)
                        {
                            //not needed here
                        }

                        @Override
                        public void getRemoteDirectories(ArrayList<RemoteDirectory> remoteDirectories)
                        {
                            //not needed here
                        }

                        @Override
                        public void getHttpResponse(String response)
                        {

                        }
                    });
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

    /**
     * Building a String out of the coordinates
     * Format: Top,Left_Top,Right_Bottom, Right_Bottom,Left_Top,Left
     *
     * @return
     */
    private String getCoordinatesAsString()
    {
        String longTop = longitudeTop.getText().toString();
        String longBottom = longitudeBottom.getText().toString();
        String latLeft = latitudeLeft.getText().toString();
        String latRight = latitudeRight.getText().toString();

        Log.i(TAG, "getCoordinatesAsString: creating coordinates string ...");
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(longTop);
        stringBuilder.append(", ");
        stringBuilder.append(latLeft);
        stringBuilder.append("_");

        stringBuilder.append(longTop);
        stringBuilder.append(", ");
        stringBuilder.append(latRight);
        stringBuilder.append("_");

        stringBuilder.append(longBottom);
        stringBuilder.append(", ");
        stringBuilder.append(latRight);
        stringBuilder.append("_");

        stringBuilder.append(longBottom);
        stringBuilder.append(", ");
        stringBuilder.append(latLeft);
        stringBuilder.append("_");

        stringBuilder.append(longTop);
        stringBuilder.append(", ");
        stringBuilder.append(latLeft);
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
        if (longitudeTop.getText().toString().length() == 0)
        {
            Toast.makeText(getActivity(), "Please insert a top latitude ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (longitudeBottom.getText().toString().length() == 0)
        {
            Toast.makeText(getActivity(), "Please insert the bottom latitude ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (latitudeRight.getText().toString().length() == 0)
        {
            Toast.makeText(getActivity(), "Please insert the right longitude ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (latitudeLeft.getText().toString().length() == 0)
        {
            Toast.makeText(getActivity(), "Please insert the left longitude", Toast.LENGTH_SHORT).show();
            return false;
        }

        double longTop = Double.parseDouble(longitudeTop.getText().toString());
        double longBottom = Double.parseDouble(longitudeBottom.getText().toString());
        double latLeft = Double.parseDouble(latitudeLeft.getText().toString());
        double latRight = Double.parseDouble(latitudeRight.getText().toString());

        if (longTop < MIN_LONGITUDE || longTop > MAX_LONGITUDE)
        {
            Toast.makeText(getActivity(), "You inserted a invalid top latitude ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (longBottom < MIN_LONGITUDE || longBottom > MAX_LONGITUDE)
        {
            Toast.makeText(getActivity(), "You inserted a invalid bottom latitude ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (latRight < MIN_LATITUDE || latRight > MAX_LATITUDE)
        {
            Toast.makeText(getActivity(), "You inserted a invalid right longitude ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (latLeft < MIN_LATITUDE || latLeft > MAX_LATITUDE)
        {
            Toast.makeText(getActivity(), "You inserted a invalid left longitude ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Builds a string as accepted by the server
     * example:
     * name=mapname&coords=13.005,15.123_13.005,15.123_13.005,15.123_13.005,15.123_13.005,15.123&date=2117-12-11
     *
     * @return the String
     */
    private String buildParamsString()
    {
        Log.d(TAG, "buildParamsString : building params string...");
        StringBuilder sb = new StringBuilder();
        sb.append("name=");
        sb.append(this.name.getText().toString());
        sb.append("&coords=");
        sb.append(this.getCoordinatesAsString());
        sb.append("&date=");
        sb.append(this.getDatePickerValuesAsString());
        Log.d(TAG, "buildParamsString : builded params string");
        return sb.toString();
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