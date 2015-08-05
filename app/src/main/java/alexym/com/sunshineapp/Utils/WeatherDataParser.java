package alexym.com.sunshineapp.Utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alexym on 25/06/15.
 */
public class WeatherDataParser {
    private static String TAG = "WeatherDataParser";;

    /**
     * Given a string of the form returned by the api call:
     * http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7
     * retrieve the maximum temperature for the day indicated by dayIndex
     * (Note: 0-indexed, so 0 would refer to the first day).
     */
    public static double getMaxTemperatureForDay(String weatherJsonStr, int dayIndex)
            throws JSONException {
        // TODO: add parsing code here
        JSONObject objectJson = new JSONObject(weatherJsonStr);
        JSONArray arrayJson = objectJson.getJSONArray("list");
       // JSONObject objectTempJson = new JSONObject(arrayJson.getJSONObject(dayIndex).getString("temp"));
        JSONObject objectDayJson = arrayJson.getJSONObject(dayIndex);
        JSONObject objectTempJson = objectDayJson.getJSONObject("temp");
        Log.i(TAG, objectTempJson.getString("max"));
        return objectTempJson.getDouble("max");
    }
}
