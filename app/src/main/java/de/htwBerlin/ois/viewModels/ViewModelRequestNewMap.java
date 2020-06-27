package de.htwBerlin.ois.viewModels;

import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import de.htwBerlin.ois.model.models.fileStructure.RemoteDirectory;
import de.htwBerlin.ois.model.models.fileStructure.RemoteFile;
import de.htwBerlin.ois.model.repositories.localRepositories.UserPreferences;
import de.htwBerlin.ois.serverCommunication.AsyncResponse;
import de.htwBerlin.ois.serverCommunication.HttpClient;
import de.htwBerlin.ois.serverCommunication.HttpTaskRequest;

import static de.htwBerlin.ois.serverCommunication.HttpTaskRequest.REQUEST_TYPE_MAP_REQUEST;

public class ViewModelRequestNewMap extends ViewModel
{


    //------------Static Variables------------

    public static final int MIN_LATITUDE = -180;
    public static final int MAX_LATITUDE = 180;
    public static final int MAX_LONGITUDE = 90;
    public static final int MIN_LONGITUDE = -90;


    //------------Instance Variables------------

    private final String TAG = getClass().getSimpleName();

    public void init()
    {
    }

    //------------Process User Input------------


    /**
     *c
     *
     * @return Date as string
     */
    public String getDatePickerValuesAsString(DatePicker datePicker)
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
     *
     * @return
     */
    public String getCoordinatesAsString(EditText latitudeMax, EditText latitudeMin, EditText longitudeMin, EditText longitudeMax)
    {
        String latMax = latitudeMax.getText().toString();
        String latMin = latitudeMin.getText().toString();
        String longMin = longitudeMin.getText().toString();
        String longMax = longitudeMax.getText().toString();

        Log.i(TAG, "getCoordinatesAsString: creating coordinates string ...");
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(longMin);
        stringBuilder.append(", ");
        stringBuilder.append(latMin);
        stringBuilder.append("_");

        stringBuilder.append(longMin);
        stringBuilder.append(", ");
        stringBuilder.append(latMax);
        stringBuilder.append("_");

        stringBuilder.append(longMax);
        stringBuilder.append(", ");
        stringBuilder.append(latMax);
        stringBuilder.append("_");

        stringBuilder.append(longMax);
        stringBuilder.append(", ");
        stringBuilder.append(latMin);
        stringBuilder.append("_");

        stringBuilder.append(longMin);
        stringBuilder.append(", ");
        stringBuilder.append(latMin);
        Log.i(TAG, "getCoordinatesAsString: String created");
        Log.i(TAG, "getCoordinatesAsString: result: " + stringBuilder.toString());
        return stringBuilder.toString();
    }


    /**
     * Builds a string as accepted by the server
     * example:
     * name=mapname&coords=13.005,15.123_13.005,15.123_13.005,15.123_13.005,15.123_13.005,15.123&date=2117-12-11&id=1JWd3wc
     * <p>
     * note: the id part is optional
     *
     * @return the String
     */
    public String buildParamsString(String name, String coords, String date)
    {
        Log.d(TAG, "buildParamsString : building params string...");
        StringBuilder sb = new StringBuilder();
        sb.append("name=");
        sb.append(name);
        sb.append("&coords=");
        sb.append(coords);
        sb.append("&date=");
        sb.append(date);
        sb.append("&id=");
        sb.append(UserPreferences.getInstance().getUserID());
        Log.d(TAG, "buildParamsString :  finished building params string");
        return sb.toString();
    }


    //------------Check  User Input------------

    /**
     * Check if a name was entered by the user
     *
     * @return
     */
    public boolean checkForNullName(Context context, EditText name)
    {
        if (name.getText().toString().length() == 0)
        {
            Toast.makeText(context.getApplicationContext(), "Please enter a name ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Check if the user entered all necessary coordinates
     *
     * @return
     */
    public boolean checkForNullCoordinates(Context context, EditText latitudeMax, EditText latitudeMin, EditText longitudeMin, EditText longitudeMax)
    {
        if (latitudeMax.getText().toString().length() == 0)
        {
            Toast.makeText(context, "Please insert a top latitude ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (latitudeMin.getText().toString().length() == 0)
        {
            Toast.makeText(context, "Please insert the bottom latitude ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (longitudeMax.getText().toString().length() == 0)
        {
            Toast.makeText(context, "Please insert the right longitude ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (longitudeMin.getText().toString().length() == 0)
        {
            Toast.makeText(context, "Please insert the left longitude", Toast.LENGTH_SHORT).show();
            return false;
        }

        double longTop = Double.parseDouble(latitudeMax.getText().toString());
        double longBottom = Double.parseDouble(latitudeMin.getText().toString());
        double latLeft = Double.parseDouble(longitudeMin.getText().toString());
        double latRight = Double.parseDouble(longitudeMax.getText().toString());

        if (longTop < MIN_LONGITUDE || longTop > MAX_LONGITUDE)
        {
            Toast.makeText(context, "You inserted a invalid top latitude ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (longBottom < MIN_LONGITUDE || longBottom > MAX_LONGITUDE)
        {
            Toast.makeText(context, "You inserted a invalid bottom latitude ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (latRight < MIN_LATITUDE || latRight > MAX_LATITUDE)
        {
            Toast.makeText(context, "You inserted a invalid right longitude ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (latLeft < MIN_LATITUDE || latLeft > MAX_LATITUDE)
        {
            Toast.makeText(context, "You inserted a invalid left longitude ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    /**
     * Todo put this into a repository
     * @param request
     */
    public void RequestMap(String request)
    {
        HttpTaskRequest httpRequest = new HttpTaskRequest();
        httpRequest.setRequestType(REQUEST_TYPE_MAP_REQUEST);
        httpRequest.setParams(request);
        httpRequest.setHttpClient(new HttpClient());
        httpRequest.setAsyncResponse(new AsyncResponse()
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
                if (response == null)
                {
                    //  Toast.makeText(getActivity().getApplicationContext(), "The server doesn't respond, try it again later", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //  Toast.makeText(getActivity().getApplicationContext(), "The server received your request", Toast.LENGTH_SHORT).show();
                }
            }
        });
        httpRequest.execute();
    }
}
